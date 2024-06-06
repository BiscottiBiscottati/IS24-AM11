package it.polimi.ingsw.am11.network.socket.server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
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

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final @NotNull Socket clientSocket;
    private final @NotNull BufferedReader in;
    private final @NotNull PrintWriter out;
    private @Nullable ServerMessageHandler messageHandler;
    private @Nullable String nickname;
    private boolean isRunning;

    public ClientHandler(@NotNull Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while creating client handler", e);
            // TODO could throw a custom exception
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
            LOGGER.info("SERVER TCP: Server ClientHandler {} closed", nickname);
        }));
    }

    public void stop() {
        LOGGER.debug("SERVER TCP: Stopping client handler");
        isRunning = false;
        if (messageHandler != null) messageHandler.close();
        try {
            if (! clientSocket.isClosed()) clientSocket.close();
            in.close();
        } catch (IOException e) {
            LOGGER.debug("SERVER TCP: Error while closing client handler: {}", e.getMessage());
        }
        out.close();
    }

    @Override
    public void run() {
        isRunning = true;
        ServerExceptionSender serverExceptionSender = new ServerExceptionSender(out);
        if (! readNickname(serverExceptionSender)) return;// FIXME disconnection
        loopMessageRead();
    }

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

            LOGGER.info("SERVER TCP: Received nickname: {}", nickname);

            try {
                ServerGameSender messageSender = new ServerGameSender(out);
                ServerChatSender chatSender = new ServerChatSender(out);
                VirtualPlayerView view = CentralController.INSTANCE
                        .connectPlayer(nickname, messageSender, messageSender, chatSender);
                ServerGameReceiver messageReceiver =
                        new ServerGameReceiver(view, exceptionSender);
                ServerChatReceiver chatReceiver = new ServerChatReceiver(exceptionSender);
                PingHandler pingHandler = new PingHandler(clientSocket, out);

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

    private void loopMessageRead() {
        while (isRunning) {
            String message = readInput();
            if (message == null) return;
            if (messageHandler == null) throw new IllegalStateException("Message handler not set");
            messageHandler.receive(message);
        }
    }

    private @Nullable String readInput() {
        try {
            String message = null;
            if (! clientSocket.isClosed()) message = in.readLine();
            if (message == null) {
                LOGGER.info("SERVER TCP: Client {} disconnected", nickname);
                CentralController.INSTANCE.disconnectPlayer(nickname);
                isRunning = false;
            }
            return message;
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while reading input", e);
            throw new RuntimeException(e);
        }
    }
}