package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientMain {

    static String nickname;
    private static Registry registry;

    public ClientMain(String ip, int port) {
        super();
        try {
            // Getting the registry
            registry = LocateRegistry.getRegistry(ip, port);
            // Looking up the registry for the remote object
            System.out.println("Remote method invoked");
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println("Hello from Client!");
    }

    public void login(String nick, ClientViewUpdater updater) throws RemoteException,
                                                                     NotBoundException {
        Loggable stub1 = (Loggable) registry.lookup("Loggable");
        ClientToServerConnector clientObject = new ClientToServerConnector(updater);
        ConnectorInterface connector = (ConnectorInterface) UnicastRemoteObject.exportObject(
                clientObject, 0);

        stub1.login(nick, connector);
        nickname = nick;
    }

    public void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.setNumOfPlayers(nick, numOfPlayers);
    }

    public void logout(String nick) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.logout(nick);
    }

    public void setStarterCard(String nick, boolean isRetro) throws RemoteException {
    }
}
