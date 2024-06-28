package it.polimi.ingsw.am11.network.RMI.remote;

import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the server to log in the clients.
 */
public interface ServerLoggable extends Remote {

    void login(@NotNull String nick, @NotNull ClientGameUpdatesInterface remoteConnector,
               @NotNull ClientChatInterface remoteChat)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException;
}
