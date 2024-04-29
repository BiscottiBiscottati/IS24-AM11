package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.view.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable {

    static int PORT = 1234;

    public static void main(String[] args) {
        System.out.println("Hello from Server!");
        Loggable stub = null;
        ServerMain obj = new ServerMain();
        try {
            stub = (Loggable) UnicastRemoteObject.exportObject(
                    obj, PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.bind("Loggable", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready");
    }

    @Override
    public void login(String nick)
    throws RemoteException, PlayerInitException, GameStatusException, NumOfPlayersException {
        try {
            MessageManager messageManager = new MessageManager();
            VirtualPlayerView view = CentralController.INSTANCE
                    .connectPlayer(nick, messageManager, messageManager);
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid nickname. Please try again.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        } catch (NumOfPlayersException e) {
            throw new NumOfPlayersException("Max players reached.");
        }
    }

    @Override
    public void logout(String nick) throws RemoteException {

    }
}
