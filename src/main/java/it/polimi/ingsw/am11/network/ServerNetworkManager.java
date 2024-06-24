package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.network.RMI.server.ServerRMI;
import it.polimi.ingsw.am11.network.socket.server.SocketManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkManager {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final SocketManager socketManager;
    private final ServerRMI serverRMI;

    public ServerNetworkManager(int socketPort, int rmiPort) {
        socketManager = new SocketManager(socketPort);
        serverRMI = new ServerRMI(rmiPort);
    }

    public void start() {
        executorService.submit(socketManager::start);
        serverRMI.start();
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }

    public void kickAllPlayers() {
        socketManager.removeClients();
        serverRMI.removeAllPlayers();
    }
}
