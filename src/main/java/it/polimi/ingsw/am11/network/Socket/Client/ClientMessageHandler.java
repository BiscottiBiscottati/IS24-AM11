package it.polimi.ingsw.am11.network.Socket.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Enums;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    private final ClientMessageReceiver messageReceiver;
    private final ClientExceptionReceiver exceptionReceiver;
    private final PongHandler pongHandler;
    private final ObjectMapper mapper;

    public ClientMessageHandler(@NotNull ClientMessageReceiver messageReceiver,
                                @NotNull ClientExceptionReceiver exceptionReceiver,
                                @NotNull PongHandler pongHandler) {
        this.messageReceiver = messageReceiver;
        this.exceptionReceiver = exceptionReceiver;
        this.pongHandler = pongHandler;
        this.mapper = new ObjectMapper();
    }

    public void receive(@NotNull String message) {
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(message);
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
            case null, default -> {}
        }
    }

    public void close() {
        pongHandler.close();
    }
}
