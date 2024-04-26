package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loggable extends Remote {
    boolean login(String nick) throws RemoteException;

    void logout(String nick) throws RemoteException;

    boolean initGame() throws RemoteException;

    boolean goNextTurn() throws RemoteException;

    boolean addPlayer(String nickname) throws RemoteException;

    boolean removePlayer(String nickname) throws RemoteException;

    boolean forceEnd() throws RemoteException;

    boolean setObjFor(String nickname, int cardID) throws RemoteException;

    boolean placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws RemoteException;

    int drawCard(boolean fromVisible, PlayableCardType type, String nickname,
                 int cardID) throws RemoteException;

    boolean setStarterFor(String nickname, boolean isRetro) throws RemoteException;
}
