package it.polimi.ingsw.am11.network.RMI.RemoteInterfaces;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface ConnectorInterface extends Remote {

    void updateHand(int cardId, boolean removeMode) throws RemoteException;

    void updatePersonalObjective(int cardId, boolean removeMode) throws RemoteException;

    void receiveStarterCard(int cardId) throws RemoteException;

    void receiveCandidateObjective(Set<Integer> cardsId) throws RemoteException;

    void updateDeckTop(PlayableCardType type, Color color) throws RemoteException;

    void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                     boolean removeMode) throws RemoteException;

    void updateShownPlayable(Integer previousId, Integer currentId) throws RemoteException;

    void updateTurnChange(String nickname) throws RemoteException;

    void updatePlayerPoint(String nickname, int points) throws RemoteException;

    void updateGameStatus(GameStatus status) throws RemoteException;

    void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) throws RemoteException;

    void updateCommonObjective(Set<Integer> cardID, boolean removeMode) throws RemoteException;

    void updatePlayers(Map<PlayerColor, String> currentPlayers) throws RemoteException;

    void notifyGodPlayer() throws RemoteException;
}
