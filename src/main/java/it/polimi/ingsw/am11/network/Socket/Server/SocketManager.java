package it.polimi.ingsw.am11.network.Socket.Server;

import jdk.net.ExtendedSocketOptions;
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

public class SocketManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketManager.class);

    private final List<ClientHandler> clientHandlers;
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private boolean isRunning;

    public SocketManager(int port) {
        clientHandlers = new ArrayList<>(4);
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(4); // Pool with 10 threads
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

    public void stop() {
        isRunning = false;
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.stop();
            }
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

    public void start() {
        LOGGER.info("SERVER TCP: Server started on port: {}", serverSocket.getLocalPort());
        LOGGER.info("SERVER TCP: Server address: {}", serverSocket.getInetAddress());
        while (isRunning) {
            try {
                LOGGER.info("SERVER TCP: Waiting for connection...");
                // Accept a new connection from a client
                Socket clientSocket = serverSocket.accept();
                clientSocket.setKeepAlive(true);
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
                isRunning = false;
            }
        }
    }
}