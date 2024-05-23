package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

public class SendCommandC implements CltToNetConnector {
    // This class is used to send commands to the server
    // It is used by the client to send commands to the server
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public SendCommandC(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void setStarterCard(boolean isRetro) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "setStarterCard");
        json.put("isRetro", isRetro);
        out.println(json);
    }

    @Override
    public void setPersonalObjective(int cardId) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "setPersonalObjective");
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void placeCard(@NotNull Position pos, int cardId, boolean isRetro) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "placeCard");
        json.put("x", pos.x());
        json.put("y", pos.y());
        json.put("cardId", cardId);
        json.put("isRetro", isRetro);
        out.println(json);
    }

    @Override
    public void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId) {
        ObjectNode json = mapper.createObjectNode();
        json.put("method", "drawCard");
        json.put("fromVisible", fromVisible);
        json.put("type", type.toString().toUpperCase());
        json.put("cardId", cardId);
        out.println(json);
    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) {
        out.println(numOfPlayers);
    }

    @Override
    public void setNickname(String nickname) {
        out.println(nickname);
    }
}
