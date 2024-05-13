package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {

    //FIXME may need to add args for the connectorInterface since the Server needs to call the
    // clients

    void login(String nick, ConnectorInterface connector) throws RemoteException;

    void logout(String nick) throws RemoteException;

}
