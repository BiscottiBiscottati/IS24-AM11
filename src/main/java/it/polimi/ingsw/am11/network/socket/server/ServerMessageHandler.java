package it.polimi.ingsw.am11.network.socket.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Enums;
import it.polimi.ingsw.am11.network.socket.server.chat.ServerChatReceiver;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameReceiver;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for handling every type of message received by the server, it will then
 * dispatch the message to the correct receiver that will handle the message.
 */
public class ServerMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);

    private final @NotNull ServerGameReceiver messageReceiver;
    private final @NotNull ServerChatReceiver chatReceiver;
    private final @NotNull PingHandler pingHandler;

    public ServerMessageHandler(@NotNull ServerGameReceiver messageReceiver,
                                @NotNull PingHandler pingHandler,
                                @NotNull ServerChatReceiver chatReceiver) {
        this.messageReceiver = messageReceiver;
        this.pingHandler = pingHandler;
        this.chatReceiver = chatReceiver;
    }

    /**
     * This method is called when a message is received by the server, it will parse the message and
     * dispatch it to the correct receiver.
     *
     * @param message the message received by the server
     */
    public void receive(@NotNull String message) {
        JsonNode jsonNode;
        try {
            jsonNode = JsonFactory.toJsonNode(message);
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

    /**
     * Used to shut down the ping handler and consequently the connection with the client.
     */
    public void close() {
        pingHandler.close();
    }
}
