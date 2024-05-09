package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerViewInterface extends Remote {

    void setStarterCard(String nick, boolean isRetro)
    throws RemoteException;

    void setObjectiveCard(String nick, int cardId)
    throws RemoteException;

    void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException;

    void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException;

    void setNumofPlayers(String nick, int numOfPlayers) throws RemoteException;
}
