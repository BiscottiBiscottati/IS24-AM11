package it.polimi.ingsw.am11.network.RMI.server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.HeartbeatInterface;
import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ServerLoggable;
import it.polimi.ingsw.am11.network.RMI.server.chat.ServerChatConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.server.game.ServerConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.server.game.ServerGameCommandsImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI implements ServerLoggable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRMI.class);

    private final int port;
    private final @NotNull Registry registry;
    private final @NotNull ServerGameCommandsImpl ServerGameCommands;
    private final @NotNull HeartbeatManager heartbeatManager;

    public ServerRMI(int port) {
        this.port = port;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerGameCommands = new ServerGameCommandsImpl();
        heartbeatManager = new HeartbeatManager(this);
    }

    public void start() {
        ServerLoggable log;
        ServerGameCommandsInterface view;
        HeartbeatInterface heartbeat;
        try {
            log = (ServerLoggable) UnicastRemoteObject.exportObject(this,
                                                                    0);
            view = (ServerGameCommandsInterface) UnicastRemoteObject.exportObject(
                    ServerGameCommands,
                    0);
            heartbeat = (HeartbeatInterface) UnicastRemoteObject.exportObject(heartbeatManager,
                                                                              0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Bind the remote object's stub in the registry
        try {
            registry.bind("Loggable", log);
            registry.bind("PlayerView", view);
            registry.bind("ping", heartbeat);

            LOGGER.info("SERVER RMI: Server open on port: {}", port);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public void close() {
        try {
            registry.unbind("Loggable");
            registry.unbind("PlayerView");
            registry.unbind("ping");

            try {
                UnicastRemoteObject.unexportObject(this, false);
                UnicastRemoteObject.unexportObject(ServerGameCommands, false);
                UnicastRemoteObject.unexportObject(registry, false);
                UnicastRemoteObject.unexportObject(heartbeatManager, false);
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

    public void removePlayer(@NotNull String nick) {
        ServerGameCommands.removePlayer(nick);
    }

    @Override
    public void login(@NotNull String nick, @NotNull ClientGameUpdatesInterface remoteConnector)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException {
        LOGGER.debug("SERVER RMI: login: {}, {}", nick, remoteConnector);
        ServerConnectorImpl connector = new ServerConnectorImpl(remoteConnector);
        ServerChatConnectorImpl chatConnector = new ServerChatConnectorImpl(); // FIXME to fix
        ServerGameCommands.addPlayer(nick, connector, chatConnector);
        LOGGER.info("SERVER RMI: Player connected: {}", nick);
    }

    @Override
    public void setNumOfPlayers(@NotNull String nick, int val)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
        CentralController.INSTANCE.setNumOfPlayers(nick, val);
    }
}
