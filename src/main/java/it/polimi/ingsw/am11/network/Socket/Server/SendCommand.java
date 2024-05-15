package it.polimi.ingsw.am11.network.Socket.Server;

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

public class SendCommand implements PlayerConnector, TableConnector {
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public SendCommand(PrintWriter out) {
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
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "sendCandidateObjective");
        json.put("cardsId", cardsId.toString());
        out.println(json);
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateDeckTop");
        json.put("type", type.toString());
        json.put("color", color.toString());
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
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "updateCommonObjective");
//        json.put("cardId", cardId); // FIXME changed to set need change
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        //TODO to implement
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        //TODO to implement
    }

    public void youGodPlayer() {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "youGodPlayer");
        out.println(json);
    }
}
