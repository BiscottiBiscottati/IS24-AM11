package it.polimi.ingsw.am11.view.client;

// net to client connector

import it.polimi.ingsw.am11.network.CltToNetConnector;

// the network handlers will use (directly) these methods to modify the client view
public class ClientPlayerView {
    private final CltToNetConnector cltToNetConnector;


    public ClientPlayerView(CltToNetConnector cltToNetConnector) {
        this.cltToNetConnector = cltToNetConnector;
    }

    public void sendCommand() {
        //TODO to implement
    }
}
