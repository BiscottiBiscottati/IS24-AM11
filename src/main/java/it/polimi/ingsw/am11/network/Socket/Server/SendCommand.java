package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class SendCommand implements PlayerConnector, TableConnector {
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    public SendCommand(String nickname, BufferedReader in, PrintWriter out) {
        this.nickname = nickname;
        this.in = in;
        this.out = out;
    }
    @Override
    public void updateHand(int cardId, boolean removeMode) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("cardId", cardId);
        json.put("removeMode", removeMode);
        out.println(json.toString());
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {

    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {

    }

    @Override
    public void updateField(String nickname, Position position, int cardId, boolean removeMode) {

    }

    @Override
    public void updateShownPlayable(int previousId, int currentId) {

    }

    @Override
    public void updateTurnChange(String nickname) {

    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {

    }

    @Override
    public void updateGameStatus(GameStatus status) {

    }

    @Override
    public void updateCommonObjective(int cardId, boolean removeMode) {

    }
}
