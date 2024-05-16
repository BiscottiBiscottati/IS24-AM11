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
import java.net.UnknownHostException;

public class ClientSocket implements ClientNetworkHandler {
    private final ClientViewUpdater clientViewUpdater;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ReceiveCommandC receiveCommandC;
    private final SendCommandC sendCommandC;
    private final Socket socket;
    private final Thread thread;
    private boolean isRunning;

    public ClientSocket(String ip, int port,
                        @NotNull ClientViewUpdater clientViewUpdater)
    throws IOException {
        this.clientViewUpdater = clientViewUpdater;
        try {
            isRunning = true;
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            sendCommandC = new SendCommandC(out);
            receiveCommandC = new ReceiveCommandC(this.clientViewUpdater);
            thread = new Thread(this::run);
            thread.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
            throw new UnknownHostException("Unknown host");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection error");
            throw new IOException("Connection error");
        }
    }


    public void run() {
        String message;
        try {
            while (isRunning) {
                message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommandC.receive(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CltToNetConnector getConnector() {
        return sendCommandC;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void close() {
        isRunning = false;
        try {
            in.close();
            out.close();
            socket.close();
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}