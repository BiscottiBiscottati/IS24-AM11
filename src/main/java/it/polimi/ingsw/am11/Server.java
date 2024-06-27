package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.ServerNetworkManager;
import it.polimi.ingsw.am11.utils.ArgParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * The main class for the server.
 */
public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * The main method to start the server, it will open a socket and an RMI connection with the
     * passed ports.
     *
     * @param parser The parser containing the arguments for the server.
     */
    static void start(@NotNull ArgParser parser) {

        if (Objects.equals(parser.getOption("resume").orElseThrow().getValue(), "")) {
            CentralController.INSTANCE.loadMostRecent();
        } else CentralController.INSTANCE.createNewGame();

        try {
            LOGGER.info("SERVER: Starting server, local ip4 address: {}",
                        InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOGGER.error("SERVER: Error while getting local ip4 address: {}", e.getMessage());
        }

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
