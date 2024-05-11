package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class ClientMain {

    static int PORT = 1234;
    static String nickname;

    public static void main(String[] args) throws RemoteException {
        System.out.println("Hello from Client!");

        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            // Looking up the registry for the remote object
            System.out.println("Remote method invoked");
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e);
            throw new RuntimeException(e);
        }
    }

    public void login(String nick) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        Loggable stub1 = (Loggable) registry.lookup("Loggable");
        try {
            stub1.login(nick);
            nickname = nick;
        } catch (NumOfPlayersException | PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNumOfPlayers(int numOfPlayers) throws RemoteException,
                                                         NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        PlayerViewInterface stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nickname)) {
            System.out.println("God player: " + nickname);
            stub3.setNumofPlayers(nickname, numOfPlayers);
        }
    }


}
