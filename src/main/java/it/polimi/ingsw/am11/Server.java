package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.utils.ArgParser;
import org.jetbrains.annotations.NotNull;

public class Server {
    static final CentralController centralController = CentralController.INSTANCE;

    static void start(@NotNull ArgParser parser) {
        int socketPort = 0;
        int rmiPort = 0;
        try {
            socketPort = Integer.parseInt(parser.getOption("socket").orElseThrow().getValue());
            rmiPort = Integer.parseInt(parser.getOption("rmi").orElseThrow().getValue());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number " + e.getMessage().toLowerCase());
            System.exit(1);
        }
        new SocketManager(socketPort).start();
        // TODO rmi start
    }
}
