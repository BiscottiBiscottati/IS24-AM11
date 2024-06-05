package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatManager.class.getName());

    private final @NotNull Map<String, ServerChatConnector> chatConnectors;

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
        if (chatConnectors.isEmpty()) return;

        LOGGER.info("SERVER CHAT: Broadcasting message from {}: {}", sender, msg);
        chatConnectors.entrySet().stream()
                      .filter(entry -> ! entry.getKey().equals(sender))
                      .forEach(entry -> entry.getValue().sendPublicMsg(sender, msg));
        chatConnectors.get(sender).confirmSentMsg(sender, msg);
    }

    public void sendPrivateMessage(@NotNull String sender, @NotNull String recipient,
                                   @NotNull String msg) throws PlayerInitException {
        if (chatConnectors.isEmpty()) return;

        LOGGER.info("SERVER CHAT: Sending private message from {} to {}: {}", sender, recipient,
                    msg);
        chatConnectors.entrySet().stream()
                      .filter(entry -> entry.getKey().equals(recipient))
                      .findFirst()
                      .map(Map.Entry::getValue)
                      .orElseThrow(() -> new PlayerInitException(
                              "Player " + recipient + " not found to send private message."))
                      .sendPrivateMsg(sender, msg);
        chatConnectors.get(sender).confirmSentMsg(sender, msg);
    }

}
