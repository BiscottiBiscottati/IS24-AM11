package it.polimi.ingsw.am11.network.socket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Enums;
import it.polimi.ingsw.am11.network.socket.client.chat.ClientChatReceiver;
import it.polimi.ingsw.am11.network.socket.client.game.ClientExceptionReceiver;
import it.polimi.ingsw.am11.network.socket.client.game.ClientGameReceiver;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    private final ClientGameReceiver messageReceiver;
    private final ClientExceptionReceiver exceptionReceiver;
    private final PongHandler pongHandler;
    private final ClientChatReceiver chatReceiver;
    private final ObjectMapper mapper;

    public ClientMessageHandler(@NotNull ClientGameReceiver messageReceiver,
                                @NotNull ClientExceptionReceiver exceptionReceiver,
                                @NotNull PongHandler pongHandler,
                                @NotNull ClientChatReceiver chatReceiver) {
        this.messageReceiver = messageReceiver;
        this.exceptionReceiver = exceptionReceiver;
        this.pongHandler = pongHandler;
        this.chatReceiver = chatReceiver;
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
            case CHAT -> chatReceiver.receive(jsonNode);
            case null, default -> {}
        }
    }

    public void close() {
        pongHandler.close();
    }
}
