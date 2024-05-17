package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class SendCommandS implements PlayerConnector, TableConnector {
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public SendCommandS(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateHand");
        json.put("cardId", cardId);
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updatePersonalObjective");
        json.put("cardId", cardId);
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void sendStarterCard(int cardId) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "sendStarterCard");
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void sendCandidateObjective(Set<Integer> cardsId) {
        try {
            ObjectNode json = mapper.createObjectNode();
            json.put("method", "sendCandidateObjective");
            String cardsIdJson = mapper.writeValueAsString(cardsId);
            json.put("cardsId", cardsIdJson);
            out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateDeckTop");
        json.put("type", type.toString().toUpperCase());
        json.put("color", color.toString().toUpperCase());
        out.println(json);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId,
                            boolean isRetro, boolean removeMode) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateField");
        json.put("nickname", nickname);
        json.put("x", x);
        json.put("y", y);
        json.put("cardId", cardId);
        json.put("isRetro", isRetro);
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateShownPlayable");
        json.put("previousId", previousId);
        json.put("currentId", currentId);
        out.println(json);
    }

    @Override
    public void updateTurnChange(String nickname) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateTurnChange");
        json.put("nickname", nickname);
        out.println(json);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updatePlayerPoint");
        json.put("nickname", nickname);
        json.put("points", points);
        out.println(json);
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateGameStatus");
        json.put("status", status.toString());
        out.println(json);
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        try {
            ObjectNode json = mapper.createObjectNode();
            json.put("method", "updateCommonObjective");
            String cardIdJson = mapper.writeValueAsString(cardId);
            json.put("cardId", cardIdJson);
            json.put("removeMode", removeMode);
            out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard)
    throws JsonProcessingException {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "receiveFinalLeaderboard");
        json.put("finalLeaderboard", mapper.writeValueAsString(finalLeaderboard));
        out.println(json);
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers)
    throws JsonProcessingException {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updatePlayers");
        json.put("currentPlayers", mapper.writeValueAsString(currentPlayers));
        out.println(json);
    }

    @Override
    public void updateNumOfPlayers(Integer numOfPlayers) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateNumOfPlayers");
        json.put("numOfPlayers", numOfPlayers);
        out.println(json);
    }

    public void youGodPlayer() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "youGodPlayer");
        out.println(json);
    }
}
