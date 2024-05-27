package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.HeartbeatInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientRMI implements ClientNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRMI.class);

    private final ExecutorService commandExecutor;
    private final Registry registry;
    private final ClientGameUpdatesInterface gameUpdatesInterface;
    private final NetworkConnector networkConnector;
    private final ScheduledExecutorService heartbeatsService;
    private final HeartbeatSender heartbeatSender;

    public ClientRMI(@NotNull String ip, int port, @NotNull ClientViewUpdater updater)
    throws RemoteException {

        commandExecutor = Executors.newSingleThreadExecutor();
        heartbeatsService = Executors.newSingleThreadScheduledExecutor();

        // Getting the registry
        registry = LocateRegistry.getRegistry(ip, port);
        LOGGER.debug("CLIENT RMI: Registry located {}", registry.toString());
        // Creating the client remote interface
        ClientGameUpdatesImpl clientObject = new ClientGameUpdatesImpl(updater);
        gameUpdatesInterface = (ClientGameUpdatesInterface) UnicastRemoteObject.exportObject(
                clientObject, 0);
        // Create the network connector
        networkConnector = new NetworkConnector(this,
                                                registry,
                                                gameUpdatesInterface,
                                                updater.getExceptionConnector());
        // Check if the connection is working and create a heartbeat sender
        HeartbeatInterface ping;
        try {
            ping = (HeartbeatInterface) registry.lookup("ping");
        } catch (NotBoundException e) {
            throw new RemoteException(e.getMessage() + "not bound");
        }
        // Start the heartbeat sender
        heartbeatSender = new HeartbeatSender(ping, updater, this);
        heartbeatsService.scheduleAtFixedRate(heartbeatSender,
                                              ping.getInterval(),
                                              ping.getInterval(),
                                              TimeUnit.MILLISECONDS);
    }

    public ClientGameConnector getGameUpdatesInterface() {
        return networkConnector;
    }

    @Override
    public void close() {
        try {
            heartbeatSender.stop();
            commandExecutor.shutdown();
            heartbeatsService.shutdown();
            UnicastRemoteObject.unexportObject(gameUpdatesInterface, false);
        } catch (NoSuchObjectException e) {
            LOGGER.debug("CLIENT RMI: No Connector to un-export");
        }
        LOGGER.debug("CLIENT RMI: Client closed");
    }

    public void setHeartbeatNickname(@NotNull String nickname) {
        heartbeatSender.setNickname(nickname);
    }

}
