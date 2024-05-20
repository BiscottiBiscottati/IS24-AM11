package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.CltToNetConnector;

public class NetworkConnector implements CltToNetConnector {
    private String nickname;
    private final ClientMain main;

    public NetworkConnector(ClientMain main) {
        this.main = main;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public void setStarterCard(boolean isRetro) {

    }

    @Override
    public void setPersonalObjective(int cardId) {

    }

    @Override
    public void placeCard(Position pos, int cardId, boolean isRetro) {

    }

    @Override
    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {

    }

    @Override
    public void setNumOfPlayers(int numOfPlayers) {

    }
}
