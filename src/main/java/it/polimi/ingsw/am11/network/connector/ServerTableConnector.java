package it.polimi.ingsw.am11.network.connector;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.network.RMI.server.game.ServerConnectorImpl;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

/**
 * Interface that defines the methods that the server can use to update the table.
 * <p>
 * The server will use these methods to send updates viewable by everyone to the clients.
 * <p>
 * Each player should provide an implementation of this interface to receive updates from the
 * server.
 *
 * @see ServerConnectorImpl
 * @see ServerGameSender
 */
public interface ServerTableConnector {

    /**
     * This method is used to notify the player of a change in the deck top card.
     *
     * @param type  specify which deck has been updated
     * @param color the new color of the deck top card, or null if the deck is empty
     */
    void updateDeckTop(@NotNull PlayableCardType type, @Nullable GameColor color);

    /**
     * This method is used to notify the player of a change in the field of a player
     *
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the field
     * @param y        the y coordinate of the field
     * @param cardId   the id of the card that has been added
     * @param isRetro  true if the card has been added face down, false if it has been added face
     *                 up
     */
    void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro);

    /**
     * This method is used to notify the player of a change in the shown playable card.
     *
     * @param previousId the id of the previous shown playable card, or null if there was no
     *                   previous shown playable card
     * @param currentId  the id of the current shown playable card, or null if there is no current
     *                   shown playable card
     */
    void updateShownPlayable(@Nullable Integer previousId, @Nullable Integer currentId);

    /**
     * This method is used to notify the player of a change in the turn.
     *
     * @param nickname the nickname of the player that has the turn
     */
    void updateTurnChange(@NotNull String nickname);

    /**
     * This method is used to notify the player of a change in the player points.
     *
     * @param nickname the nickname of the player
     * @param points   the new points of the player
     */
    void updatePlayerPoint(@NotNull String nickname, int points);

    /**
     * This method is used to notify the player of a change in the game status.
     *
     * @param status the new game status
     */
    void updateGameStatus(@NotNull GameStatus status);

    /**
     * This method is used to notify the player of a change in the common objectives.
     *
     * @param cardsId    the ids of the common objectives that have been added or removed
     * @param removeMode true if the cards have been removed, false if they have been added
     */
    void updateCommonObjective(@NotNull Set<Integer> cardsId, boolean removeMode);

    /**
     * This method is used to notify the player with the final leaderboard with the position of each
     * player
     *
     * @param finalLeaderboard the final leaderboard
     */
    void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard);

    /**
     * This method is used to notify the player with all the players in the game and their colors.
     *
     * @param currentPlayers the map containing the players and their colors
     */
    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers);

    /**
     * This method is used to notify the player with the number of players in the game.
     *
     * @param numOfPlayers the number of players in the game
     */
    void updateNumOfPlayers(@NotNull Integer numOfPlayers);
}
