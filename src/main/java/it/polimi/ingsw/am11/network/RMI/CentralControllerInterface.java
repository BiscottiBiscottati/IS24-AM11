package it.polimi.ingsw.am11.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CentralControllerInterface extends Remote {

    CentralControllerInterface playerDisconnected() throws RemoteException;

    CentralControllerInterface playerReconnected(String nickname) throws RemoteException;


    CentralControllerInterface connectPlayer(String nickname) throws RemoteException;
}
