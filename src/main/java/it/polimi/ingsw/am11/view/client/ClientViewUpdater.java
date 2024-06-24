package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public interface ClientViewUpdater {
    /**
     * Update the deck top card
     *
     * @param type  the type of the deck
     * @param color the color of the card
     */
    void updateDeckTop(@NotNull PlayableCardType type, Color color);

    /**
     * Update the field of the player, it can place or remove a card, if the removeMode is true the
     * cardId and isRetro are ignored.
     *
     * @param nickname the nickname of the player
     * @param x        the x coordinate of the card
     * @param y        the y coordinate of the card
     * @param cardId   the id of the card
     * @param isRetro  if the card is placed on it's retro
     */
    void updateField(@NotNull String nickname, int x, int y, int cardId,
                     boolean isRetro);

    /**
     * Update the visible cards on the table, it will remove the previousId and add the currentId
     *
     * @param previousId the id of the card to remove
     * @param currentId  the id of the card to add
     */
    void updateShownPlayable(Integer previousId, Integer currentId);

    /**
     * Update the current turn of the game
     *
     * @param nickname the nickname of the player that has the turn
     */
    void updateTurnChange(@NotNull String nickname);

    /**
     * Update the points of a player
     *
     * @param nickname the nickname of the player
     * @param points   the points to add to the already present points
     */
    void updatePlayerPoint(@NotNull String nickname, int points);

    /**
     * Update the status of the game, it is referred to the status of the model, not the state of
     * the implemented user interface
     *
     * @param status the new status of the game
     */
    void updateGameStatus(@NotNull GameStatus status);

    /**
     * Update the common objectives of the table
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode);

    /**
     * Set the final leaderboard of the game
     *
     * @param finalLeaderboard the final leaderboard
     */
    void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard);

    /**
     * Update the hand of the player
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    void updateHand(int cardId, boolean removeMode);

    /**
     * Update the personal objective of the player
     *
     * @param cardId     the id of the card to add or remove
     * @param removeMode if you want to remove the card
     */
    void updatePersonalObjective(int cardId, boolean removeMode);

    /**
     * Receive the starter card of the player
     *
     * @param cardId the id of the card
     */
    void receiveStarterCard(int cardId);

    /**
     * Receive the candidate objectives of the player
     *
     * @param cardId Set of card id
     */
    void receiveCandidateObjective(@NotNull Set<Integer> cardId);

    /**
     * Notify the player that he is the god player, the god player is the moderator of the game. It
     * will set the god player and confirm the player name in the model
     */
    void notifyGodPlayer();

    /**
     * Update the model with the playing players and their colors. It also confirms the player name
     *
     * @param currentPlayers the map of the players and their colors
     */
    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers);

    /**
     * Update the number of players in the game
     *
     * @param numOfPlayers the number of players
     */
    void updateNumOfPlayers(int numOfPlayers);

    /**
     * Notify the player that there has been a disconnection from the server
     */
    void disconnectedFromServer(@NotNull String message);

    /**
     * Notify the player that there has been a reconnection from the server and update the model to
     * the current situation of the game
     *
     * @param memento data structure that contains the current state of the game
     */
    void receiveReconnection(@NotNull ReconnectionModelMemento memento);

    /**
     * Get the ExceptionThrower of the user interface updater
     *
     * @return the ExceptionThrower
     */
    @NotNull
    ExceptionThrower getExceptionThrower();

    /**
     * Get the ClientChatUpdater of the user interface updater
     *
     * @return the ClientChatUpdater
     */
    @NotNull
    ClientChatUpdater getChatUpdater();
}
