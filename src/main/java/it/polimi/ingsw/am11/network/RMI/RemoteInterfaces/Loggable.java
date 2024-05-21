package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import it.polimi.ingsw.am11.model.exceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {

    void login(String nick, ConnectorInterface connector)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException;

    void logout(String nick) throws RemoteException;

    default void setNumOfPlayers(String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
    }

    void reconnect(String nick) throws RemoteException;

}
