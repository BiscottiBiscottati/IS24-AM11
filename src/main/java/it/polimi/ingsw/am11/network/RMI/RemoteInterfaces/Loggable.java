package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {

    void login(String nick, ConnectorInterface connector) throws RemoteException;

    void logout(String nick) throws RemoteException;

    default void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
    }

    void reconnect(String nick) throws RemoteException;

}
