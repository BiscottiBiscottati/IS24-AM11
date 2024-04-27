package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.view.VirtualPlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectMapper mapper;

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
            nickname = in.readLine();
            System.out.println("Connected: " + nickname);
            SendCommand sendCommand = new SendCommand(nickname, in, out);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}