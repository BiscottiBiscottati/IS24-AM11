package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.network.ClientPlayerConnector;

import java.util.Set;

public class ClientPlayerView {
    private final String playerName;
    private final ClientViewUpdater viewUpdater;
    private final ClientPlayerConnector playerConnector;

    public ClientPlayerView(String playerName, ClientViewUpdater viewUpdater,
                            ClientPlayerConnector playerConnector) {
        this.playerName = playerName;
        this.viewUpdater = viewUpdater;
        this.playerConnector = playerConnector;
    }

    void updateHand(int cardId, boolean removeMode) {
    }

    void updatePersonalObjective(int cardId, boolean removeMode) {
    }

    void sendStarterCard(int cardId) {
    }

    void sendCandidateObjective(Set<Integer> cardsId) {
    }
}
