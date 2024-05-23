package it.polimi.ingsw.am11.network;

import java.util.Set;

public interface PlayerConnector {
    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void sendStarterCard(int cardId);

    void sendCandidateObjective(Set<Integer> cardsId);

    void notifyGodPlayer();
}
