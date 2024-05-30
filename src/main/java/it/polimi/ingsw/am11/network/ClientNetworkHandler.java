package it.polimi.ingsw.am11.network;


import org.jetbrains.annotations.NotNull;

/**
 * This interface is used by the UI to get the connector to send commands to the server and to close
 * the connection.
 * <p>
 * Each network implementation will provide an implementation of this interface.
 *
 * @see it.polimi.ingsw.am11.network.RMI.Client.ClientRMI
 * @see it.polimi.ingsw.am11.network.Socket.Client.ClientSocket
 */
public interface ClientNetworkHandler {
    @NotNull
    ClientGameConnector getGameConnector();

    @NotNull
    ClientChatConnector getChatConnector();

    void close();
}
