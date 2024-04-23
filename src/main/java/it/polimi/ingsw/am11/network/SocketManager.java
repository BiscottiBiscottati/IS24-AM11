package it.polimi.ingsw.am11.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketManager {
    private final List<String> Users = new ArrayList<String>();
    private ServerSocket serverSocket;
    private boolean isRunning;

    public SocketManager(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server in ascolto sulla porta " + port);
    }

    public void start() {
        while (isRunning) {
            try {
                // Accetta una nuova connessione dal client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione: " + clientSocket);

                // Crea un nuovo thread per gestire la connessione
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String username = null;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        try {
            // Ottieni l'indirizzo IP e il numero di porta del client
            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            int clientPort = clientSocket.getPort();
            System.out.println(
                    "Nuova connessione da: " + username + clientAddress + ":" + clientPort);

            // Gestisci la connessione del client qui
            // Esempio: leggi e scrivi dati tramite input/output stream
            // Esempio: clientSocket.getInputStream() / clientSocket.getOutputStream()

            // Chiudi la connessione quando non è più necessaria
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
