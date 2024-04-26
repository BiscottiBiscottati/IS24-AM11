package it.polimi.ingsw.am11.network.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketManager {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean isRunning;

    public SocketManager(int port) {
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(10); // Pool with 10 threads
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (isRunning) {
            try {
                System.out.println("Server open on port: " + serverSocket.getLocalPort());
                //System.out.println("Server address: " + serverSocket.getInetAddress());
                System.out.println("Waiting fo connections...");
                // Accept a new connection from a client
                Socket clientSocket = serverSocket.accept();
                //System.out.println("Nuova connessione: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                // Execute the client handler in a separate thread
                threadPool.execute(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
            threadPool.shutdown(); // Stop accepting new tasks
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server stopped");
    }
}