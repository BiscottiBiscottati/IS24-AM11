package it.polimi.ingsw.am11.network;

public interface PlayerConnector {
    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);
}
