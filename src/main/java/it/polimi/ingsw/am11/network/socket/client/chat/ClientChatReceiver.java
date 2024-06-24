package it.polimi.ingsw.am11.network.socket.client.chat;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import org.jetbrains.annotations.NotNull;

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
        }
    }
}
