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

/**
 * The handler of the messages received from the server by the client socket connection. It is
 * responsible for dispatching the messages to the correct receiver.
 *
 * @param messageReceiver   The receiver of the game messages from the server
 * @param exceptionReceiver The receiver of the exceptions from the server
 * @param pongHandler       The handler of the pong messages
 * @param chatReceiver      The receiver of the chat messages from the server
 */
public record ClientMessageHandler(@NotNull ClientGameReceiver messageReceiver,
                                   @NotNull ClientExceptionReceiver exceptionReceiver,
                                   @NotNull PongHandler pongHandler,
                                   @NotNull ClientChatReceiver chatReceiver) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    /**
     * This method is called when a message is received, it will parse the message and dispatch it
     * to the correct receiver.
     *
     * @param message the message received by the server
     */
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

    /**
     * Closes the pong handler
     */
    public void close() {
        pongHandler.close();
    }
}
