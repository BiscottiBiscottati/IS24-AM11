package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.CltToNetConnector;

public class NetworkConnector implements CltToNetConnector {
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

    @Override
    public void setNickname(String nickname) {

    }
}
