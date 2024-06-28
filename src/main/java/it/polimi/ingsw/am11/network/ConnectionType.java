package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.network.RMI.client.ClientRMI;
import it.polimi.ingsw.am11.network.socket.client.ClientSocket;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This enum is used to select the type of connection to use.
 * <p>
 * Each connection type has a string representation and a factory to create the connection. The
 * factory is used to create the connection to the server. The string representation is used to
 * select the connection type from the UI.
 * </p>
 */
public enum ConnectionType {
    RMI("rmi", ClientRMI::new),
    SOCKET("socket", ClientSocket::new);

    private final String type;
    private final ConnectionFactory connectionFactory;

    ConnectionType(String type, ConnectionFactory connectionFactory) {
        this.type = type;
        this.connectionFactory = connectionFactory;
    }

    /**
     * Returns an Optional of ConnectionType by giving the name of the type.
     *
     * @param type the name of the type.
     * @return an Optional of ConnectionType.
     */
    public static @NotNull Optional<ConnectionType> fromString(@NotNull String type) {
        return Stream.of(ConnectionType.values())
                     .filter(connectionType -> connectionType.type.equals(type))
                     .findFirst();
    }

    /**
     * Creates a ClientNetworkHandler to connect to the server.
     *
     * @param ip      the server ip
     * @param port    the server port
     * @param updater the updater to notify the UI
     * @return a new ClientNetworkHandler
     * @throws Exception if an error occurs
     */
    public @NotNull ClientNetworkHandler create(@NotNull String ip, int port,
                                                @NotNull ClientViewUpdater updater)
    throws Exception {
        return connectionFactory.create(ip, port, updater);
    }
}
