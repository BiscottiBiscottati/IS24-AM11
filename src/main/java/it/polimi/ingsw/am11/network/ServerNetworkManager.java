package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.network.RMI.server.ServerRMI;
import it.polimi.ingsw.am11.network.socket.server.SocketManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is used to manage the network connections of the server.
 * <p>
 * It starts the socket manager and the RMI server. It also provides a method to kick all the
 * players from the server.
 * </p>
 */
public class ServerNetworkManager {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final SocketManager socketManager;
    private final ServerRMI serverRMI;

    public ServerNetworkManager(int socketPort, int rmiPort) {
        socketManager = new SocketManager(socketPort);
        serverRMI = new ServerRMI(rmiPort);
    }

    /**
     * Starts the socket and RMI connections for the server
     */
    public void start() {
        executorService.submit(socketManager::start);
        serverRMI.start();
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }

    /**
     * Kicks all the players from the server
     */
    public void kickAllPlayers() {
        socketManager.removeClients();
        serverRMI.removeAllPlayers();
    }
}
