package it.polimi.ingsw.am11.network.RMI.remote.chat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The client uses this interface to send chat messages to the server.
 */
public interface ServerChatInterface extends Remote {

    /**
     * The client uses this method to send a public message to the server.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     * @throws RemoteException if an error occurs during the remote invocation
     */
    void pubMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    /**
     * The client uses this method to send a private message to the server.
     *
     * @param sender    the sender of the message
     * @param recipient the recipient of the message
     * @param msg       the message that has been sent
     * @throws RemoteException if an error occurs during the remote invocation
     */
    void pubPrivateMsg(@NotNull String sender, @NotNull String recipient, String msg)
    throws RemoteException;
}
