package it.polimi.ingsw.am11.network.Socket.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public enum ContextJSON {
    GAME,
    EXCEPTION,
    CHAT,
    PING;

    public @NotNull ObjectNode createObjectNode(@NotNull ObjectMapper mapper) {
        ObjectNode json = mapper.createObjectNode();
        json.put("context", this.toString());
        return json;
    }
}
