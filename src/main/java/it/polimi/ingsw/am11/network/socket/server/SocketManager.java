package it.polimi.ingsw.am11.network.socket.server;

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
 * The class that manages the server socket
 * <p>
 *     The class that manages the server socket and the client connections
 *     <br>
 *     It uses a {@link ServerSocket} to handle the server socket
 *     <br>
 *     It uses a {@link List} of {@link ClientHandler} to handle the client connections
 *     <br>
 *     It uses a {@link ExecutorService} to handle the client connections
 *     <br>
 *     It uses a {@link Logger} to log the messages
 * </p>
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
     * Stops the socket server
     * <p>
     *     It uses the {@link #removeClients()} method to remove the clients
     *     <br>
     *     It uses the {@link ExecutorService#shutdown()} method to stop accepting new tasks
     *     <br>
     *     It uses the {@link ExecutorService#awaitTermination(long, TimeUnit)} method to wait for termination
     *     <br>
     *     It uses the {@link ServerSocket#close()} method to close the server socket
     * </p>
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
     * Removes the clients from the server socket
     */
    public void removeClients() {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.youUgly();
            clientHandler.stop();
        }
    }

    /**
     * Starts the server socket
     * <p>
     *     It uses the {@link ServerSocket#accept()} method to accept a new connection from a client
     *     <br>
     *     It uses the {@link Socket#setKeepAlive(boolean)} method to keep the connection alive
     *     <br>
     *     It uses the {@link Socket#setSoTimeout(int)} method to set the timeout for the connection
     *     <br>
     *     It uses the {@link ClientHandler} to handle the client connection
     *     <br>
     *     It uses the {@link ExecutorService#execute(Runnable)} method to execute the client
     *     handler in a separate thread
     *     <br>
     *     It uses the {@link Logger} to log the messages
     *     <br>
     *     It uses the {@link #isRunning} to check if the server is running
     * </p>
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
            } catch (IOException e) {
                LOGGER.warn("SERVER TCP: Error while accepting: {}", e.getMessage());
            }
        }
    }
}