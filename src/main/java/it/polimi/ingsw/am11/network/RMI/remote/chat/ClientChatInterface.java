package it.polimi.ingsw.am11.network.RMI.remote.chat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the client to receive messages from the server.
 */
public interface ClientChatInterface extends Remote {
    /**
     * The server uses this method to send a public message to a specific client.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     * @throws RemoteException if an error occurs during the remote invocation
     */
    void receiveMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    /**
     * This method is used by the server to send a private message to a specific client.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     * @throws RemoteException if an error occurs during the remote invocation
     */
    void receivePrivateMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    /**
     * This method is used by the server to confirm to the sender that the message he sent has been
     * sent.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     * @throws RemoteException if an error occurs during the remote invocation
     */
    void receiveConfirmation(@NotNull String sender, @NotNull String msg) throws RemoteException;
}
