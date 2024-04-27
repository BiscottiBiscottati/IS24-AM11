package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.view.VirtualPlayerView;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceiveCommand {

    private final VirtualPlayerView playerView;
    private final BufferedReader in;
    private ObjectMapper mapper;

    public ReceiveCommand(VirtualPlayerView playerView, BufferedReader in) {
        this.playerView = playerView;
        this.in = in;
        this.mapper = new ObjectMapper();
    }

    public void receive(String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);
            jsonNode.get("cardId").asInt();
            // Now you can use jsonNode to access the data in the JSON object

        } catch (IOException e) {
            System.out.println("Received invalid message.");
        }
    }

}
