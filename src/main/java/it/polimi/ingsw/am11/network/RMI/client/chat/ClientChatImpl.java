package it.polimi.ingsw.am11.network.RMI.client.chat;

import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ClientChatInterface;
import org.jetbrains.annotations.NotNull;

public class ClientChatImpl implements ClientChatInterface {
    @Override
    public void receiveMsg(@NotNull String sender, @NotNull String msg) {

    }

    @Override
    public void receivePrivateMsg(@NotNull String sender, @NotNull String msg) {

    }
}
