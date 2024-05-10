package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private final ClientViewUpdater clientViewUpdater;
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private ReceiveCommand receiveCommand;
    private SendCommand sendCommand;

    public ClientSocket(String ip, int port, String nickname,
                        @NotNull ClientViewUpdater clientViewUpdater) {
        this.nickname = nickname;
        this.clientViewUpdater = clientViewUpdater;
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

    public void connect() throws IOException {
        out.println(nickname);
        String response = in.readLine();
        while (true) {
            boolean validNickname = response.equals("VALID_NICKNAME");
            if (validNickname) {
                break;
            } else if (response.equals("NOT_VALID_NICKNAME")) {
                System.out.println("Nickname already taken, please choose another one");
                //TODO: implement the nickname selection
                out.println(nickname);
                response = in.readLine();
            } else {
                // FIXME not to throw
                throw new IOException("Invalid nickname response");
            }
        }
        if (in.readLine().equals("YOU_GOD_PLAYER")) {
            //TODO: implement the number of players selection
            sendCommand.setNumOfPlayers(3);
            startCommunication();
        } else if (in.readLine().equals("YOU_NOT_GOD_PLAYER")) {
            startCommunication();
        } else {
            // FIXME not to throw
            throw new IOException("Invalid god player");
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

    public CltToNetConnector getConnector() {
        return sendCommand;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}