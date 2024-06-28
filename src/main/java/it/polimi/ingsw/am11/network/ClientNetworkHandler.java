package it.polimi.ingsw.am11.network;


import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used by the UI to get the connector to send commands to the server and to close
 * the connection. Each network implementation will provide an implementation of this interface.
 *
 * @see it.polimi.ingsw.am11.network.RMI.client.ClientRMI
 * @see it.polimi.ingsw.am11.network.socket.client.ClientSocket
 */
public interface ClientNetworkHandler {
    /**
     * Returns the connector to send commands to the server.
     *
     * @return the connector to send commands to the server.
     */
    @NotNull
    ClientGameConnector getGameConnector();

    /**
     * Returns the connector to send chat messages to the server.
     *
     * @return the connector to send chat messages to the server.
     */
    @NotNull
    ClientChatConnector getChatConnector();

    /**
     * Closes the connection to the server, it will stop all the threads.
     */
    void close();
}
