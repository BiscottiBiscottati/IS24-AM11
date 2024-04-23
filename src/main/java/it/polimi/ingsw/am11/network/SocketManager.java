package it.polimi.ingsw.am11.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            threadPool = Executors.newFixedThreadPool(10); // Pool di thread con 10 thread
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (isRunning) {
            try {
                System.out.println("Server in ascolto sulla porta " + serverSocket.getLocalPort());
                System.out.println("Indirizzo: " + serverSocket.getInetAddress());
                System.out.println("In attesa di connessioni...");
                // Accetta una nuova connessione dal client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione: " + clientSocket);

                // Assegna un nuovo thread per gestire la connessione
                threadPool.execute(new ClientHandler(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
            threadPool.shutdown(); // Ferma il pool di thread
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server chiuso");
    }

    class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                System.out.println("Client connesso: " + clientSocket);
                String input = in.readLine();
                String output = processInput(input);
                out.println(output);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processInput(String input) {
            return input.toUpperCase();
        }
    }
}