package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HearbeatInterface extends Remote {
    void ping(String nickname) throws RemoteException;
}
