package it.polimi.ingsw.am11.network.socket.server.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public record ServerExceptionSender(@NotNull PrintWriter out) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.EXCEPTION;

    public void exception(@NotNull Exception e) {
        LOGGER.info("SERVER TCP: Exception to send: {}", e.getClass().getSimpleName());
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", e.getClass().getSimpleName());
        json.put("description", e.getMessage());
        out.println(json);
    }
}
