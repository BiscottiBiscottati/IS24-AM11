package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.network.RMI.client.ClientRMI;
import it.polimi.ingsw.am11.network.socket.client.ClientSocket;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public enum ConnectionType {
    RMI("rmi", ClientRMI::new),
    SOCKET("socket", ClientSocket::new);

    private final String type;
    private final ConnectionFactory connectionFactory;

    ConnectionType(String type, ConnectionFactory connectionFactory) {
        this.type = type;
        this.connectionFactory = connectionFactory;
    }

    public static @NotNull Optional<ConnectionType> fromString(@NotNull String type) {
        return Stream.of(ConnectionType.values())
                     .filter(connectionType -> connectionType.type.equals(type))
                     .findFirst();
    }

    public @NotNull ClientNetworkHandler create(@NotNull String ip, int port,
                                                @NotNull ClientViewUpdater updater)
    throws Exception {
        return connectionFactory.create(ip, port, updater);
    }
}
