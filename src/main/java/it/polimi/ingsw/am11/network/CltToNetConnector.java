package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;

public interface CltToNetConnector {

    void setStarterCard(boolean isRetro);

    void setPersonalObjective(int cardId);

    void placeCard(Position pos, int cardId, boolean isRetro);

    void drawCard(boolean fromVisible, PlayableCardType type, int cardId);

    void setNumOfPlayers(int numOfPlayers);

    void connect(String nickname);
}
