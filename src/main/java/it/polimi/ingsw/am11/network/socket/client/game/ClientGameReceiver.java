package it.polimi.ingsw.am11.network.socket.client.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public class ClientGameReceiver implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientGameReceiver.class);

    private final @NotNull ClientViewUpdater clientPlayerView;
    private final @NotNull ClientExceptionReceiver clientExceptionReceiver;

    public ClientGameReceiver(@NotNull ClientViewUpdater clientPlayerView) {
        this.clientPlayerView = clientPlayerView;
        this.clientExceptionReceiver = new ClientExceptionReceiver(
                clientPlayerView.getExceptionThrower());
    }

    public void receive(@NotNull JsonNode jsonNode) {
        LOGGER.debug("CLIENT TCP: Received message: {}", jsonNode);
        try {
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
                    String cardsIdJson = jsonNode.get("cardsId").asText();
                    Set<Integer> cardsId = JsonFactory.readValue(cardsIdJson,
                                                                 new TypeReference<>() {});
                    clientPlayerView.receiveCandidateObjective(cardsId);
                    break;
                case "updateDeckTop":
                    clientPlayerView.
                            updateDeckTop(
                                    PlayableCardType.valueOf(jsonNode.get("type").asText()),
                                    Color.valueOf(jsonNode.get("color").asText()));
                    break;
                case "updateField":
                    clientPlayerView.updateField(jsonNode.get("nickname").asText(),
                                                 jsonNode.get("x").asInt(),
                                                 jsonNode.get("y").asInt(),
                                                 jsonNode.get("cardId").asInt(),
                                                 jsonNode.get("isRetro").asBoolean(),
                                                 jsonNode.get("removeMode").asBoolean());
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
                    String commonObjectiveJson = jsonNode.get("cardId").asText();
                    Set<Integer> commonObjective = JsonFactory.readValue(commonObjectiveJson,
                                                                         new TypeReference<>() {});
                    clientPlayerView.updateCommonObjective(commonObjective,
                                                           jsonNode.get("removeMode").asBoolean());
                    break;
                case "receiveFinalLeaderboard":
                    String finalLeaderboardJson = jsonNode.get("finalLeaderboard").asText();
                    Map<String, Integer> finalLeaderboard = JsonFactory.readValue(
                            finalLeaderboardJson,
                            new TypeReference<>() {});
                    clientPlayerView.receiveFinalLeaderboard(finalLeaderboard);
                    break;
                case "updatePlayers":
                    String currentPlayersJson = jsonNode.get("currentPlayers").asText();
                    SequencedMap<PlayerColor, String> currentPlayers = JsonFactory.readValue(
                            currentPlayersJson,
                            new TypeReference<>() {});
                    clientPlayerView.updatePlayers(currentPlayers);
                    break;
                case "updateNumOfPlayers":
                    JsonNode numOfPlayers = jsonNode.get("numOfPlayers");
                    if (numOfPlayers != null) {
                        clientPlayerView.updateNumOfPlayers(numOfPlayers.asInt());
                    }
                    break;
                case "youGodPlayer":
                    clientPlayerView.notifyGodPlayer();
                    break;
                case "sendReconnection":
                    ReconnectionModelMemento memento = JsonFactory.readValue(
                            jsonNode.get("memento").asText(),
                            new TypeReference<>() {});
                    clientPlayerView.receiveReconnection(memento);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Received invalid message.");
        }
    }

    public @NotNull ClientExceptionReceiver getReceiveException() {
        return clientExceptionReceiver;
    }
}
