package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ConnectionFactory {
    @NotNull
    ClientNetworkHandler create(@NotNull String ip,
                                int port,
                                @NotNull ClientViewUpdater updater)
    throws Exception;
}
