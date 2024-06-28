package it.polimi.ingsw.am11.network.socket.client.chat;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import org.jetbrains.annotations.NotNull;

/**
 * The class that receives the chat messages from the server and updates the chat
 * @see MessageReceiver
 * @see ClientChatUpdater
 */
public record ClientChatReceiver(@NotNull ClientChatUpdater chatUpdater)
        implements MessageReceiver {

    @Override
    public void receive(@NotNull JsonNode jsonNode) {
        // Switch on the method
        switch (jsonNode.get("method").asText()) {
            case "sendPublicMsg" -> chatUpdater.receiveMsg(jsonNode.get("sender").asText(),
                                                           jsonNode.get("msg").asText());
            case "sendPrivateMsg" -> chatUpdater.receivePrivateMsg(jsonNode.get("sender").asText(),
                                                                   jsonNode.get("msg").asText());
            case "confirmSentMsg" -> chatUpdater.confirmSentMsg(jsonNode.get("sender").asText(),
                                                                jsonNode.get("msg").asText());
        }
    }
}
