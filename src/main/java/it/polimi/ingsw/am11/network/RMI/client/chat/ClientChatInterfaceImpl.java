package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.view.client.ClientChatUpdater;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

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
}
