package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

/**
 * Implementation of the {@link ClientChatInterface} interface for RMI connections. This class is
 * responsible for receiving messages from the server for chat purposes.
 *
 * @param chatUpdater The {@link ClientChatUpdater} object that will be used to update the client's
 *                    chat.
 */
public record ClientChatInterfaceImpl(@NotNull ClientChatUpdater chatUpdater)
        implements ClientChatInterface {

    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) throws RemoteException {
        chatUpdater.receiveMsg(sender, msg);
    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg)
    throws RemoteException {
        chatUpdater.receivePrivateMsg(sender, msg);
    }

    @Override
    public void receiveConfirmation(@NotNull String sender, @NotNull String msg)
    throws RemoteException {
        chatUpdater.confirmSentMsg(sender, msg);
    }
}
