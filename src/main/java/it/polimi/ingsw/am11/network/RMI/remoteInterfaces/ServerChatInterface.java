package it.polimi.ingsw.am11.network.RMI.remoteInterfaces;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerChatInterface extends Remote {
    void pubMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    void pubPrivateMsg(@NotNull String sender, @NotNull String recipient, String msg)
    throws RemoteException;
}
