package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReceiveCommand {
    private final ClientViewUpdater clientPlayerView;
    private final ObjectMapper mapper;
    private final ReceiveException receiveException;

    public ReceiveCommand(ClientViewUpdater clientPlayerView) {
        this.clientPlayerView = clientPlayerView;
        this.mapper = new ObjectMapper();
        this.receiveException = new ReceiveException();
    }

    public void receive(String message) {
        try {
            // Parse the message
            JsonNode jsonNode = mapper.readTree(message);
            // Switch on the method
            // TODO final leaderboard receive missing
            // FIXME we may use command pattern
            //  to simplify and make the code more readable
            if (jsonNode.get("method").asText().equals("Method")) {
                switch (jsonNode.get("method").asText()) {
                    case "updateHand":
                        clientPlayerView.updateHand(jsonNode.get("cardId").asInt(),
                                                    jsonNode.get("removeMode").asBoolean());
                        break;
                    case "updatePersonalObjective":
                        clientPlayerView.updatePersonalObjective(jsonNode.get("cardId").asInt(),
                                                                 jsonNode.get(
                                                                         "removeMode").asBoolean());
                        break;
                    case "sendStarterCard":
                        clientPlayerView.receiveStarterCard(jsonNode.get("cardId").asInt());
                        break;
                    case "sendCandidateObjective":
                        Set<Integer> cardsId = new HashSet<>();
                        for (JsonNode cardId : jsonNode.get("cardsId")) {
                            cardsId.add(cardId.asInt());
                        }
                        clientPlayerView.receiveCandidateObjective(cardsId);
                        break;
                    case "updateDeckTop":
                        clientPlayerView.
                                updateDeckTop(
                                        PlayableCardType.valueOf(jsonNode.get("type").asText()),
                                        Color.valueOf(jsonNode.get("color").asText()));
                        break;
                    case "updateField":
                        clientPlayerView.updateField(jsonNode.get("nickname").asText(), jsonNode.
                                                             get("x").asInt(), jsonNode.get("y").
                                                                                       asInt(),
                                                     jsonNode.get("cardId").asInt(), jsonNode.
                                                             get("removeMode").asBoolean());
                        break;
                    case "updateShownPlayable":
                        clientPlayerView.updateShownPlayable(jsonNode.get("previousId").asInt(),
                                                             jsonNode.get("currentId").asInt());
                        break;
                    case "updateTurnChange":
                        clientPlayerView.updateTurnChange(jsonNode.get("nickname").asText());
                        break;
                    case "updatePlayerPoint":
                        clientPlayerView.updatePlayerPoint(jsonNode.get("nickname").asText(),
                                                           jsonNode.get("points").asInt());
                        break;
                    case "updateGameStatus":
                        clientPlayerView.updateGameStatus(
                                GameStatus.valueOf(jsonNode.get("status").asText()));
                        break;
                    case "updateCommonObjective":
                        clientPlayerView.updateCommonObjective(jsonNode.get("cardId").asInt(),
                                                               jsonNode.get(
                                                                       "removeMode").asBoolean());
                        break;
                }
            } else if (jsonNode.get("method").asText().equals("Exception")) {
                receiveException.receive(message);
            }
        } catch (IOException e) {
            System.out.println("Received invalid message.");
        }
    }
}
