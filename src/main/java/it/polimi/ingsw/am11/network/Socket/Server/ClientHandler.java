package it.polimi.ingsw.am11.network.Socket.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

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

    public ClientHandler(Socket clientSocket) {
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
        while (! validNickname) {
            try {
                nickname = in.readLine();
                SendCommand sendCommand = new SendCommand(out);
                view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommand, sendCommand);
                receiveCommand = new ReceiveCommand(view, out);
                out.println("VALID_NICKNAME");
                validNickname = true;
                if (CentralController.INSTANCE.getGodPlayer().equals(nickname)) {
                    try {
                        out.println("YOU_GOD_PLAYER");
                        int numOfPlayers = Integer.parseInt(in.readLine());
                        CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
                        System.out.println("God player: " + nickname);
                        System.out.println("Num of players: " + numOfPlayers);
                        out.println("NUM_OF_PLAYERS_SET");
                    } catch (NotGodPlayerException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    out.println("YOU_NOT_GOD_PLAYER");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PlayerInitException e) {
                out.println("NICKNAME_ALREADY_TAKEN");
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            } catch (NumOfPlayersException e) {
                out.println("MAX_NUM_OF_PLAYERS_REACHED");
            }
        }
        System.out.println("Connected: " + nickname);
        while (true) {
            try {
                String message = in.readLine();
                receiveCommand.receive(message);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}