package it.polimi.ingsw.am11.view.client;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that defines the methods that will be called from the network side to notify the user
 * interface of a change in the chat.
 */
public interface ClientChatUpdater {
    /**
     * Receive a message from the chat
     *
     * @param sender the nickname of the sender
     * @param msg    the message
     */
    void receiveMsg(@NotNull String sender, @NotNull String msg);

    /**
     * Receive a private message from the chat
     *
     * @param sender the nickname of the sender
     * @param msg    the message
     */
    void receivePrivateMsg(@NotNull String sender, @NotNull String msg);

    /**
     * Confirm that the message has been sent
     *
     * @param sender the nickname of the sender, it should be the same as the player's nickname
     * @param msg    the message
     */
    void confirmSentMsg(@NotNull String sender, @NotNull String msg);
}
