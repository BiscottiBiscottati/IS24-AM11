package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {
    void login(String nick)
    throws RemoteException, PlayerInitException, GameStatusException, NumOfPlayersException;

    void logout(String nick) throws RemoteException;


}
