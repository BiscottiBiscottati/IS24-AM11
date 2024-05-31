package it.polimi.ingsw.am11.network.socket.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public enum ContextJSON {
    GAME,
    EXCEPTION,
    CHAT,
    PING;

    public @NotNull ObjectNode createObjectNode() {
        ObjectNode json = JsonFactory.MAPPER.createObjectNode();
        json.put("context", this.toString());
        return json;
    }
}
