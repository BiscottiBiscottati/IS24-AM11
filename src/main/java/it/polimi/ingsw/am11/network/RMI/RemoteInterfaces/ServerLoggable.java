package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerLoggable extends Remote {

    void login(String nick, ClientGameUpdatesInterface connector)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException;

    default void setNumOfPlayers(String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
    }

}
