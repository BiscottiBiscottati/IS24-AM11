package it.polimi.ingsw.am11.network.Socket.Client;

import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import jdk.net.ExtendedSocketOptions;
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

    private final @NotNull ClientViewUpdater clientViewUpdater;
    private final @NotNull BufferedReader in;
    private final @NotNull PrintWriter out;
    private final @NotNull ClientMessageReceiver clientMessageReceiver;
    private final @NotNull ClientMessageSender clientMessageSender;
    private final @NotNull Socket socket;
    private final @NotNull ExecutorService clientExecutor;
    private boolean isRunning;

    public ClientSocket(@NotNull String ip, int port,
                        @NotNull ClientViewUpdater clientViewUpdater)
    throws IOException {
        this.clientViewUpdater = clientViewUpdater;
        try {
            //FIXME: If I try to connect to a random ip and port the method Socket() will not
            // terminate and the program will hang;
            socket = new Socket(ip, port);
            socket.setKeepAlive(true);
            socket.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, 5);
            socket.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, 2);
            socket.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, 1);
            socket.setSoTimeout(0);
            if (! socket.isConnected()) {
                throw new IOException("Connection error");
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clientMessageSender = new ClientMessageSender(out);
            clientMessageReceiver = new ClientMessageReceiver(this.clientViewUpdater);
            clientExecutor = Executors.newFixedThreadPool(1);
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

            clientExecutor.submit(this::run);

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
            } catch (IOException e) {
                LOGGER.debug("CLIENT TCP: Error while receiving message because {}",
                             e.getMessage());
                //TODO: to test
                clientViewUpdater.disconnectedFromServer();
                close();
                return;
            }
            LOGGER.debug("CLIENT TCP: Client received message: {}", message);
            if (message == null) {
                LOGGER.debug("CLIENT TCP: Connection closed by the server");
                // TODO to test
                clientViewUpdater.disconnectedFromServer();
                close();
                return;
            } else if (! message.isBlank()) {
                clientMessageReceiver.receive(message);
            }
        }
    }

    public @NotNull ClientGameConnector getGameUpdatesInterface() {
        return clientMessageSender;
    }

    @Override
    public void close() {
        LOGGER.debug("CLIENT TCP: Closing client");
        clientExecutor.shutdown();
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