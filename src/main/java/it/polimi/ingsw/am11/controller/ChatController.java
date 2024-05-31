package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.chat.ChatManager;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.jetbrains.annotations.NotNull;

public class ChatController {
    private final ChatManager chatModel;

    ChatController() {
        chatModel = new ChatManager();
    }

    public void addPlayer(@NotNull String nickname, @NotNull ServerChatConnector chatConnector) {
        chatModel.addPlayer(nickname, chatConnector);
    }

    public void removePlayer(@NotNull String nickname) {
        chatModel.removePlayer(nickname);
    }

    public void clear() {
        chatModel.clear();
    }

    public void broadcastMessage(@NotNull String sender, @NotNull String msg) {
        chatModel.broadcastMessage(sender, msg);
    }

    public void sendPrivateMessage(@NotNull String sender, @NotNull String recipient,
                                   @NotNull String msg) throws PlayerInitException {
        chatModel.sendPrivateMessage(sender, recipient, msg);
    }
}
