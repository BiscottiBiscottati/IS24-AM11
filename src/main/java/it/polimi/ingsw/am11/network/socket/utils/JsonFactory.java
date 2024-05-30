package it.polimi.ingsw.am11.network.socket.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public class JsonFactory {
    public static @NotNull ObjectNode createObjectNode(@NotNull ContextJSON context) {
        return context.createObjectNode();
    }
}
