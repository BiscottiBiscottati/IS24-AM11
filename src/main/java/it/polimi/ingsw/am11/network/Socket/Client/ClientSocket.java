package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.view.client.ClientPlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private ReceiveCommand receiveCommand;

    public ClientSocket(String ip, int port, String nickname) {
        try {
            this.nickname = nickname;
            Socket socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            SendCommand sendCommand = new SendCommand(out);
            ClientPlayerView clientPlayerView = new ClientPlayerView(nickname);
            receiveCommand = new ReceiveCommand(clientPlayerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        out.println(nickname);
        try {
            String message = in.readLine();
            if (message.equals("You")) {
                //TODO implement listener
                out.println("2");
                startCommunication();
            } else if (message.equals("NotYou")) {
                startCommunication();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCommunication() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = in.readLine();
                    receiveCommand.receive(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}