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

public class ClientHandler implements Runnable {
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private VirtualPlayerView view;
    private ReceiveCommand receiveCommand;

    public ClientHandler(@NotNull Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean validNickname = false;
        SendException sendException = new SendException(out);
        while (! validNickname) {
            try {
                System.out.println("TCP: Waiting for nickname...");
                nickname = in.readLine();
                SendCommand sendCommand = new SendCommand(out);
                view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommand, sendCommand);
                receiveCommand = new ReceiveCommand(view, out);
                validNickname = true;
                System.out.println("TCP: Connected: " + nickname);
                if (CentralController.INSTANCE.getGodPlayer().equals(nickname)) {
                    boolean validNumOfPlayers = false;
                    while (! validNumOfPlayers) {
                        try {
                            sendCommand.youGodPlayer();
                            System.out.println("TCP: Waiting for number of players...");
                            int numOfPlayers = Integer.parseInt(in.readLine());
                            CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                            System.out.println("God player: " + nickname);
                            System.out.println("Num of players: " + numOfPlayers);
                            validNumOfPlayers = true;
                        } catch (NotGodPlayerException | NumOfPlayersException |
                                 GameStatusException e) {
                            sendException.Exception(e);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GameStatusException | PlayerInitException | NumOfPlayersException |
                     NotSetNumOfPlayerException e) {
                sendException.Exception(e);
                System.out.println("TCP: Problem with nickname: " + nickname);
            }
        }
        while (true) {
            try {
                String message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommand.receive(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}