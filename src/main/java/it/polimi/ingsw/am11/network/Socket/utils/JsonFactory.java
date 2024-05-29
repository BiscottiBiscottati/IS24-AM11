package it.polimi.ingsw.am11.network.Socket.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public class JsonFactory {
    public static @NotNull ObjectNode createObjectNode(@NotNull ObjectMapper mapper,
                                                       @NotNull ContextJSON context) {
        return context.createObjectNode(mapper);
    }
}
