package it.polimi.ingsw.am11.network.RMI.server.chat;

import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.jetbrains.annotations.NotNull;

public class ServerChatConnectorImpl implements ServerChatConnector {

    @Override
    public void sendPublicMsg(@NotNull String sender, @NotNull String msg) {

    }

    @Override
    public void sendPrivateMsg(@NotNull String sender, @NotNull String recipient,
                               @NotNull String msg) {

    }
}
