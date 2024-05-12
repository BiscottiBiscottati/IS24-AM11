package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


// FIXME the client shouldn't see these methods when Loggable gets a login the server will use
//  CentralController to add player
public interface CentralControllerInterface extends Remote {

    CentralControllerInterface playerDisconnected() throws RemoteException;

    CentralControllerInterface playerReconnected(String nickname) throws RemoteException;


    CentralControllerInterface connectPlayer(String nickname) throws RemoteException;
}
