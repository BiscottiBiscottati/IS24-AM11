package it.polimi.ingsw.am11.network.socket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Enums;
import it.polimi.ingsw.am11.network.socket.client.chat.ClientChatReceiver;
import it.polimi.ingsw.am11.network.socket.client.game.ClientExceptionReceiver;
import it.polimi.ingsw.am11.network.socket.client.game.ClientGameReceiver;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ClientMessageHandler(@NotNull ClientGameReceiver messageReceiver,
                                   @NotNull ClientExceptionReceiver exceptionReceiver,
                                   @NotNull PongHandler pongHandler,
                                   @NotNull ClientChatReceiver chatReceiver) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    public void receive(@NotNull String message) {
        JsonNode jsonNode;
        try {
            jsonNode = JsonFactory.toJsonNode(message);
        } catch (JsonProcessingException e) {
            LOGGER.info("CLIENT TCP: Error while parsing json: {}", e.getMessage());
            return;
        }
        ContextJSON context =
                Enums.getIfPresent(ContextJSON.class, jsonNode.get("context").asText())
                     .orNull();

        switch (context) {
            case PING -> pongHandler.pong();
            case GAME -> messageReceiver.receive(jsonNode);
            case EXCEPTION -> exceptionReceiver.receive(jsonNode);
            case CHAT -> chatReceiver.receive(jsonNode);
            case null, default -> {}
        }
    }

    public void close() {
        pongHandler.close();
    }
}
