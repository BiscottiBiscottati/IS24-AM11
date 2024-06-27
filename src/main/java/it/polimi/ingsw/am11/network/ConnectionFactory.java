package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used by the UI to create a new connection to the server.
 * Each network implementation will provide an implementation of this interface.
 */
@FunctionalInterface
public interface ConnectionFactory {
    @NotNull
    ClientNetworkHandler create(@NotNull String ip,
                                int port,
                                @NotNull ClientViewUpdater updater)
    throws Exception;
}
