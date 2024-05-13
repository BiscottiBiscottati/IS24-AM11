package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class ClientMain {

    static int PORT = 1234;
    static String ip = "127.0.0.1";
    static String nickname;
    private static Registry registry;
    private static Registry clientRegistry;

    public ClientMain() {
        super();
        try {
            // Getting the registry
            registry = LocateRegistry.getRegistry(ip, PORT);
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
        registry = LocateRegistry.getRegistry(ip, PORT);
        Loggable stub1 = (Loggable) registry.lookup("Loggable");
        ClientToServerConnector clientObject = new ClientToServerConnector(updater);
        ConnectorInterface connector = (ConnectorInterface) UnicastRemoteObject.exportObject(
                clientObject, 0);

        stub1.login(nick, connector);
        nickname = nick;
    }

    public void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
        registry = LocateRegistry.getRegistry(ip, PORT);
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nick)) {
            System.out.println("God player: " + nick);
            stub3.setNumOfPlayers(nick, numOfPlayers);
        }
    }


}
