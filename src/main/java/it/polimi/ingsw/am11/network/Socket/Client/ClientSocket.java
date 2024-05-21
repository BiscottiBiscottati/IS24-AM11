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
    private boolean isRunning;

    public ClientSocket(String ip, int port,
                        @NotNull ClientViewUpdater clientViewUpdater)
    throws IOException {
        this.clientViewUpdater = clientViewUpdater;
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            sendCommandC = new SendCommandC(out);
            receiveCommandC = new ReceiveCommandC(this.clientViewUpdater);
            Thread thread = new Thread(this::run);
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
            throw new UnknownHostException("Unknown host");
        } catch (IOException e) {
            throw new IOException("Connection error");
        }
    }


    public void run() {
        isRunning = true;
        String message;
        while (isRunning) {
            try {
                message = in.readLine();
                if (message != null && ! message.isEmpty()) {
                    receiveCommandC.receive(message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed");
                ReceiveException receiveException = receiveCommandC.getReceiveException();
                receiveException.sendDisconnectionException();
                //TODO: handle disconnection
                try {
                    close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void close() throws IOException {
        isRunning = false;
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }

    public CltToNetConnector getConnector() {
        return sendCommandC;
    }

    // For testing purposes
    public PrintWriter getOut() {
        return out;
    }
}