package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;

public class Server {
    static final int DEFAULT_SOCKET_PORT = 12345;
    static final int DEFAULT_RMI_PORT = 12346;
    static final CentralController centralController = CentralController.INSTANCE;

    static void start() {
        new SocketManager(DEFAULT_SOCKET_PORT).start();
    }
}
