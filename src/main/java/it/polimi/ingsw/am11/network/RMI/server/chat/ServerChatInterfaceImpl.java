package it.polimi.ingsw.am11.network.RMI.server.chat;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.ChatController;
import it.polimi.ingsw.am11.model.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ServerChatInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

/**
 * Implementation of the {@link ServerChatInterface} interface for RMI connections.
 * This class is responsible for receiving messages from the clients for chat purposes.
 */
public class ServerChatInterfaceImpl implements ServerChatInterface {
    private final @NotNull ChatController chatController;

    public ServerChatInterfaceImpl() {
        this.chatController = CentralController.INSTANCE.getChatController();
    }

    @Override
    public void pubMsg(@NotNull String sender, @NotNull String msg) throws RemoteException {
        chatController.broadcastMessage(sender, msg);
    }

    @Override
    public void pubPrivateMsg(@NotNull String sender, @NotNull String recipient,
                              @NotNull String msg)
    throws RemoteException {
        try {
            chatController.sendPrivateMessage(sender, recipient, msg);
        } catch (PlayerInitException e) {
            throw new GameBreakingException(e);
        }
    }
}
