package it.polimi.ingsw.am11.network.socket.server.chat;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.ChatController;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.network.socket.server.game.ServerExceptionSender;
import org.jetbrains.annotations.NotNull;

public class ServerChatReceiver implements MessageReceiver {

    private final ChatController chatController;
    private final ServerExceptionSender exceptionSender;

    public ServerChatReceiver(@NotNull ServerExceptionSender exceptionSender) {
        this.chatController = CentralController.INSTANCE.getChatController();
        this.exceptionSender = exceptionSender;
    }

    @Override
    public void receive(@NotNull JsonNode jsonNode) {
        try {
            switch (jsonNode.get("method").asText()) {
                case "pubMsg" -> {
                    String sender = jsonNode.get("sender").asText();
                    String msg = jsonNode.get("msg").asText();
                    chatController.broadcastMessage(sender, msg);
                }
                case "pubPrivateMsg" -> {
                    String sender = jsonNode.get("sender").asText();
                    String recipient = jsonNode.get("recipient").asText();
                    String msg = jsonNode.get("msg").asText();
                    chatController.sendPrivateMessage(sender, recipient, msg);
                }
            }
        } catch (PlayerInitException e) {
            exceptionSender.exception(e);
        }
    }
}
