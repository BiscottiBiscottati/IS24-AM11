package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.players.utils.Position;

public interface CltToNetConnector {

    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void setStarterCard(boolean isRetro);

    void setPersonalObjective(int cardId);

    void placeCard(Position pos, int cardId, boolean isRetro);

    void setNumOfPlayers(int numOfPlayers);
}
