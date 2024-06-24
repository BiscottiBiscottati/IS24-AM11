package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.ServerNetworkManager;
import it.polimi.ingsw.am11.utils.ArgParser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Server {

    static void start(@NotNull ArgParser parser) {

        if (Objects.equals(parser.getOption("resume").orElseThrow().getValue(), "")) {
            CentralController.INSTANCE.loadMostRecent();
        } else CentralController.INSTANCE.createNewGame();

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
        ServerNetworkManager networkManager = new ServerNetworkManager(socketPort, rmiPort);
        networkManager.start();
        CentralController.INSTANCE.setNetworkManager(networkManager);
    }
}
