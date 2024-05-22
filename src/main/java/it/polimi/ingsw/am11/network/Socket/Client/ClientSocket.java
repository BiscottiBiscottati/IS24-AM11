package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket implements ClientNetworkHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSocket.class);

    private final ClientViewUpdater clientViewUpdater;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ReceiveCommandC receiveCommandC;
    private final SendCommandC sendCommandC;
    private final Socket socket;
    private final Thread clientThread;
    private boolean isRunning;

    public ClientSocket(String ip, int port,
                        @NotNull ClientViewUpdater clientViewUpdater)
    throws IOException {
        this.clientViewUpdater = clientViewUpdater;
        try {
            //FIXME: If I try to connect to a random ip and port the method Socket() will not
            // terminate and the program will hang;
            socket = new Socket(ip, port);
            if (! socket.isConnected()) {
                throw new IOException("Connection error");
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            sendCommandC = new SendCommandC(out);
            receiveCommandC = new ReceiveCommandC(this.clientViewUpdater);
            clientThread = new Thread(this::run);
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

            clientThread.start();

        } catch (IOException e) {
            LOGGER.debug("CLIENT TCP: connection error", e);
            throw e;
        }
    }

    private void run() {
        isRunning = true;
        String message;
        while (isRunning) {
            try {
                message = in.readLine();
                LOGGER.debug("CLIENT TCP: Client received message: {}", message);
                if (message != null && ! message.isEmpty()) {
                    receiveCommandC.receive(message);
                }
            } catch (IOException e) {
                LOGGER.debug("CLIENT TCP: Error while receiving message because {}",
                             e.getMessage());
                //TODO: handle disconnection
                close();
            }
        }
    }

    public CltToNetConnector getConnector() {
        return sendCommandC;
    }

    @Override
    public void close() {
        LOGGER.debug("CLIENT TCP: Closing client");
        clientThread.interrupt();
        isRunning = false;
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            clientThread.join();
        } catch (IOException e) {
            LOGGER.debug("CLIENT TCP: Error while closing the connection (likely already closed)");
        } catch (InterruptedException e) {
            LOGGER.debug("CLIENT TCP: Error while closing the client thread (likely already " +
                         "closed)");
        }
        LOGGER.debug("CLIENT TCP: Client closed");
    }

    // For testing purposes
    public PrintWriter getOut() {
        return out;
    }
}