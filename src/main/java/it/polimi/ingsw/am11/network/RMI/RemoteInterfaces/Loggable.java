package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {

    //FIXME may need to add args for the connectorInterface since the Server needs to call the
    // clients
    void login(String nick)
    throws RemoteException, NumOfPlayersException, PlayerInitException, GameStatusException;

    void logout(String nick) throws RemoteException;

}
