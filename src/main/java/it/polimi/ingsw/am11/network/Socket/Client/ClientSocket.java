package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSocket implements ClientNetworkHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSocket.class);

    private final ClientViewUpdater clientViewUpdater;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ReceiveCommandC receiveCommandC;
    private final SendCommandC sendCommandC;
    private final Socket socket;
    private final ExecutorService clientThread;
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
            clientThread = Executors.newFixedThreadPool(1);
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

            clientThread.submit(this::run);

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
                if (message == null) {
                    LOGGER.debug("CLIENT TCP: Connection closed by the server");
                    // TODO handle disconnection
                    close();
                } else if (! message.isBlank()) {
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

    public ClientGameConnector getConnector() {
        return sendCommandC;
    }

    @Override
    public void close() {
        LOGGER.debug("CLIENT TCP: Closing client");
        clientThread.shutdown();
        isRunning = false;
        try {
            if (socket != null && ! socket.isClosed()) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            LOGGER.debug("CLIENT TCP: Error while closing the connection (likely already closed)");
        }
        LOGGER.debug("CLIENT TCP: Client closed");
    }

    // For testing purposes
    public PrintWriter getOut() {
        return out;
    }
}