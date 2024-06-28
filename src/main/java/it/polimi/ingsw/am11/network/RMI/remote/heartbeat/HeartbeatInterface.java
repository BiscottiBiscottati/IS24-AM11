package it.polimi.ingsw.am11.network.RMI.remote.heartbeat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the server to receive heartbeat pings from the clients.
 */
public interface HeartbeatInterface extends Remote {

    /**
     * The client calls this method to ping the server.
     * <p>
     * Every n milliseconds that the server provides with {@link #getInterval()}.
     * <p>
     * The server checks if the client has sent a ping within the last 5n milliseconds.
     *
     * @param nickname The nickname of the client.
     * @throws RemoteException If a remote error occurs.
     */
    void ping(@NotNull String nickname) throws RemoteException;

    /**
     * The client calls this method to get the interval between two pings.
     *
     * @return The interval between two pings in milliseconds.
     * @throws RemoteException If a remote error occurs.
     */
    int getInterval() throws RemoteException;
}
