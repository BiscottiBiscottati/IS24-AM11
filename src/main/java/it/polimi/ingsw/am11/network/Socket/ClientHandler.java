package it.polimi.ingsw.am11.network.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
            System.out.println("Client running: " + clientSocket);
            String input = in.readLine();
            // the server send back the input string in uppercase
            String output = input.toUpperCase();
            out.println(output);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}