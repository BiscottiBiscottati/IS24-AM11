package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

/**
 * The UI uses this interface to create a new connection to the server. Each network implementation
 * will provide an implementation of this interface.
 */
@FunctionalInterface
public interface ConnectionFactory {
    /**
     * Creates a ClientNetworkHandler to connect to the server.
     *
     * @param ip      the server ip
     * @param port    the server port
     * @param updater the updater to notify the UI
     * @return a new ClientNetworkHandler
     * @throws Exception if an error occurs
     */
    @NotNull
    ClientNetworkHandler create(@NotNull String ip,
                                int port,
                                @NotNull ClientViewUpdater updater)
    throws Exception;
}
