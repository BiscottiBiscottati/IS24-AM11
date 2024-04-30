package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.io.IOException;

public class ReceiveCommand {

    private final VirtualPlayerView playerView;
    private final ObjectMapper mapper;

    public ReceiveCommand(VirtualPlayerView playerView) {
        this.playerView = playerView;
        this.mapper = new ObjectMapper();
    }

    public void receive(String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);
            switch (jsonNode.get("method").asText()) {
                // FIXME add exception handling for communicating errors to the client
                case "setStarterCard":
                    playerView.setStarterCard(jsonNode.get("isRetro").asBoolean());
                    break;
                case "setObjectiveCard":
                    playerView.setObjectiveCard(jsonNode.get("cardId").asInt());
                    break;
                case "placeCard":
                    playerView.placeCard(jsonNode.get("cardId").asInt(), jsonNode.get("x").asInt(),
                                         jsonNode.get("y").asInt(),
                                         jsonNode.get("isRetro").asBoolean());
                    break;
                case "drawCard":
                    playerView.drawCard(jsonNode.get("fromVisible").asBoolean(),
                                        PlayableCardType.valueOf(jsonNode.get("type").asText()),
                                        jsonNode.get("cardId").asInt());
                    break;
            }

        } catch (IOException e) {
            System.out.println("Received invalid message.");
        }
    }

}
