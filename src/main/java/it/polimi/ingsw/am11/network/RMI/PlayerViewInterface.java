package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerViewInterface extends Remote {

    void setStarterCard(boolean isRetro)
    throws PlayerInitException, IllegalCardPlacingException, GameStatusException, RemoteException;

    void setObjectiveCard(int cardId)
    throws IllegalPlayerSpaceActionException, PlayerInitException, GameStatusException,
           RemoteException;

    void placeCard(int cardId, int x, int y, boolean isRetro)
    throws TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException, RemoteException;

    void drawCard(boolean fromVisible, PlayableCardType type, int cardId)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, EmptyDeckException, MaxHandSizeException, GameStatusException,
           RemoteException;

    void setNumofPlayers(String nick, int numOfPlayers) throws RemoteException;
}
