package it.polimi.ingsw.am11.network.RMI.remote.chat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientChatInterface extends Remote {
    void receiveMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    void receivePrivateMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;
}
