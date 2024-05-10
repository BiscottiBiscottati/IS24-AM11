package it.polimi.ingsw.am11.view.client;

// net to client connector

import it.polimi.ingsw.am11.network.CltToNetConnector;

// the network handlers will use (directly) these methods to modify the client view
public class ClientActionHandler {
    private final CltToNetConnector cltToNetConnector;


    public ClientActionHandler(CltToNetConnector cltToNetConnector) {
        this.cltToNetConnector = cltToNetConnector;
    }

    public void sendCommand() {
        //TODO to implement we may use listener pattern to send specific commands
    }
}
