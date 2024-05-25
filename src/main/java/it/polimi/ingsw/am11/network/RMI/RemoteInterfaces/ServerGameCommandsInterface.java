package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerGameCommandsInterface extends Remote {

    void setStarterCard(String nick, boolean isRetro)
    throws RemoteException, PlayerInitException, IllegalCardPlacingException, GameStatusException;

    void setObjectiveCard(String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, PlayerInitException,
           GameStatusException;

    void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException;

    void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, PlayerInitException, EmptyDeckException,
           MaxHandSizeException, GameStatusException;
}
