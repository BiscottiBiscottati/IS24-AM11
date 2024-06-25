package it.polimi.ingsw.am11.network.RMI.server;

import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.remote.ServerLoggable;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ServerChatInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.RMI.remote.heartbeat.HeartbeatInterface;
import it.polimi.ingsw.am11.network.RMI.server.chat.ServerChatConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.server.chat.ServerChatInterfaceImpl;
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
import java.util.ArrayList;
import java.util.List;

public class ServerRMI implements ServerLoggable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRMI.class);

    private final int port;
    private final @NotNull Registry registry;
    private final @NotNull ServerGameCommandsImpl ServerGameCommands;
    private final @NotNull ServerChatInterfaceImpl chatInterface;
    private final @NotNull HeartbeatManager heartbeatManager;
    private final @NotNull List<ClientGameUpdatesInterface> playersRemote;

    public ServerRMI(int port) {
        this.port = port;
        this.playersRemote = new ArrayList<>(4);
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ServerGameCommands = new ServerGameCommandsImpl();
        heartbeatManager = new HeartbeatManager(this);
        chatInterface = new ServerChatInterfaceImpl();
        ServerConnectorImpl.start();
        ServerChatConnectorImpl.start();
    }

    public void start() {
        ServerLoggable log;
        ServerGameCommandsInterface view;
        HeartbeatInterface heartbeat;
        ServerChatInterface chat;
        try {
            log = (ServerLoggable) UnicastRemoteObject.exportObject(this,
                                                                    0);
            view = (ServerGameCommandsInterface) UnicastRemoteObject.exportObject(
                    ServerGameCommands,
                    0);
            heartbeat = (HeartbeatInterface) UnicastRemoteObject.exportObject(heartbeatManager,
                                                                              0);
            chat = (ServerChatInterface) UnicastRemoteObject.exportObject(
                    chatInterface,
                    0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        // Bind the remote object's stub in the registry
        try {
            registry.bind("Loggable", log);
            registry.bind("PlayerView", view);
            registry.bind("ping", heartbeat);
            registry.bind("chat", chat);

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
            registry.unbind("chat");

            try {
                UnicastRemoteObject.unexportObject(this, false);
                UnicastRemoteObject.unexportObject(ServerGameCommands, false);
                UnicastRemoteObject.unexportObject(registry, false);
                UnicastRemoteObject.unexportObject(heartbeatManager, false);
                UnicastRemoteObject.unexportObject(chatInterface, false);
            } catch (NoSuchObjectException e) {
                LOGGER.debug("SERVER RMI: Object already un-exported");
            }

        } catch (RemoteException e) {
            LOGGER.error("SERVER RMI: Error while closing server", e);
        } catch (NotBoundException e) {
            LOGGER.debug("SERVER RMI: Not bound (probably already closed)");
        } finally {
            heartbeatManager.close();
            ServerConnectorImpl.stop();
            ServerChatConnectorImpl.stop();
            LOGGER.info("SERVER RMI: Server closed");
        }
    }

    public void removeAllPlayers() {
        ServerGameCommands.clearPlayers();
        for (ClientGameUpdatesInterface player : playersRemote) {
            try {
                player.youUgly();
            } catch (RemoteException e) {
                LOGGER.debug("SERVER RMI: unable to disconnect player or already disconnected");
            }
        }
        heartbeatManager.clear();
    }

    public void removePlayer(@NotNull String nick) {
        ServerGameCommands.removePlayer(nick);
    }

    @Override
    public void login(@NotNull String nick, @NotNull ClientGameUpdatesInterface remoteConnector,
                      @NotNull ClientChatInterface remoteChat)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException {
        LOGGER.debug("SERVER RMI: login: {}, {}", nick, remoteConnector);
        playersRemote.add(remoteConnector);
        ServerConnectorImpl connector = new ServerConnectorImpl(remoteConnector);
        ServerChatConnectorImpl chatConnector = new ServerChatConnectorImpl(remoteChat);
        ServerGameCommands.addPlayer(nick, connector, chatConnector);
        LOGGER.info("SERVER RMI: Player connected: {}", nick);
    }
}
