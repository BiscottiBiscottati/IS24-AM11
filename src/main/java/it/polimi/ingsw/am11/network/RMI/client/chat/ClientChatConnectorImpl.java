package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ServerChatInterface;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public final class ClientChatConnectorImpl
        implements ClientChatConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientChatConnectorImpl.class);
    private final @NotNull Registry registry;
    private String sender;

    public ClientChatConnectorImpl(@NotNull Registry registry) {
        this.registry = registry;
    }

    public void setSender(@NotNull String sender) {
        this.sender = sender;
    }


    @Override
    public void pubMsg(@NotNull String msg) {
        try {
            if (sender == null) return;
            ((ServerChatInterface) registry.lookup("chat"))
                    .pubMsg(sender, msg);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.debug("CLIENT RMI: Error sending public message: {}", e.getMessage());
        }
    }

    @Override
    public void pubPrivateMsg(@NotNull String recipient, @NotNull String msg) {
        try {
            if (sender == null) return;
            ((ServerChatInterface) registry.lookup("chat"))
                    .pubPrivateMsg(sender, recipient, msg);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.debug("CLIENT RMI: Error sending private message: {}", e.getMessage());
        }
    }

}
