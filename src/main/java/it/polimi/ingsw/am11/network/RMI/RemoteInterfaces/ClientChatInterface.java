package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;

public interface ClientChatInterface extends Remote {
    void receiveMsg(@NotNull String sender, @NotNull String msg);
}
