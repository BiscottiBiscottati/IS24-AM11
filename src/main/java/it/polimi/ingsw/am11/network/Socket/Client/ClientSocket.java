package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private final ClientViewUpdater clientViewUpdater;
    private final String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private ReceiveCommand receiveCommand;
    private SendCommand sendCommand;

    public ClientSocket(String ip, int port, String nickname,
                        @NotNull ClientViewUpdater clientPlayerView) {
        this.nickname = nickname;
        this.clientViewUpdater = clientPlayerView;
        try {
            Socket socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            sendCommand = new SendCommand(out);
            receiveCommand = new ReceiveCommand(this.clientViewUpdater);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        out.println(nickname);
        // FIXME missing logic if nickname is not valid
        try {
            String message = in.readLine();
            if (message.equals("You")) {
                //TODO implement listener
                sendCommand.setNumOfPlayers(3);
                startCommunication();
            } else if (message.equals("NotYou")) {
                out.println("not me");
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