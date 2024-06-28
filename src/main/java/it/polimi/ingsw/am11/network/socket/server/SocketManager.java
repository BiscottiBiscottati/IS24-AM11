package it.polimi.ingsw.am11.network.socket.server;

import it.polimi.ingsw.am11.network.exceptions.SocketCreationException;
import jdk.net.ExtendedSocketOptions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The SocketManager class is responsible for managing the socket connection of the server, a new
 * ClientHandler is created for each client connection and executed in a separate thread
 */
public class SocketManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);

    private final @NotNull List<ClientHandler> clientHandlers;
    private final @NotNull ServerSocket serverSocket;
    private final @NotNull ExecutorService threadPool;
    private boolean isRunning;

    public SocketManager(int port) {
        clientHandlers = new ArrayList<>(4);
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(5);
            isRunning = true;
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while creating server", e);
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stop();
            LOGGER.info("SERVER TCP SHUTDOWN HOOK: Server closed");
        }));
    }

    /**
     * Stops the server socket and the client handlers, it waits for the termination of the thread.
     */
    public void stop() {
        isRunning = false;
        try {
            removeClients();
            threadPool.shutdown(); // Stop accepting new tasks
            if (! threadPool.awaitTermination(10, TimeUnit.SECONDS)) throw new RuntimeException();
            if (! serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("SERVER TCP: Error while closing server because of {}", e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("SERVER TCP: Error while waiting for termination because of {}",
                         e.getMessage());
        }
    }

    /**
     * It notifies the client of the disconnection and stops the client handler
     */
    public void removeClients() {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.youUgly();
            clientHandler.stop();
        }
    }

    /**
     * This method while the server is running, waits for a new connection and creates a new client
     * handler for each client.
     */
    public void start() {
        LOGGER.info("SERVER TCP: Server started on port: {}", serverSocket.getLocalPort());
        LOGGER.info("SERVER TCP: Server address: {}", serverSocket.getInetAddress());
        while (isRunning) {
            try {
                LOGGER.info("SERVER TCP: Waiting for connection...");
                // Accept a new connection from a client
                Socket clientSocket = serverSocket.accept();
                clientSocket.setKeepAlive(true);
                clientSocket.setSoTimeout(0);
                clientSocket.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, 5);
                clientSocket.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, 2);
                clientSocket.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, 1);
                LOGGER.info("SERVER TCP: Connection accepted from: {}",
                            clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                // Execute the client handler in a separate thread
                threadPool.execute(clientHandler);
            } catch (SocketCreationException e) {
                LOGGER.error("SERVER TCP: Error while creating client handler: {}", e.getMessage());
            } catch (IOException e) {
                LOGGER.warn("SERVER TCP: Error while accepting: {}", e.getMessage());
            }
        }
    }
}