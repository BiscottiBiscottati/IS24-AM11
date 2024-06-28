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

/**
 * This class is responsible for managing the RMI server. It implements the {@link ServerLoggable}
 * interface.
 * <p>
 * The class is instantiated with a port number, which is used to create a {@link Registry} object.
 * The class provides methods to start and close the server, as well as to add and remove players.
 * The class also provides a method to log in a player. The class also provides a method to remove
 * all players.
 * </p>
 */
public class ServerRMI implements ServerLoggable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRMI.class);

    private final int port;
    private final @NotNull Registry registry;
    private final @NotNull ServerGameCommandsImpl ServerGameCommands;
    private final @NotNull ServerChatInterfaceImpl chatInterface;
    private final @NotNull HeartbeatManager heartbeatManager;
    private final @NotNull List<ClientGameUpdatesInterface> playersRemote;

    /**
     * Creates a new RMI server.
     *
     * @param port The port number to create the server on.
     * @throws RuntimeException if an error occurs while creating the server.
     * @see LocateRegistry#createRegistry(int)
     * @see ServerGameCommandsImpl
     * @see HeartbeatManager
     * @see ServerChatInterfaceImpl
     * @see ServerConnectorImpl
     */
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

    /**
     * Starts the server.
     * <p>
     * The method exports the server's remote objects and binds them to the registry. The method
     * also adds a shutdown hook to close the server when the JVM is shut down. The method logs a
     * message when the server is open. The method throws a {@link RuntimeException} if an error
     * occurs while exporting or binding the remote objects. The method catches and logs any
     * {@link RemoteException} or {@link AlreadyBoundException} thrown while binding the remote
     * objects. The method also catches and logs any {@link NotBoundException} thrown while closing
     * the server.
     */
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

    /**
     * Closes the server.
     * <p>
     * The method unbinds the remote objects from the registry. The method also unexports the remote
     * objects. The method logs a message when the server is closed. The method catches and logs any
     * {@link RemoteException} or {@link NotBoundException} thrown while unbinding the remote
     * objects. The method also catches and logs any {@link NoSuchObjectException} thrown while
     * unexporting the remote objects. The method also closes the {@link HeartbeatManager}. The
     * method also stops the {@link ServerConnectorImpl} and {@link ServerChatConnectorImpl}.
     */
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

    /**
     * Removes all players.
     * <p>
     * The method clears the players from the <code>ServerGameCommand</code> and sends a message to
     * all players to disconnect then clears the {@link HeartbeatManager}.
     * </p>
     */
    public void removeAllPlayers() {
        ServerGameCommands.clearPlayers();
        for (ClientGameUpdatesInterface player : playersRemote) {
            try {
                player.youUgly();
            } catch (RemoteException e) {
                LOGGER.warn("SERVER RMI: unable to disconnect player or already disconnected");
            }
        }
        heartbeatManager.clear();
    }

    /**
     * Removes a player from the <code>ServerGameCommands</code>
     *
     * @param nick The nickname of the player to remove.
     */
    public void removePlayer(@NotNull String nick) {
        ServerGameCommands.removePlayer(nick);
    }

    /**
     * Logs in a player.
     * <p>
     * The method adds the player to the {@link #playersRemote} list. The method creates a new
     * {@link ServerConnectorImpl} and {@link ServerChatConnectorImpl} object for the player.
     * </p>
     */
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
