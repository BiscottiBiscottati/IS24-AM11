package it.polimi.ingsw.am11.network.RMI.remote.game;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerGameCommandsInterface extends Remote {

    void setStarterCard(@NotNull String nick, boolean isRetro)
    throws RemoteException, PlayerInitException, IllegalCardPlacingException, GameStatusException;

    void setObjectiveCard(@NotNull String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, PlayerInitException,
           GameStatusException;

    void placeCard(@NotNull String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException;

    void drawCard(@NotNull String nick, boolean fromVisible, @NotNull PlayableCardType type,
                  int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, PlayerInitException, EmptyDeckException,
           MaxHandSizeException, GameStatusException;

    void setNumOfPlayers(@NotNull String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException;
}
