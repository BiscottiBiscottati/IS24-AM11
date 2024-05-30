package it.polimi.ingsw.am11.network;

import org.jetbrains.annotations.NotNull;

public interface ServerChatConnector {

    void sendPublicMsg(@NotNull String sender, @NotNull String msg);

    void sendPrivateMsg(@NotNull String sender, @NotNull String recipient, @NotNull String msg);
}
