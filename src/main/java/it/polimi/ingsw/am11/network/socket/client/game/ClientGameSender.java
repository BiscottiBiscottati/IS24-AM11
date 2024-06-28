package it.polimi.ingsw.am11.network.socket.client.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import it.polimi.ingsw.am11.network.socket.client.PongHandler;
import it.polimi.ingsw.am11.network.socket.client.chat.ClientChatSender;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

/**
 * The class that sends the game messages to the server
 * @see ClientGameConnector
 * @see ContextJSON
 * @see PongHandler
 * @see ClientChatSender
 * @param out the output stream
 * @param pongHandler the pong handler
 * @param chatSender the chat sender
 */
public record ClientGameSender(@NotNull PrintWriter out, @NotNull PongHandler pongHandler,
                               @NotNull ClientChatSender chatSender)
        implements ClientGameConnector {
    private static final ContextJSON CONTEXT = ContextJSON.GAME;

    @Override
    public void setStarterCard(boolean isRetro) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "setStarterCard");
        json.put("isRetro", isRetro);
        out.println(json);
    }

    @Override
    public void setPersonalObjective(int cardId) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "setPersonalObjective");
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void placeCard(@NotNull Position pos, int cardId, boolean isRetro) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "placeCard");
        json.put("x", pos.x());
        json.put("y", pos.y());
        json.put("cardId", cardId);
        json.put("isRetro", isRetro);
        out.println(json);
    }

    @Override
    public void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "drawCard");
        json.put("fromVisible", fromVisible);
        json.put("type", type.toString().toUpperCase());
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "setNumOfPlayers");
        json.put("numOfPlayers", numOfPlayers);
        out.println(json);
    }

    @Override
    public void setNickname(@NotNull String nickname) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "setNickname");
        json.put("nickname", nickname);
        out.println(json);
        pongHandler.start();
        chatSender.setNickname(nickname);
    }

    @Override
    public void syncMeUp() {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "syncMeUp");
        out.println(json);
    }
}
