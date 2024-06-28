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
 * The server uses this interface to log in the clients.
 */
public interface ServerLoggable extends Remote {

    /**
     * The client calls this method to log in to the server.
     *
     * @param nick            The nickname of the client.
     * @param remoteConnector The remote object used to send game updates.
     * @param remoteChat      The remote object used to send chat messages.
     * @throws RemoteException            If a remote error occurs.
     * @throws NumOfPlayersException      If the number of players is not correct.
     * @throws PlayerInitException        If an error occurs while initializing the player.
     * @throws NotSetNumOfPlayerException If the number of players has not been set.
     * @throws GameStatusException        If the game status is not correct.
     */
    void login(@NotNull String nick, @NotNull ClientGameUpdatesInterface remoteConnector,
               @NotNull ClientChatInterface remoteChat)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException;
}
