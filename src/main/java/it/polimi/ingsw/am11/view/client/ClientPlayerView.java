package it.polimi.ingsw.am11.view.client;

// net to client connector

import it.polimi.ingsw.am11.network.CltToNetConnector;

// the network handlers will use (directly) these methods to modify the client view
public class ClientPlayerView {
    private final String playerName;
    private final ClientViewUpdater clientViewUpdater;
    private final CltToNetConnector cltToNetConnector;


    public ClientPlayerView(String playerName, ClientViewUpdater clientViewUpdater,
                            CltToNetConnector cltToNetConnector) {
        this.playerName = playerName;
        this.clientViewUpdater = clientViewUpdater;
        this.cltToNetConnector = cltToNetConnector;
    }

    // FIXME: temporary constructor for testing
    public ClientPlayerView(String playerName) {
        this.playerName = playerName;
        this.clientViewUpdater = null;
        this.cltToNetConnector = null;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ClientViewUpdater getClientViewUpdater() {
        return clientViewUpdater;
    }

    public void sendCommand() {
        //TODO to implement
    }
}
