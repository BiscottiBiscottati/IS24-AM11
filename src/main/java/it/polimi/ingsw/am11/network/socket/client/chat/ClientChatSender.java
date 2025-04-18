package it.polimi.ingsw.am11.network.socket.client.chat;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

/**
 * The class that sends the chat messages to the server
 * @see ClientChatConnector
 * @see ContextJSON
 */
public class ClientChatSender implements ClientChatConnector {
    private static final ContextJSON CONTEXT = ContextJSON.CHAT;

    private final @NotNull PrintWriter out;
    private String nickname;

    public ClientChatSender(@NotNull PrintWriter out) {
        this.out = out;
    }

    public void setNickname(@NotNull String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void pubMsg(@NotNull String msg) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "pubMsg");
        json.put("sender", nickname);
        json.put("msg", msg);

        out.println(json);
    }

    @Override
    public void pubPrivateMsg(@NotNull String recipient, @NotNull String msg) {
        ObjectNode json = JsonFactory.createObjectNode(CONTEXT);
        json.put("method", "pubPrivateMsg");
        json.put("sender", nickname);
        json.put("recipient", recipient);
        json.put("msg", msg);

        out.println(json);
    }
}
