package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectMapper mapper;
    private String nickname;
    private SendCommand sendCommand;

    public ClientSocket(String ip, int port, String nickname) {
        try {
            this.nickname = nickname;
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        out.println(nickname);
        try {
            String message = in.readLine();
            if (message.equals("You")) {
                out.println("2");
            } else {
                System.out.println("Not god player");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = in.readLine();
                    readJSON(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void readJSON(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(message);
        String jsonString = mapper.writeValueAsString(jsonNode);
        System.out.println(jsonString);
    }

    public void send(String message) {
        switch (message) {
            case "setStarterCard":
                sendCommand.setStarterCard(true);
                break;
            default:
                System.out.println("Invalid message");
        }
    }
}