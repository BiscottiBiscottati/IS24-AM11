package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket implements ClientNetworkHandler {
    private final ClientViewUpdater clientViewUpdater;
    private BufferedReader in;
    private PrintWriter out;
    private ReceiveCommand receiveCommand;
    private SendCommand sendCommand;

    public ClientSocket(String ip, int port,
                        @NotNull ClientViewUpdater clientViewUpdater) {
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

    @Override
    public void connect(String nickname) throws IOException {
        out.println(nickname);
        String response = in.readLine();
        if (response.equals("MAX_NUM_OF_PLAYERS_REACHED")) {
            // TODO: implement the nickname retry logic
        } else if (response.equals("NICKNAME_ALREADY_TAKEN")) {
            //TODO: implement the nickname retry logic
        } else if (response.equals("VALID_NICKNAME")) {
            //TODO: implement the logic for the case where the nickname is valid
            response = in.readLine();
            if (response.equals("YOU_GOD_PLAYER")) {
                //TODO: implement the number of players setting logic
            } else if (response.equals("YOU_NOT_GOD_PLAYER")) {
                startCommunication();
            }
        }
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) throws IOException {
        out.println(numOfPlayers);
        String response = in.readLine();
        if (response.equals("NUM_OF_PLAYERS_SET")) {
            startCommunication();
        } else {
            //TODO: implement the logic for the case where the number of players is not set
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

}