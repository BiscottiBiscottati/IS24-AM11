package it.polimi.ingsw.am11.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {
    void login(String nick)
    throws RemoteException;

    void logout(String nick) throws RemoteException;

}
