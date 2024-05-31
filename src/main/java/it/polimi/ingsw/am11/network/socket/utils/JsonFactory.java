package it.polimi.ingsw.am11.network.socket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public class JsonFactory {
    static final ObjectMapper MAPPER = new ObjectMapper();

    public static @NotNull ObjectNode createObjectNode(@NotNull ContextJSON context) {
        return context.createObjectNode();
    }

    public static JsonNode toJsonNode(@NotNull String message) throws JsonProcessingException {
        return MAPPER.readTree(message);
    }

    public static String writeValueAsString(@NotNull Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T readValue(@NotNull String content, @NotNull TypeReference<T> typeReference)
    throws JsonProcessingException {
        return MAPPER.readValue(content, typeReference);
    }
}
