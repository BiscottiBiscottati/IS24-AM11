package it.polimi.ingsw.am11.view.client;

import org.jetbrains.annotations.NotNull;

public interface ClientChatUpdater {
    void receiveMsg(@NotNull String sender, @NotNull String msg);

    void receivePrivateMsg(@NotNull String sender, @NotNull String msg);

    void confirmSentMsg(@NotNull String sender, @NotNull String msg);
}
