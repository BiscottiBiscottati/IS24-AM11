package it.polimi.ingsw.am11.network;


import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used by the UI to get the connector to send commands to the server and to close
 * the connection.
 * Each network implementation will provide an implementation of this interface.
 *
 * @see it.polimi.ingsw.am11.network.RMI.client.ClientRMI
 * @see it.polimi.ingsw.am11.network.socket.client.ClientSocket
 */
public interface ClientNetworkHandler {
    @NotNull
    ClientGameConnector getGameConnector();

    @NotNull
    ClientChatConnector getChatConnector();

    void close();
}
