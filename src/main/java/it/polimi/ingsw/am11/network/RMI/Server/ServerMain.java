package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class ServerMain implements Loggable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

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

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    registry.unbind("Loggable");
                    registry.unbind("PlayerView");

                    UnicastRemoteObject.unexportObject(this, true);
                    UnicastRemoteObject.unexportObject(playerView, true);

                    LOGGER.info("RMI: Server closed");
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            }));

            LOGGER.info("RMI: Server open on port: {}", port);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void login(String nick, ConnectorInterface remoteConnector) throws RemoteException {
        try {
            LOGGER.debug("RMI: login: {}, {}", nick, remoteConnector);
            ConnectorImplementation connector = new ConnectorImplementation(remoteConnector);
            VirtualPlayerView view = new VirtualPlayerView(connector, nick);
            CentralController.INSTANCE.connectPlayer(nick, connector, connector);
            playerView.addPlayer(nick, view);
            LOGGER.info("RMI: Player connected: {}", nick);
            if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nick)) {
                LOGGER.info("RMI: God player: {}", nick);
                connector.notifyGodPlayer();
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
