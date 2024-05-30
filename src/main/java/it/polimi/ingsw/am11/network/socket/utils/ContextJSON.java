package it.polimi.ingsw.am11.network.socket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public enum ContextJSON {
    GAME,
    EXCEPTION,
    CHAT,
    PING;

    static final ObjectMapper MAPPER = new ObjectMapper();

    public static JsonNode toJsonNode(@NotNull String message) throws JsonProcessingException {
        return MAPPER.readTree(message);
    }

    public @NotNull ObjectNode createObjectNode() {
        ObjectNode json = MAPPER.createObjectNode();
        json.put("context", this.toString());
        return json;
    }
}
