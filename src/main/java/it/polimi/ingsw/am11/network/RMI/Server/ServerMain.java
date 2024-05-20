package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable {

    private final int port;
    PlayerViewImpl playerView;
    ExceptionConnector exceptionConnector;

    {
        try {
            playerView = new PlayerViewImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerMain(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        System.out.println("Server ready");
    }

    public void start() {
        Loggable log;
        PlayerViewInterface view;
        try {
            log = (Loggable) UnicastRemoteObject.exportObject(this, 0);
            view = (PlayerViewInterface) UnicastRemoteObject.exportObject(playerView, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Bind the remote object's stub in the registry
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("Loggable", log);
            registry.bind("PlayerView", view);

            System.out.println("RMI: Server open on port: " + port);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void login(String nick, ConnectorInterface remoteConnector) throws RemoteException {
        try {
            if (playerView.containsPlayer(nick)) {
                CentralController.INSTANCE.playerReconnected(nick);
            } else {
                ConnectorImplementation connector = new ConnectorImplementation(remoteConnector);
                VirtualPlayerView view = new VirtualPlayerView(connector, nick);
                CentralController.INSTANCE.connectPlayer(nick, connector, connector);
                playerView.addPlayer(nick, view);
            }
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        } catch (NumOfPlayersException e) {
            exceptionConnector.throwException(e);
        } catch (NotSetNumOfPlayerException e) {
            exceptionConnector.throwException(e);
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
        } catch (NotGodPlayerException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        } catch (NumOfPlayersException e) {
            exceptionConnector.throwException(e);
        }
    }

    @Override
    public void reconnect(String nick) throws RemoteException {
        if (playerView.containsPlayer(nick)) {
            CentralController.INSTANCE.playerReconnected(nick);
        }
    }
}
