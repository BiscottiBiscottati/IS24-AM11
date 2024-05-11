package it.polimi.ingsw.am11.network.RMI.Chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    void receive(String msg) throws RemoteException;
}
