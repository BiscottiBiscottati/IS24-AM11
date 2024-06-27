package it.polimi.ingsw.am11.network.connector;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that defines the methods that a client chat connector must implement.
 * The client chat connector is used to send messages to the chat.
 * The chat is a separate service from the game, so the client must connect to it separately.
 * The chat is used to send messages to all the players in the game or to send private messages to a
 * specific player.
 */
public interface ClientChatConnector {
    void pubMsg(@NotNull String msg);

    void pubPrivateMsg(@NotNull String recipient, @NotNull String msg);
}
