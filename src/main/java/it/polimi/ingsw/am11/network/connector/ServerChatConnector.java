package it.polimi.ingsw.am11.network.connector;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that defines the methods that a server chat connector must implement. The server chat
 * connector is used to send messages to a specific client.
 */
public interface ServerChatConnector {

    /**
     * This method is used by the server to send a public message to a specific client.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     */
    void sendPublicMsg(@NotNull String sender, @NotNull String msg);

    /**
     * This method is used by the server to send a private message to a specific client.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     */
    void sendPrivateMsg(@NotNull String sender, @NotNull String msg);

    /**
     * This method is used by the server to confirm to the sender that the message he sent has been
     * sent.
     *
     * @param sender the sender of the message
     * @param msg    the message that has been sent
     */
    void confirmSentMsg(@NotNull String sender, @NotNull String msg);
}
