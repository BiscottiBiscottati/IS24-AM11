package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public class ClientChatInterfaceImpl implements ClientChatInterface {
    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) throws RemoteException {

    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg)
    throws RemoteException {

    }
}
