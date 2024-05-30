package it.polimi.ingsw.am11.network.connector;

import org.jetbrains.annotations.NotNull;

public interface ClientChatConnector {
    void pubMsg(@NotNull String msg);

    void pubPrivateMsg(@NotNull String recipient, @NotNull String msg);
}
