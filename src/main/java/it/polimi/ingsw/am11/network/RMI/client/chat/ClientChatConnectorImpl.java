package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ServerChatInterface;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * Implementation of the {@link ClientChatConnector} interface for RMI connections.
 * This class is responsible for sending messages to the server for chat purposes.
 * <p>
 *     The class is instantiated with a {@link Registry} object, which is used to lookup the server's remote object.
 *     The sender's name is set using the {@link #setSender(String)} method.
 *     The class provides two methods to send messages to the server:
 * </p>
 */
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


    /**
     * Sends a public message to the server.
     * @param msg The message to send to the server.
     */
    @Override
    public void pubMsg(@NotNull String msg) {
        try {
            if (sender == null) return;
            LOGGER.debug("CLIENT RMI: Sending public message: {}", msg);
            ((ServerChatInterface) registry.lookup("chat"))
                    .pubMsg(sender, msg);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.debug("CLIENT RMI: Error sending public message: {}", e.getMessage());
        }
    }

    /**
     * Sends a private message to the server.
     * @param recipient The recipient of the message (the user's name).
     * @param msg The message to send to the server.
     */
    @Override
    public void pubPrivateMsg(@NotNull String recipient, @NotNull String msg) {
        try {
            if (sender == null) return;
            LOGGER.debug("CLIENT RMI: Sending private message to {}: {}", recipient, msg);
            ((ServerChatInterface) registry.lookup("chat"))
                    .pubPrivateMsg(sender, recipient, msg);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.debug("CLIENT RMI: Error sending private message: {}", e.getMessage());
        }
    }

}
