package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.Socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

public class ClientGameSender implements ClientGameConnector {
    private static final ContextJSON CONTEXT = ContextJSON.GAME;

    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();
    private final PongHandler pongHandler;
    private final ClientChatSender chatSender;

    public ClientGameSender(@NotNull PrintWriter out,
                            @NotNull PongHandler pongHandler,
                            @NotNull ClientChatSender chatSender) {
        this.out = out;
        this.pongHandler = pongHandler;
        this.chatSender = chatSender;
    }

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
}
