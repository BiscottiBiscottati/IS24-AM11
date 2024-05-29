package it.polimi.ingsw.am11.network;

import org.jetbrains.annotations.NotNull;

public interface ServerChatConnector {

    void sendMsg(@NotNull String sender, @NotNull String msg);
}
