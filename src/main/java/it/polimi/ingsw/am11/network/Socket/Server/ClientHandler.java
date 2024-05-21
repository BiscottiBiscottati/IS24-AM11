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

    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private VirtualPlayerView view;
    private ReceiveCommandS receiveCommandS;
    private boolean isRunning;
    private Socket clientSocket;

    public ClientHandler(@NotNull Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stop();
                LOGGER.info("TCP: Server ClientHandler {} closed", nickname);
            } catch (IOException e) {
                e.printStackTrace();
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
                System.out.println("TCP: Waiting for nickname...");
                nickname = in.readLine();
                LOGGER.info("TCP: Received nickname: {}", nickname);

                SendCommandS sendCommandS = new SendCommandS(out);
                view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommandS, sendCommandS);
                receiveCommandS = new ReceiveCommandS(view, out);
                validNickname = true;
                System.out.println("TCP: Connected: " + nickname);
                if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
                    boolean validNumOfPlayers = false;
                    LOGGER.debug("Notifying god player: {}", nickname);
                    sendCommandS.youGodPlayer();
                    while (! validNumOfPlayers) {
                        try {
                            System.out.println("TCP: Waiting for number of players...");
                            String input = in.readLine();
                            LOGGER.debug("Received message from god player: {}", input);
                            if (input == null) return;
                            if (! input.isBlank()) {
                                int numOfPlayers = Integer.parseInt(input);
                                CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                                System.out.println("God player: " + nickname);
                                System.out.println("Num of players: " + numOfPlayers);
                                validNumOfPlayers = true;
                                sendCommandS.updateNumOfPlayers(numOfPlayers);
                            }
                        } catch (NotGodPlayerException | NumOfPlayersException |
                                 GameStatusException e) {
                            sendException.Exception(e);
                        }
                    }
                }
            } catch (GameStatusException | PlayerInitException | NumOfPlayersException |
                     NotSetNumOfPlayerException e) {
                sendException.Exception(e);
                System.out.println("TCP: Problem with nickname: " + nickname);
            } catch (SocketException e) {
                System.out.println("TCP: Connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (isRunning) {
            try {
                String message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommandS.receive(message);
                }
            } catch (IOException e) {
                System.out.println("TCP: Connection closed");
                try {
                    stop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                CentralController.INSTANCE.playerDisconnected(nickname);
            }
        }
    }
}