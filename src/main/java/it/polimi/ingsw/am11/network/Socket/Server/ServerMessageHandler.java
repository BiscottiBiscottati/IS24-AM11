package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Enums;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);

    private final ServerGameReceiver messageReceiver;
    private final ServerChatReceiver chatReceiver;
    private final PingHandler pingHandler;

    public ServerMessageHandler(@NotNull ServerGameReceiver messageReceiver,
                                @NotNull PingHandler pingHandler,
                                @NotNull ServerChatReceiver chatReceiver) {
        this.messageReceiver = messageReceiver;
        this.pingHandler = pingHandler;
        this.chatReceiver = chatReceiver;
    }

    public void receive(@NotNull String message) {
        JsonNode jsonNode;
        try {
            jsonNode = ContextJSON.toJsonNode(message);
        } catch (JsonProcessingException e) {
            LOGGER.info("SERVER TCP: Error while parsing json: {}", e.getMessage());
            return;
        }
        ContextJSON context =
                Enums.getIfPresent(ContextJSON.class, jsonNode.get("context").asText())
                     .orNull();

        switch (context) {
            case PING -> pingHandler.ping();
            case GAME -> messageReceiver.receive(jsonNode);
            case CHAT -> chatReceiver.receive(jsonNode);
            case null, default -> LOGGER.debug("SERVER TCP: Invalid json message!");
        }
    }

    public void close() {
        pingHandler.close();
    }
}
