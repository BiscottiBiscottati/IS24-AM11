package it.polimi.ingsw.am11.network.Socket.Server;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.ServerChatConnector;
import it.polimi.ingsw.am11.network.Socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.Socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class ServerChatSender implements ServerChatConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerChatSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.CHAT;

    private final PrintWriter out;

    public ServerChatSender(@NotNull PrintWriter out) {
        this.out = out;
    }

    @Override
    public void sendPublicMsg(@NotNull String sender, @NotNull String msg) {
        LOGGER.info("SERVER TCP: Public message to send: {}", msg);
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "sendPublicMsg");
        json.put("sender", sender);
        json.put("msg", msg);

        out.println(json);
    }

    @Override
    public void sendPrivateMsg(@NotNull String sender, @NotNull String recipient,
                               @NotNull String msg) {
        LOGGER.info("SERVER TCP: Private message from {} to {}: {}", sender, recipient, msg);
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "sendPrivateMsg");
        json.put("sender", sender);
        json.put("recipient", recipient);
        json.put("msg", msg);

        out.println(json);
    }
}
