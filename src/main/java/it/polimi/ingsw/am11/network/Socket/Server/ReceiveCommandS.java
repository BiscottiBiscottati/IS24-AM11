package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.io.IOException;
import java.io.PrintWriter;

public class ReceiveCommandS {

    private final VirtualPlayerView playerView;
    private final ObjectMapper mapper;
    private final SendException sendException;

    public ReceiveCommandS(VirtualPlayerView playerView, PrintWriter out) {
        this.playerView = playerView;
        this.mapper = new ObjectMapper();
        this.sendException = new SendException(out);
    }

    public void receive(String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);
            switch (jsonNode.get("method").asText()) {
                case "setStarterCard":
                    playerView.setStarterCard(jsonNode.get("isRetro").asBoolean());
                    break;
                case "setPersonalObjective":
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
        } catch (IllegalPlayerSpaceActionException | TurnsOrderException | PlayerInitException |
                 IllegalCardPlacingException | IllegalPickActionException | NotInHandException |
                 EmptyDeckException | IllegalPlateauActionException | MaxHandSizeException |
                 GameStatusException e) {
            sendException.Exception(e);
        }
    }

}
