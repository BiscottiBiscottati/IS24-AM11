package it.polimi.ingsw.am11.network.connector;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that defines the methods that a server chat connector must implement.
 * The server chat connector is used to send messages to the clients in the chat.
 */
public interface ServerChatConnector {

    void sendPublicMsg(@NotNull String sender, @NotNull String msg);

    void sendPrivateMsg(@NotNull String sender, @NotNull String msg);

    /**
     * This method is called when a message is sent to the chat
     * and the server needs to confirm that the message has been sent.
     * @param sender the sender of the message
     * @param msg the message that has been sent
     */
    void confirmSentMsg(@NotNull String sender, @NotNull String msg);
}
