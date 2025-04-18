package it.polimi.ingsw.am11.network.socket.server.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

/**
 * The class that sends the game exceptions to the client It uses a {@link PrintWriter} to send the
 * messages to the client
 *
 * @param out the output stream
 */
public record ServerExceptionSender(@NotNull PrintWriter out) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.EXCEPTION;

    /**
     * Sends the exception to the client
     *
     * @param e the exception to send
     */
    public void exception(@NotNull Exception e) {
        LOGGER.info("SERVER TCP: Exception to send: {}", e.getClass().getSimpleName());
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", e.getClass().getSimpleName());
        json.put("description", e.getMessage());
        out.println(json);
    }
}
