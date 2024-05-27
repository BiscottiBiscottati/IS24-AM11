package it.polimi.ingsw.am11.network.factory;

import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ConnectionFactory {
    ClientNetworkHandler create(@NotNull String ip,
                                int port,
                                @NotNull ClientViewUpdater updater)
    throws Exception;
}
