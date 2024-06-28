package it.polimi.ingsw.am11.network.RMI.remote.heartbeat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the server to receive heartbeat pings from the clients.
 */
public interface HeartbeatInterface extends Remote {

    void ping(@NotNull String nickname) throws RemoteException;

    int getInterval() throws RemoteException;
}
