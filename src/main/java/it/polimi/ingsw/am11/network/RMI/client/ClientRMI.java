package it.polimi.ingsw.am11.network.RMI.client;

import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.client.chat.ClientChatConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.client.chat.ClientChatInterfaceImpl;
import it.polimi.ingsw.am11.network.RMI.client.game.ClientConnectorImpl;
import it.polimi.ingsw.am11.network.RMI.client.game.ClientGameUpdatesImpl;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.remote.heartbeat.HeartbeatInterface;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
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

public class ClientRMI implements ClientNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRMI.class);

    private final @NotNull ClientGameUpdatesInterface gameUpdatesInterface;
    private final @NotNull ClientChatInterface chatInterface;
    private final @NotNull ClientConnectorImpl clientConnectorImpl;
    private final @NotNull HeartbeatSender heartbeatSender;
    private final ClientChatConnectorImpl chatConnectorImpl;

    public ClientRMI(@NotNull String ip, int port, @NotNull ClientViewUpdater updater)
    throws RemoteException {

        ClientConnectorImpl.start();

        // Getting the registry
        Registry registry = LocateRegistry.getRegistry(ip, port);
        LOGGER.debug("CLIENT RMI: Registry located {}", registry.toString());
        // Creating the client remote interface
        ClientGameUpdatesImpl clientObject = new ClientGameUpdatesImpl(updater);
        gameUpdatesInterface = (ClientGameUpdatesInterface) UnicastRemoteObject.exportObject(
                clientObject, 0);
        ClientChatInterfaceImpl chatObject = new ClientChatInterfaceImpl(updater.getChatUpdater());
        chatInterface = (ClientChatInterface) UnicastRemoteObject.exportObject(
                chatObject, 0);

        chatConnectorImpl = new ClientChatConnectorImpl(registry);
        // Create the network connector
        clientConnectorImpl = new ClientConnectorImpl(this,
                                                      registry,
                                                      gameUpdatesInterface,
                                                      chatInterface,
                                                      chatConnectorImpl,
                                                      updater);
        // Check if the connection is working and create a heartbeat sender
        HeartbeatInterface ping;
        try {
            ping = (HeartbeatInterface) registry.lookup("ping");
        } catch (NotBoundException e) {
            throw new RemoteException(e.getMessage() + "not bound");
        }
        // Start the heartbeat sender
        heartbeatSender = new HeartbeatSender(ping, updater, this);
        HeartbeatSender.setHeartbeatInterval(ping.getInterval());
    }

    public @NotNull ClientGameConnector getGameConnector() {
        return clientConnectorImpl;
    }

    @Override
    public @NotNull ClientChatConnector getChatConnector() {
        return chatConnectorImpl;
    }

    @Override
    public void close() {
        heartbeatSender.stop();
        try {
            UnicastRemoteObject.unexportObject(gameUpdatesInterface, false);
            UnicastRemoteObject.unexportObject(chatInterface, false);
        } catch (NoSuchObjectException e) {
            LOGGER.debug("CLIENT RMI: No Connector to un-export");
        } finally {
            ClientConnectorImpl.stop();
            LOGGER.debug("CLIENT RMI: Client closed");
        }
    }

    public void setHeartbeatNickname(@NotNull String nickname) {
        heartbeatSender.setNickname(nickname);
    }

}
