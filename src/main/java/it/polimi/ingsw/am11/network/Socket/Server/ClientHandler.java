package it.polimi.ingsw.am11.network.Socket.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public class ClientHandler implements Runnable {
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
                SendCommandS sendCommandS = new SendCommandS(out);
                view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommandS, sendCommandS);
                receiveCommandS = new ReceiveCommandS(view, out);
                validNickname = true;
                System.out.println("TCP: Connected: " + nickname);
                if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
                    boolean validNumOfPlayers = false;
                    while (! validNumOfPlayers) {
                        try {
                            sendCommandS.youGodPlayer();
                            System.out.println("TCP: Waiting for number of players...");
                            int numOfPlayers = Integer.parseInt(in.readLine());
                            CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                            System.out.println("God player: " + nickname);
                            System.out.println("Num of players: " + numOfPlayers);
                            validNumOfPlayers = true;
                            sendCommandS.updateNumOfPlayers(numOfPlayers);
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
                //e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        isRunning = false;
        in.close();
        out.close();
        clientSocket.close();
    }
}