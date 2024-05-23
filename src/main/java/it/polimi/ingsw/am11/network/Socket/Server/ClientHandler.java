package it.polimi.ingsw.am11.network.Socket.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
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
        if (clientSocket != null) clientSocket.close();
    }

    @Override
    public void run() {
        boolean validNickname = false;
        isRunning = true;
        SendException sendException = new SendException(out);
        while (! validNickname) {
            try {
                System.out.println("SERVER TCP: Waiting for nickname...");
                nickname = in.readLine();
                LOGGER.info("SERVER TCP: Received nickname: {}", nickname);

                if (nickname == null) return; // FIXME when client disconnects

                SendCommandS sendCommandS = new SendCommandS(out);
                VirtualPlayerView view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommandS, sendCommandS);
                receiveCommandS = new ReceiveCommandS(view, out);
                validNickname = true;
                LOGGER.info("SERVER TCP: Player connected: {}", nickname);
                if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
                    boolean validNumOfPlayers = false;
                    LOGGER.debug("SERVER TCP: Notifying god player: {}", nickname);
                    sendCommandS.youGodPlayer();
                    while (! validNumOfPlayers) {
                        try {
                            LOGGER.info("SERVER TCP: waiting for number of players...");
                            String input = in.readLine();
                            LOGGER.debug("SERVER TCP: Received message from god player: {}", input);
                            if (input == null) return;
                            if (! input.isBlank()) {
                                int numOfPlayers = Integer.parseInt(input);
                                CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                                LOGGER.info("SERVER TCP: Number of players set to {} by {}",
                                            numOfPlayers, nickname);
                                validNumOfPlayers = true;
                                //FIXME: it has to send it to all currently connected players
                                sendCommandS.updateNumOfPlayers(numOfPlayers);
                            }
                        } catch (NotGodPlayerException | NumOfPlayersException |
                                 GameStatusException e) {
                            LOGGER.error("SERVER TCP: Error while setting number of players", e);
                            sendException.Exception(e);
                        }
                    }
                }
            } catch (GameStatusException | PlayerInitException | NumOfPlayersException |
                     NotSetNumOfPlayerException e) {
                LOGGER.error("SERVER TCP: Error while connecting player", e);
                sendException.Exception(e);
            } catch (SocketException e) {
                LOGGER.error("SERVER TCP: Error the socket might be closed", e);
            } catch (IOException e) {
                LOGGER.error("TCP: Error while reading nickname", e);
                throw new RuntimeException(e);
            }

        }
        while (isRunning) {
            try {
                String message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommandS.receive(message);
                }
            } catch (IOException e) {
                LOGGER.info("SERVER TCP: Player disconnected: {}", nickname);
                try {
                    stop();
                } catch (IOException ex) {
                    LOGGER.error("SERVER TCP: Error while closing connection", ex);
                }
                CentralController.INSTANCE.playerDisconnected(nickname);
                isRunning = false;
            }
        }
    }
}