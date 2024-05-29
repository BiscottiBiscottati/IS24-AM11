package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;

public interface ServerChatInterface extends Remote {
    void pubMsg(@NotNull String sender, @NotNull String msg);

    void pubPrivateMsg(@NotNull String sender, @NotNull String recipient, String msg);
}
