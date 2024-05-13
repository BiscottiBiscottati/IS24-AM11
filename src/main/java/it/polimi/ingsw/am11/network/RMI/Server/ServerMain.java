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
    private final CentralController centralController;

    //TODO to save the connector in the server

    public ServerMain() {
        super();
        centralController = CentralController.INSTANCE;
        view = null;
    }

    // FIXME construction process have to be in the constructor since we don't call main from here

    public static void main(String[] args) {
        System.err.println("Server ready");
    }

    public static void run() {
        Loggable log;
        PlayerViewInterface playerV;
        PlayerViewImpl playerView = new PlayerViewImpl(view);
        ServerMain obj;
        obj = new ServerMain();
        try {
            log = (Loggable) UnicastRemoteObject.exportObject(obj, 0);
            playerV = (PlayerViewInterface) UnicastRemoteObject.exportObject(playerView, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Bind the remote object's stub in the registry
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Loggable", log);
            registry.bind("PlayerView", playerV);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void login(String nick, ConnectorInterface remoteConnector) throws RemoteException {
        PlayerViewInterface view;

        try {
            ConnectorImplementation connector = new ConnectorImplementation(remoteConnector);
            view = new PlayerViewImpl(centralController.connectPlayer(nick, connector,
                                                                      connector));
        } catch (PlayerInitException | GameStatusException | NumOfPlayersException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
        try {
            UnicastRemoteObject.exportObject(view, 0);
        } catch (RemoteException e) {
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
            centralController.setNumOfPlayers(nick, val);
        } catch (NotGodPlayerException | GameStatusException | NumOfPlayersException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
    }

}
