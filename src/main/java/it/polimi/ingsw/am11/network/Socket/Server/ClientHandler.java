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
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String nickname;
    private ReceiveCommandS receiveCommandS;
    private boolean isRunning;

    public ClientHandler(@NotNull Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while creating client handler", e);
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
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null && ! clientSocket.isClosed()) clientSocket.close();
    }

    @Override
    public void run() {
        isRunning = true;
        SendException sendException = new SendException(out);
        if (! readNickname(sendException)) return;// FIXME disconnection
        if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
            if (! readNumOfPlayers(sendException)) return; //FIXME disconnection
        }
        readJSONs();

    }

    private boolean readNickname(SendException sendException) {
        boolean validNickname = false;
        while (! validNickname) {
            try {
                System.out.println("SERVER TCP: Waiting for nickname...");
                nickname = in.readLine();
                LOGGER.info("SERVER TCP: Received nickname: {}", nickname);

                if (nickname == null) return false;

                SendCommandS sendCommandS = new SendCommandS(out);
                VirtualPlayerView view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommandS, sendCommandS);
                receiveCommandS = new ReceiveCommandS(view, out);
                validNickname = true;
                LOGGER.info("SERVER TCP: Player connected with name: {}", nickname);
            } catch (GameStatusException | PlayerInitException | NumOfPlayersException |
                     NotSetNumOfPlayerException e) {
                LOGGER.error("SERVER TCP: Error while connecting player", e);
                sendException.Exception(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private boolean readNumOfPlayers(SendException sendException) {
        boolean validNumOfPlayers = false;
        while (! validNumOfPlayers) {
            try {
                LOGGER.info("SERVER TCP: waiting for number of players...");
                String message = readInput();
                LOGGER.debug("SERVER TCP: Received message from god player: {}", message);
                if (message == null) return false;
                if (! message.isBlank()) {
                    int numOfPlayers = Integer.parseInt(message);
                    CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                    LOGGER.info("SERVER TCP: Number of players set to {} by {}",
                                numOfPlayers, nickname);
                    validNumOfPlayers = true;
                }
            } catch (NotGodPlayerException | NumOfPlayersException | GameStatusException e) {
                LOGGER.error("SERVER TCP: Error while setting number of players", e);
                sendException.Exception(e);
            }
        }
        return true;
    }

    private void readJSONs() {
        while (isRunning) {
            String message = readInput();
            if (message == null) return;
            if (! message.isEmpty()) {
                receiveCommandS.receive(message);
            }
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