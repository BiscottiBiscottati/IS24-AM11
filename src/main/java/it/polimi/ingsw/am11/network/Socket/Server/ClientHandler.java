package it.polimi.ingsw.am11.network.Socket.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
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
import java.util.Objects;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private final @NotNull Socket clientSocket;
    private final @NotNull BufferedReader in;
    private final @NotNull PrintWriter out;
    private String nickname;
    private ServerMessageReceiver serverMessageReceiver;
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
            try {
                stop();
                LOGGER.info("SERVER TCP: Server ClientHandler {} closed", nickname);
            } catch (IOException e) {
                LOGGER.error("SERVER TCP: Error while closing client handler", e);
            }
        }));
    }

    public void stop() throws IOException {
        isRunning = false;
        in.close();
        out.close();
        if (! clientSocket.isClosed()) clientSocket.close();
    }

    @Override
    public void run() {
        isRunning = true;
        ServerExceptionSender serverExceptionSender = new ServerExceptionSender(out);
        if (! readNickname(serverExceptionSender)) return;// FIXME disconnection
        if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
            if (! readNumOfPlayers(serverExceptionSender)) return; //FIXME disconnection
        }
        readJSONs();

    }

    private boolean readNickname(@NotNull ServerExceptionSender exceptionSender) {
        boolean validNickname = false;
        while (! validNickname) {
            LOGGER.info("SERVER TCP: Waiting for nickname");
            try {
                nickname = in.readLine();
            } catch (IOException e) {
                LOGGER.error("SERVER TCP: Error while reading nickname", e);
                return false;
            }
            LOGGER.info("SERVER TCP: Received nickname: {}", nickname);

            if (nickname == null) return false;

            try {
                ServerMessageSender serverMessageSender = new ServerMessageSender(out);
                VirtualPlayerView view = CentralController.INSTANCE
                        .connectPlayer(nickname, serverMessageSender, serverMessageSender);
                serverMessageReceiver = new ServerMessageReceiver(view, exceptionSender);
                validNickname = true;
                LOGGER.info("SERVER TCP: Player connected with name: {}", nickname);
            } catch (GameStatusException | NumOfPlayersException |
                     NotSetNumOfPlayerException e) {
                LOGGER.error("SERVER TCP: Error while connecting player: {}", e.getMessage());
                exceptionSender.exception(e);
                return false;
            } catch (PlayerInitException e) {
                LOGGER.error("SERVER TCP: Nickname in use!");
            }
        }
        return true;
    }

    private boolean readNumOfPlayers(@NotNull ServerExceptionSender exceptionSender) {
        boolean validNumOfPlayers = false;
        while (! validNumOfPlayers) {
            LOGGER.info("SERVER TCP: waiting for number of players...");
            String message = readInput();
            if (message == null) return false;
            LOGGER.debug("SERVER TCP: Received message from god player: {}", message);
            try {
                if (! message.isBlank()) {
                    int numOfPlayers = Integer.parseInt(message);
                    CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                    LOGGER.info("SERVER TCP: Number of players set to {} by {}",
                                numOfPlayers, nickname);
                    validNumOfPlayers = true;
                }
            } catch (NotGodPlayerException | GameStatusException e) {
                LOGGER.error("SERVER TCP: Error while setting number of players: {}",
                             e.getMessage());
                exceptionSender.exception(e);
                return false;
            } catch (NumOfPlayersException e) {
                LOGGER.error("SERVER TCP: Invalid number of players: {}", e.getMessage());
                exceptionSender.exception(e);
            } catch (NumberFormatException e) {
                LOGGER.error("SERVER TCP: Invalid string: {}", e.getMessage());
                exceptionSender.exception(
                        new NumOfPlayersException("Invalid input, please insert a number"));
            }
        }
        return true;
    }

    private void readJSONs() {
        while (isRunning) {
            String message = readInput();
            if (message == null) return;
            if (! message.isEmpty()) {
                serverMessageReceiver.receive(message);
            }
        }
    }

    private @Nullable String readInput() {
        try {
            LOGGER.debug("SERVER TCP: Waiting for input...");
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