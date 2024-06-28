package it.polimi.ingsw.am11.network.RMI.remote.game;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
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

/**
 * This interface is used by the client to receive updates from the server.
 */

public interface ClientGameUpdatesInterface extends Remote {

    /**
     * Updates the hand of the player
     *
     * @param cardId     The card id
     * @param removeMode The remove mode
     * @throws RemoteException if the remote operation fails
     */
    void updateHand(int cardId, boolean removeMode) throws RemoteException;

    /**
     * Update the personal objective of the player
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     * @throws RemoteException if the remote operation fails
     */
    void updatePersonalObjective(int cardId, boolean removeMode) throws RemoteException;

    /**
     * Receive the starter card of the player
     *
     * @param cardId the id of the card
     * @throws RemoteException if the remote operation fails
     */
    void receiveStarterCard(int cardId) throws RemoteException;

    /**
     * Receive the candidate objectives of the player
     *
     * @param cardsId Set of card id
     * @throws RemoteException if the remote operation fails
     */
    void receiveCandidateObjective(@NotNull Set<Integer> cardsId) throws RemoteException;

    /**
     * Update the deck top card
     *
     * @param type  the type of the deck
     * @param color the color of the card
     * @throws RemoteException if the remote operation fails
     */
    void updateDeckTop(@NotNull PlayableCardType type, @Nullable GameColor color)
    throws RemoteException;

    /**
     * Update the field of the player, it can place or remove a card, if the removeMode is true the
     * cardId and isRetro are ignored.
     *
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the card
     * @param y        the y coordinate of the card
     * @param cardId   the id of the card
     * @param isRetro  if the card is placed on it's retro
     * @throws RemoteException if the remote operation fails
     */
    void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro)
    throws RemoteException;

    /**
     * Update the visible cards on the table, it will remove the previousId and add the currentId
     *
     * @param previousId the id of the card to remove
     * @param currentId  the id of the card to add
     * @throws RemoteException if the remote operation fails
     */
    void updateShownPlayable(@Nullable Integer previousId, @Nullable Integer currentId)
    throws RemoteException;

    /**
     * Update the current turn of the game
     *
     * @param nickname the nickname of the player that has the turn
     * @throws RemoteException if the remote operation fails
     */
    void updateTurnChange(@NotNull String nickname) throws RemoteException;

    /**
     * Update the points of a player
     *
     * @param nickname the nickname of the player
     * @param points   the points to add to the already present points
     * @throws RemoteException if the remote operation fails
     */
    void updatePlayerPoint(@NotNull String nickname, int points) throws RemoteException;

    /**
     * Update the status of the game, it is referred to the status of the model, not the state of
     * the implemented user interface
     *
     * @param status the new status of the game
     * @throws RemoteException if the remote operation fails
     */
    void updateGameStatus(@NotNull GameStatus status) throws RemoteException;

    /**
     * Set the final leaderboard of the game
     *
     * @param finalLeaderboard the final leaderboard
     * @throws RemoteException if the remote operation fails
     */
    void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard)
    throws RemoteException;

    /**
     * Update the common objectives of the table
     *
     * @param cardID     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     * @throws RemoteException if the remote operation fails
     */
    void updateCommonObjective(@NotNull Set<Integer> cardID, boolean removeMode)
    throws RemoteException;

    /**
     * Update the model with the playing players and their colors. It also confirms the player name
     *
     * @param currentPlayers the map of the players and their colors
     * @throws RemoteException if the remote operation fails
     */
    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers)
    throws RemoteException;

    /**
     * Notify the player that he is the god player, the god player is the moderator of the game. It
     * will set the god player and confirm the player name in the model
     *
     * @throws RemoteException if the remote operation fails
     */
    void notifyGodPlayer() throws RemoteException;

    /**
     * Update the number of players in the game
     *
     * @param numberOfPlayers the number of players
     * @throws RemoteException if the remote operation fails
     */
    void updateNumOfPlayers(int numberOfPlayers) throws RemoteException;

    /**
     * Notify the player that there has been a reconnection from the server and update the model to
     * the current situation of the game
     *
     * @param memento data structure that contains the current state of the game
     * @throws RemoteException if the remote operation fails
     */
    void sendReconnection(@NotNull ReconnectionModelMemento memento) throws RemoteException;

    /**
     * Notify the client to disconnect from the server
     *
     * @throws RemoteException if the remote operation fails
     */
    void youUgly() throws RemoteException;
}
