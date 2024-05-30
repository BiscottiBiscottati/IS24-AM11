package it.polimi.ingsw.am11.network.RMI.remote;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.RMI.remote.game.ClientGameUpdatesInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerLoggable extends Remote {

    void login(@NotNull String nick, @NotNull ClientGameUpdatesInterface connector)
    throws RemoteException, NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException;

    default void setNumOfPlayers(@NotNull String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
    }

}
