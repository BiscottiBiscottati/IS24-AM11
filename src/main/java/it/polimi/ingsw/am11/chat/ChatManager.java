package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private final Map<String, ServerChatConnector> chatConnectors;

    public ChatManager() {
        this.chatConnectors = new ConcurrentHashMap<>(8);
    }

    public void addPlayer(@NotNull String nickname, @NotNull ServerChatConnector connector) {
        this.chatConnectors.put(nickname, connector);
    }

    public void removePlayer(@NotNull String nickname) {
        this.chatConnectors.remove(nickname);
    }

    public void clear() {
        this.chatConnectors.clear();
    }

    public void broadcastMessage(@NotNull String sender, @NotNull String msg) {
        chatConnectors.forEach((nickname, connector) -> {
            if (! nickname.equals(sender)) {
                connector.sendPublicMsg(sender, msg);
            }
        });
    }

    public void sendPrivateMessage(@NotNull String sender, @NotNull String recipient,
                                   @NotNull String msg) throws PlayerInitException {
        if (chatConnectors.containsKey(recipient))
            chatConnectors.get(recipient)
                          .sendPrivateMsg(sender, recipient, msg);
        else throw new PlayerInitException(
                "Player " + recipient + " not found to send private message.");
    }

}
