package it.polimi.ingsw.am11.network.RMI.remote.chat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the server to send messages to the clients.
 * <p>
 *     The server will implement this interface to send messages to the clients.
 *     The clients will implement the {@link ClientChatInterface} interface to receive messages from the server.
 * </p>
 */
public interface ServerChatInterface extends Remote {
    void pubMsg(@NotNull String sender, @NotNull String msg) throws RemoteException;

    void pubPrivateMsg(@NotNull String sender, @NotNull String recipient, String msg)
    throws RemoteException;
}
