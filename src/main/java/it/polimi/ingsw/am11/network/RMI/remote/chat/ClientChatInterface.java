package it.polimi.ingsw.am11.network.RMI.remote.chat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the client to receive messages from the server.
 * <p>
 *     The client will implement this interface to receive messages from the server.
 * </p>
 */
public interface ClientChatInterface extends Remote {
    void receiveMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    void receivePrivateMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    void receiveConfirmation(@NotNull String sender, @NotNull String msg) throws RemoteException;
}
