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
            new Thread(this::run).start();

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
            System.out.println("Connection error");
        }
    }


    public void run() {
        String message;
        try {
            while (true) {
                message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommand.receive(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CltToNetConnector getConnector() {
        return sendCommand;
    }

    public PrintWriter getOut() {
        return out;
    }
}