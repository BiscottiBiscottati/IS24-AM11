package it.polimi.ingsw.am11.network.RMI.server.chat;

import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ServerChatInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public class ServerChatInterfaceImpl implements ServerChatInterface {
    @Override
    public void pubMsg(@NotNull String sender, @NotNull String msg) throws RemoteException {

    }

    @Override
    public void pubPrivateMsg(@NotNull String sender, @NotNull String recipient, String msg)
    throws RemoteException {

    }
}
