package it.polimi.ingsw.am11.network.Socket.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketManager {
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean isRunning;

    public SocketManager(int port) {
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(4); // Pool with 10 threads
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        isRunning = false;
        try {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.stop();
            }
            threadPool.shutdown(); // Stop accepting new tasks
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (isRunning) {
            try {
                System.out.println("TCP: Server open on port: " + serverSocket.getLocalPort());
                //System.out.println("Server address: " + serverSocket.getInetAddress());
                System.out.println("TCP: Waiting fo connections...");
                // Accept a new connection from a client
                Socket clientSocket = serverSocket.accept();
                System.out.println("TCP: Nuova connessione: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                // Execute the client handler in a separate thread
                threadPool.execute(clientHandler);
            } catch (IOException e) {
                System.out.println("TCP: Socket Manager closed");
            }
        }
    }
}