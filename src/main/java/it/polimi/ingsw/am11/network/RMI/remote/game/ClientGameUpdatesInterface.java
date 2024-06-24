package it.polimi.ingsw.am11.network.RMI.remote.game;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public interface ClientGameUpdatesInterface extends Remote {

    void updateHand(int cardId, boolean removeMode) throws RemoteException;

    void updatePersonalObjective(int cardId, boolean removeMode) throws RemoteException;

    void receiveStarterCard(int cardId) throws RemoteException;

    void receiveCandidateObjective(@NotNull Set<Integer> cardsId) throws RemoteException;

    void updateDeckTop(@NotNull PlayableCardType type, @NotNull Color color) throws RemoteException;

    void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro)
    throws RemoteException;

    void updateShownPlayable(@Nullable Integer previousId, @Nullable Integer currentId)
    throws RemoteException;

    void updateTurnChange(@NotNull String nickname) throws RemoteException;

    void updatePlayerPoint(@NotNull String nickname, int points) throws RemoteException;

    void updateGameStatus(@NotNull GameStatus status) throws RemoteException;

    void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard)
    throws RemoteException;

    void updateCommonObjective(@NotNull Set<Integer> cardID, boolean removeMode)
    throws RemoteException;

    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers)
    throws RemoteException;

    void notifyGodPlayer() throws RemoteException;

    void updateNumOfPlayers(int numberOfPlayers) throws RemoteException;

    void sendReconnection(@NotNull ReconnectionModelMemento memento) throws RemoteException;

    void youUgly();
}
