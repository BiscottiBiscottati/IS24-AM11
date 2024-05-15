package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;

import java.net.UnknownHostException;
import java.util.List;

//Its purpose is to effectively actuate the commands parsed by the reader
public class Actuator {
    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;

    public Actuator(TuiUpdater tuiUpdater) {
        this.tuiUpdater = tuiUpdater;
    }

    public static void close() {
        System.exit(0);
    }

    public void connect(List<String> positionalArgs)
    throws InvalidArgumetsException, UnknownHostException {
        if (positionalArgs.size() != 4) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        String type = positionalArgs.get(1);
        String ip = positionalArgs.get(2);
        int port;
        try {
            port = Integer.parseInt(positionalArgs.get(3));
        } catch (NumberFormatException e) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        switch (type) {
            case "rmi": {
                //TODO
                break;
            }
            case "socket": {
                ClientSocket clientSocket = new ClientSocket(ip, port, tuiUpdater);
                connector = clientSocket.getConnector();
                break;
            }
            default: {
                invalidArguments();
            }
        }
    }

    private static void invalidArguments() {
        System.out.println("Invalid arguments for this command, type help to get a list of all " +
                           "commands and arguments");
    }

    public void help() {

    }
}
