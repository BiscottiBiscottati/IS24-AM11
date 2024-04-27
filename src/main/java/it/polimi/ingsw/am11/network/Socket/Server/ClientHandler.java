package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.view.VirtualPlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectMapper mapper;
    private VirtualPlayerView view;
    private ReceiveCommand receiveCommand;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
        while (!validNickname) {
            try {
                nickname = in.readLine();
                SendCommand sendCommand = new SendCommand(out);
                view = CentralController.INSTANCE
                        .connectPlayer(nickname, sendCommand, sendCommand);
                receiveCommand = new ReceiveCommand(view, in);
                validNickname = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PlayerInitException e) {
                out.println("Invalid nickname. Please try again.");
            } catch (GameStatusException e) {

                throw new RuntimeException(e);
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