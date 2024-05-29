package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.Socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class ServerExceptionSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExceptionSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.EXCEPTION;

    private final @NotNull PrintWriter out;
    private final @NotNull ObjectMapper mapper;

    public ServerExceptionSender(@NotNull PrintWriter out) {
        this.out = out;
        this.mapper = new ObjectMapper();
    }

    public void exception(@NotNull Exception e) {
        LOGGER.info("SERVER TCP: Exception to send: {}", e.getClass().getSimpleName());
        ObjectNode json = JsonFactory.createObjectNode(mapper, CONTEXT);
        json.put("method", e.getClass().getSimpleName());
        json.put("description", e.getMessage());
        out.println(json);
    }
}
