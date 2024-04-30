package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientPlayerConnector;

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

    void setStarterCard(boolean isRetro) {
    }

    void setPersonalObjective(int cardId) {
    }

    void placeCard(Position pos, int cardId, boolean isRetro) {

    }


}
