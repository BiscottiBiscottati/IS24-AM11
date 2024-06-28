package it.polimi.ingsw.am11.network.socket.server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.exceptions.SocketCreationException;
import it.polimi.ingsw.am11.network.socket.server.chat.ServerChatReceiver;
import it.polimi.ingsw.am11.network.socket.server.chat.ServerChatSender;
import it.polimi.ingsw.am11.network.socket.server.game.ServerExceptionSender;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameReceiver;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameSender;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The class that handles the client socket connection
 * <p>
 * The class that handles the client socket connection and the communication with the server
 * <br>
 * It uses a {@link PingHandler} to handle the ping messages
 * <br>
 * It uses a {@link ServerGameSender} to send the game messages to the client
 * <br>
 * It uses a {@link ServerMessageHandler} to handle the messages received from the client
 * <br>
 * It uses a {@link Socket} to handle the connection
 * <br>
 * It uses a {@link BufferedReader} to read the messages from the client
 * <br>
 * It uses a {@link PrintWriter} to send the messages to the client
 * <br>
 * It uses a {@link Logger} to log the messages
 * </p>
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final @NotNull Socket clientSocket;
    private final @NotNull BufferedReader in;
    private final @NotNull PrintWriter out;
    private final @NotNull PingHandler pingHandler;
    private final ServerGameSender gameSender;
    private @Nullable ServerMessageHandler messageHandler;
    private @Nullable String nickname;
    private boolean isRunning;

    public ClientHandler(@NotNull Socket clientSocket) throws SocketCreationException {
        this.clientSocket = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while creating client handler", e);
            throw new SocketCreationException(e.getMessage());
        }

        pingHandler = new PingHandler(clientSocket, out);
        gameSender = new ServerGameSender(out);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
            LOGGER.info("SERVER TCP: Server ClientHandler {} closed", nickname);
        }));
    }

    /**
     * Stops the client handler
     * <p>
     * Stops the client handler and closes the connection
     * <br>
     * It sets the {@link #isRunning} to false
     * <br>
     * It closes the {@link PingHandler}
     * <br>
     * It closes the {@link ServerMessageHandler}
     * <br>
     * It closes the {@link Socket}
     * <br>
     * It closes the {@link BufferedReader}
     * <br>
     * It closes the {@link PrintWriter}
     * </p>
     */
    public void stop() {
        LOGGER.debug("SERVER TCP: Stopping client handler");
        isRunning = false;
        pingHandler.close();
        if (messageHandler != null) messageHandler.close();
        try {
            if (! clientSocket.isClosed()) clientSocket.close();
            in.close();
        } catch (IOException e) {
            LOGGER.debug("SERVER TCP: Error while closing client handler: {}", e.getMessage());
        }
        out.close();
    }

    /**
     * The method that runs the client handler
     * <p>
     * The method that runs the client handler and waits for the nickname
     * <br>
     * It uses a {@link ServerExceptionSender} to send the exceptions to the client
     * <br>
     * It calls the {@link #loopMessageRead()} to loop the message read
     * </p>
     */
    @Override
    public void run() {
        isRunning = true;
        ServerExceptionSender serverExceptionSender = new ServerExceptionSender(out);
        if (! readNickname(serverExceptionSender)) return;
        loopMessageRead();
    }

    /**
     * Reads the nickname from the client
     * <p>
     * Reads the nickname from the client and connects the player to the game
     * <br>
     * It uses a {@link ServerExceptionSender} to send the exceptions to the client
     * </p>
     *
     * @param exceptionSender the exception sender
     * @return true if the nickname is valid, false otherwise
     */
    private boolean readNickname(@NotNull ServerExceptionSender exceptionSender) {
        boolean validNickname = false;
        while (! validNickname) {
            LOGGER.info("SERVER TCP: Waiting for nickname");
            try {
                nickname = ServerGameReceiver.receiveNickname(in.readLine());
            } catch (IOException e) {
                LOGGER.error("SERVER TCP: Error while reading nickname", e);
                return false;
            }
            if (nickname == null) return false;
            if (nickname.isBlank()) {
                LOGGER.info("SERVER TCP: Retrying nickname");
                continue;
            }

            LOGGER.info("SERVER TCP: Received nickname: {}", nickname);

            try {
                ServerChatSender chatSender = new ServerChatSender(out);
                VirtualPlayerView view = CentralController.INSTANCE
                        .connectPlayer(nickname, gameSender, gameSender, chatSender);
                ServerGameReceiver messageReceiver =
                        new ServerGameReceiver(view, exceptionSender);
                ServerChatReceiver chatReceiver = new ServerChatReceiver(exceptionSender);

                messageHandler = new ServerMessageHandler(messageReceiver, pingHandler,
                                                          chatReceiver);
                validNickname = true;
                LOGGER.info("SERVER TCP: Player connected with name: {}", nickname);
            } catch (NumOfPlayersException |
                     NotSetNumOfPlayerException | PlayerInitException e) {
                LOGGER.error("SERVER TCP: Error while connecting player: {}", e.getMessage());
                exceptionSender.exception(e);
            } catch (GameStatusException e) {
                LOGGER.error("SERVER TCP: Error player trying to connect an ongoing game: {}",
                             e.getMessage());
                exceptionSender.exception(e);
                stop();
                return false;
            }
        }
        return true;
    }

    /**
     * Loops the message read
     * <p>
     * Loops the message read and calls the message handler to handle the message
     * <br>
     * It stops the client handler if the message is null
     * <br>
     * It throws an exception if the message handler is null
     * </p>
     */
    private void loopMessageRead() {
        while (isRunning) {
            String message = readInput();
            if (message == null) return;
            if (messageHandler == null) throw new IllegalStateException("Message handler not set");
            messageHandler.receive(message);
        }
    }

    /**
     * Reads the input from the client
     * <p>
     * Reads the input from the client and returns the message read
     * <br>
     * It logs the message received
     * <br>
     * It disconnects the player if the message is null
     * <br>
     * It catches the {@link IOException} and disconnects the player
     * </p>
     *
     * @return the message read from the client
     */
    private @Nullable String readInput() {
        try {
            String message = null;
            if (! clientSocket.isClosed()) message = in.readLine();
            if (message == null) {
                LOGGER.info("SERVER TCP: Received message: {}", (Object) null);
                LOGGER.info("SERVER TCP: Client {} disconnected", nickname);
                assert nickname != null;
                CentralController.INSTANCE.disconnectPlayer(nickname);
                isRunning = false;
            }
            return message;
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while reading input: {}", e.getMessage());
            assert nickname != null;
            CentralController.INSTANCE.disconnectPlayer(nickname);
            return null;
        }
    }

    public void youUgly() {
        gameSender.youUgly();
    }
}