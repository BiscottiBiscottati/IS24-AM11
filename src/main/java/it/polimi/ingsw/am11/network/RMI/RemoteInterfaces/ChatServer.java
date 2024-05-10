package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void login(ChatClient cc) throws RemoteException;

    void send(String msg) throws RemoteException;
}
