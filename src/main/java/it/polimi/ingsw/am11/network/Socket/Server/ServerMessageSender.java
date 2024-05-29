package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.ServerTableConnector;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.Socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

public class ServerMessageSender implements ServerPlayerConnector, ServerTableConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.GAME;

    private final @NotNull PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public ServerMessageSender(@NotNull PrintWriter out) {
        this.out = out;
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateHand");
        json.put("cardId", cardId);
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updatePersonalObjective");
        json.put("cardId", cardId);
        json.put("removeMode", removeMode);
        out.println(json);
    }

    @Override
    public void sendStarterCard(int cardId) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "sendStarterCard");
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void sendCandidateObjective(@NotNull Set<Integer> cardsId) {
        try {
            ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
            json.put("method", "sendCandidateObjective");
            String cardsIdJson = mapper.writeValueAsString(cardsId);
            json.put("cardsId", cardsIdJson);
            out.println(json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while sending candidate objective", e);
        }
    }

    @Override
    public void notifyGodPlayer() {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "youGodPlayer");
        out.println(json);
    }

    @Override
    public void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateDeckTop");
        json.put("type", type.toString().toUpperCase());
        json.put("color", color.toString().toUpperCase());
        out.println(json);
    }

    @Override
    public void updateField(@NotNull String nickname, int x, int y, int cardId,
                            boolean isRetro, boolean removeMode) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
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
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateShownPlayable");
        json.put("previousId", previousId);
        json.put("currentId", currentId);
        out.println(json);
    }

    @Override
    public void updateTurnChange(@NotNull String nickname) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateTurnChange");
        json.put("nickname", nickname);
        out.println(json);
    }

    @Override
    public void updatePlayerPoint(@NotNull String nickname, int points) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updatePlayerPoint");
        json.put("nickname", nickname);
        json.put("points", points);
        out.println(json);
    }

    @Override
    public void updateGameStatus(@NotNull GameStatus status) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateGameStatus");
        json.put("status", status.toString());
        out.println(json);
    }

    @Override
    public void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode) {
        try {
            ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
            json.put("method", "updateCommonObjective");
            String cardIdJson = mapper.writeValueAsString(cardId);
            json.put("cardId", cardIdJson);
            json.put("removeMode", removeMode);
            out.println(json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while sending common objective", e);
        }
    }

    @Override
    public void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard) {
        try {
            ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
            json.put("method", "receiveFinalLeaderboard");
            json.put("finalLeaderboard", mapper.writeValueAsString(finalLeaderboard));
            out.println(json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while sending final leaderboard", e);
        }
    }

    @Override
    public void updatePlayers(@NotNull Map<PlayerColor, String> currentPlayers) {
        try {
            ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
            json.put("method", "updatePlayers");
            json.put("currentPlayers", mapper.writeValueAsString(currentPlayers));
            out.println(json);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while sending current players", e);
        }
    }

    @Override
    public void updateNumOfPlayers(@NotNull Integer numOfPlayers) {
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", "updateNumOfPlayers");
        json.put("numOfPlayers", numOfPlayers);
        out.println(json);
    }
}
