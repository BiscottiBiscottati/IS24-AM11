package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.utils.ArgParser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Server {
    static final CentralController centralController = CentralController.INSTANCE;

    static void start(@NotNull ArgParser parser) {
        int socketPort = 0;
        int rmiPort = 0;
        try {
            socketPort = Integer.parseInt(
                    Objects.requireNonNull(parser.getOption("socket").orElseThrow().getValue()));
            rmiPort = Integer.parseInt(
                    Objects.requireNonNull(parser.getOption("rmi").orElseThrow().getValue()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number " + e.getMessage().toLowerCase());
            System.exit(1);
        }
        SocketManager socketManager = new SocketManager(socketPort);
        Thread socketThread = new Thread(socketManager::start);

        //ServerMain serverMain = new ServerMain(rmiPort);
        //serverMain.start();

        socketThread.start();
    }
}
