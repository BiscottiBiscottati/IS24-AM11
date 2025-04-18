package it.polimi.ingsw.am11.network.socket.server.chat;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.ChatController;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.socket.MessageReceiver;
import it.polimi.ingsw.am11.network.socket.server.game.ServerExceptionSender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that receives the chat messages from the client and updates the chat
 * @see MessageReceiver
 * @see ServerExceptionSender
 * @see ChatController
 * @see CentralController
 */
public class ServerChatReceiver implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerChatReceiver.class);

    private final @NotNull ChatController chatController;
    private final @NotNull ServerExceptionSender exceptionSender;

    public ServerChatReceiver(@NotNull ServerExceptionSender exceptionSender) {
        this.chatController = CentralController.INSTANCE.getChatController();
        this.exceptionSender = exceptionSender;
    }

    /**
     * Receives the message from the client and updates the chat
     * <p>
     *     The method is called when a message is received from the client.
     *     The method switches on the method of the message and calls the appropriate method of the
     *     chat controller.
     *     The method logs the message received.
     *     The method catches the PlayerInitException and sends it to the exception sender.
     * </p>
     * @param jsonNode the message received from the client
     */
    @Override
    public void receive(@NotNull JsonNode jsonNode) {
        try {
            switch (jsonNode.get("method").asText()) {
                case "pubMsg" -> {
                    LOGGER.info("SERVER TCP: Public message received: {}",
                                jsonNode.get("msg").asText());
                    String sender = jsonNode.get("sender").asText();
                    String msg = jsonNode.get("msg").asText();
                    chatController.broadcastMessage(sender, msg);
                }
                case "pubPrivateMsg" -> {
                    LOGGER.info("SERVER TCP: Private message received: {}",
                                jsonNode.get("msg").asText());
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
