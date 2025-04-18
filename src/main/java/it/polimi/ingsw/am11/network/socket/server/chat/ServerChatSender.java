package it.polimi.ingsw.am11.network.socket.server.chat;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

/**
 * The class that sends the chat messages to the clients
 * @see ServerChatConnector
 * @see ContextJSON
 * @param out the output stream
 */
public record ServerChatSender(@NotNull PrintWriter out) implements ServerChatConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerChatSender.class);
    private static final ContextJSON CONTEXT = ContextJSON.CHAT;

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
    public void sendPrivateMsg(@NotNull String sender,
                               @NotNull String msg) {
        LOGGER.info("SERVER TCP: Private message sent from {}: {}", sender, msg);
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "sendPrivateMsg");
        json.put("sender", sender);
        json.put("msg", msg);

        out.println(json);
    }

    @Override
    public void confirmSentMsg(@NotNull String sender, @NotNull String msg) {
        LOGGER.info("SERVER TCP: Message sent confirmed: {}", msg);
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "confirmSentMsg");
        json.put("sender", sender);
        json.put("msg", msg);

        out.println(json);
    }
}
