package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
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
    Registry registry;
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
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
            registry.bind("Loggable", log);
            registry.bind("PlayerView", view);

            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

            LOGGER.info("SERVER RMI: Server open on port: {}", port);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            registry.unbind("Loggable");
            registry.unbind("PlayerView");

            try {
                UnicastRemoteObject.unexportObject(this, true);
                UnicastRemoteObject.unexportObject(playerView, true);
            } catch (NoSuchObjectException e) {
                LOGGER.debug("SERVER RMI: Object already un-exported");
            }

            LOGGER.info("SERVER RMI: Server closed");
        } catch (RemoteException e) {
            LOGGER.error("SERVER RMI: Error while closing server", e);
        } catch (NotBoundException e) {
            LOGGER.debug("SERVER RMI: Not bound (probably already closed)");
        }
    }

    @Override
    public void login(String nick, ConnectorInterface remoteConnector)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException {
        LOGGER.debug("SERVER RMI: login: {}, {}", nick, remoteConnector);
        ConnectorImplementation connector = new ConnectorImplementation(remoteConnector);
        VirtualPlayerView view = new VirtualPlayerView(connector, nick);
        CentralController.INSTANCE.connectPlayer(nick, connector, connector);
        playerView.addPlayer(nick, view);
        LOGGER.info("SERVER RMI: Player connected: {}", nick);
        if (Objects.equals(CentralController.INSTANCE.getGodPlayer(), nick)) {
            LOGGER.info("SERVER RMI: God player: {}", nick);
            connector.notifyGodPlayer();
        }
    }

    @Override
    public void logout(String nick) throws RemoteException {
        CentralController.INSTANCE.playerDisconnected(nick);
        System.out.println(nick + " disconnected");
    }

    public void setNumOfPlayers(String nick, int val)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
        CentralController.INSTANCE.setNumOfPlayers(nick, val);
    }

    @Override
    public void reconnect(String nick) throws RemoteException {
        if (playerView.containsPlayer(nick)) {
            CentralController.INSTANCE.playerReconnected(nick);
        }
    }
}
