package it.polimi.ingsw.am11.network.RMI.remote.heartbeat;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartbeatInterface extends Remote {
    void ping(@NotNull String nickname) throws RemoteException;

    int getInterval() throws RemoteException;
}
