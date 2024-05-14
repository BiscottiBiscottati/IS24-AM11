package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable {

    static int PORT = 1234;
    static VirtualPlayerView view;
    Registry registry;

    //TODO to save the connector in the server

    public ServerMain() {
        super();
    }

    // FIXME construction process have to be in the constructor since we don't call main from here

    public static void main(String[] args) {
        System.err.println("Server ready");
    }

    public void start() {
        Loggable log;
        try {
            log = (Loggable) UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Bind the remote object's stub in the registry
        try {
            registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Loggable", log);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void login(String nick, ConnectorInterface remoteConnector) throws RemoteException {
        PlayerViewInterface view;

        try {
            ConnectorImplementation connector = new ConnectorImplementation(remoteConnector);
            view = new PlayerViewImpl(CentralController.INSTANCE.connectPlayer(nick, connector,
                                                                               connector));
        } catch (PlayerInitException | GameStatusException | NumOfPlayersException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
        try {
            UnicastRemoteObject.exportObject(view, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        try {
            registry.bind("PlayerView" + nick, view);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout(String nick) throws RemoteException {
        CentralController.INSTANCE.playerDisconnected(nick);
        System.out.println(nick + " disconnected");
    }

    public void setNumOfPlayers(String nick, int val) throws RemoteException {
        try {
            CentralController.INSTANCE.setNumOfPlayers(nick, val);
        } catch (NotGodPlayerException | GameStatusException | NumOfPlayersException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
    }

}
