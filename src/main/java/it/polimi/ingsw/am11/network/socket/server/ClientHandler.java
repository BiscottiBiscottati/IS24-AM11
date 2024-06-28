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
 * Each client connection is managed by a ClientHandler, which is executed in a separate thread.
 * This class is used to listen to a client after the connection is established. It reads the
 * nickname of the player, and then it starts the loop to read the messages from the client.
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final @NotNull Socket clientSocket;
    private final @NotNull BufferedReader in;
    private final @NotNull PrintWriter out;
    private final @NotNull PingHandler pingHandler;
    private final @NotNull ServerGameSender gameSender;
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
     * Stops the client handler and closes the connection, it will also stop the ping handler and
     * the message handler.
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
     * This method is called to start the client handler, it will wait for the nickname of the
     * player, and then it will start the loop to read the messages from the client if the nickname
     * is valid.
     */
    @Override
    public void run() {
        isRunning = true;
        ServerExceptionSender serverExceptionSender = new ServerExceptionSender(out);
        if (! readNickname(serverExceptionSender)) return;
        loopMessageRead();
    }

    /**
     * Reads the nickname from the client and connects the player to the game, if the nickname has
     * an invalid format, it continues to ask for a new nickname. If the nickname is in a valid
     * format, it tries to connect the player to the game. The success of the connection depends on
     * the requisites of the game.
     *
     * @param exceptionSender the exception sender used to throw exceptions to the client
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
     * While the client handler is running, it continuously reads the input from the client and
     * calls the {@link ServerMessageHandler} to handle the message.
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
     * Reads the input from the client and returns the message read. If the message is null, it
     * disconnects the player.
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

    /**
     * Sends a message to the client to disconnect from the server
     */
    public void youUgly() {
        gameSender.youUgly();
    }
}