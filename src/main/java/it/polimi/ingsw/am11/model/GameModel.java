package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.RuleSet;
import it.polimi.ingsw.am11.model.utils.memento.GameModelMemento;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"ClassWithTooManyMethods", "OverlyCoupledClass"})
public interface GameModel {

    /**
     * Used to get the parameters to regulate various aspects of the game
     *
     * @return the rule set of the game
     */
    RuleSet getRuleSet();

    /**
     * Retrieves the nicknames of all the players of the current game.
     * <p>
     * The nickname is unique in the game and can be used to identify each specific
     * <code> Player </code>.
     * <p>
     * Nicknames are chosen at the beginning of the game.
     *
     * @return a set of the nicknames of the players
     */
    Set<String> getPlayers();

    /**
     * Retrieves the nickname of the player whose turn it is.
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method
     * returns <code> null
     * </code>.
     *
     * @return the nickname of the player that is playing
     * @throws GameStatusException if the game hasn't started or has ended
     */
    String getCurrentTurnPlayer() throws GameStatusException;

    /**
     * Retrieves the nickname of the player that is first in the order of the turns
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method
     * returns null
     *
     * @return the nickname of the player that played the first turn
     * @throws GameStatusException if the game hasn't started or has ended
     */
    String getFirstPlayer() throws GameStatusException;

    /**
     * Retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific player
     * hand. The player is identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>PlayableCards</code> in the player hand
     */
    Set<Integer> getPlayerHand(@NotNull String nickname)
    throws GameStatusException, PlayerInitException;

    /**
     * Each player can have one or more personal objectives represented by the
     * <code>ObjectiveCards</code>.
     * <p>
     * This method retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific
     * player hand.
     * <p>
     * The player is identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>ObjectiveCards</code> of the player
     */
    Set<Integer> getPlayerObjective(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    /**
     * Each player has an assigned color for his pawn.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>PlayerColor</code> of the player
     */
    PlayerColor getPlayerColor(@NotNull String nickname) throws PlayerInitException;

    /**
     * Return a Map that has Positions as keys and CardContainers as values, it represents the field
     * of a player.
     *
     * @param nickname nickname of the player of interest
     * @return a map with the positions and the cards placed on the field
     * @throws PlayerInitException if the player is not found
     * @throws GameStatusException if the game is not ongoing
     */
    Map<Position, CardContainer> getPositionedCard(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    /**
     * For placing a card on the field, a set of conditions has to be satisfied.
     * <p>
     * The new card needs to touch at least one of the corners of another placed card, and all the
     * corners of placed cards touched by the new card have to not be NOT_USABLE.
     * <p>
     * This method retrieves the positions that satisfy these conditions.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>Set</code> of <code>Position</code>s that are available for placing a card
     * in the field
     */
    Set<Position> getAvailablePositions(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    /**
     * During a game, players have objectives in common
     *
     * @return a set of ID of <code>ObjectiveCard</code> that represent the common objectives
     */
    @NotNull
    Set<Integer> getCommonObjectives() throws GameStatusException;

    /**
     * Return a Set of cardIDs, it represents the cards of PlayableCardType that are on their front
     * on the table
     *
     * @param type type of the card to be shown
     * @return a set of card IDs
     * @throws GameStatusException if the game is not ongoing
     */
    Set<Integer> getExposedCards(@NotNull PlayableCardType type) throws GameStatusException;

    /**
     * Retrive the points that a player has obtained since the start of the game
     *
     * @param nickname nickname of the player of interest
     * @return the points of the player
     */
    int getPlayerPoints(@NotNull String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    /**
     * Retrieve the ranking of a player at the end of the game, there could be more player with the
     * same ranking
     *
     * @param nickname of the player of interest
     * @return the position in the leaderboard of the player
     */
    int getPlayerFinishingPosition(@NotNull String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    /**
     * This method returns the winners, there could be more than on winners.
     *
     * @return a set with the names of the winners
     */
    Set<String> getWinner() throws GameStatusException;

    /**
     * This method initializes a new game, it has to be called after all the players have joined the
     * games This method shuffles the players Set to randomize the turn order, it chooses the first
     * player, it clears the field, the hand, and the objectives for each player, it resets the
     * plateau and the pickableTable.
     *
     * @throws NumOfPlayersException If there aren't at least two players in the game
     * @throws GameStatusException   if a game is in progress
     */
    void initGame() throws NumOfPlayersException, GameStatusException, GameBreakingException;

    /**
     * This method can be used to add a new player in the game during the setting-up phase.
     * <p>
     * A player can choose his nickname and color.
     *
     * @param nickname nickname of the new player
     * @param colour   color of the new player
     * @throws PlayerInitException if there is another player with the chosen nickname, if the color
     *                             is already taken, if the limit of players per game has already
     *                             been reached
     * @throws GameStatusException if a game is in progress
     */
    void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
    throws PlayerInitException, GameStatusException, NumOfPlayersException;

    /**
     * Remove a player from the player list if present, else it does nothing.
     *
     * @param nickname nickname of the player of interest
     * @throws GameStatusException if a game is in progress
     */

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    /**
     * Each player needs a <code>StartingCard</code> at the beginning of the game. This method
     * places the picked one on the field
     *
     * @param nickname nickname of the player of interest
     * @param isRetro  tell if the <code>StartingCard</code> is placed on the front (false) or on
     *                 the back (true)
     * @throws IllegalCardPlacingException if there is already a card in that position
     * @throws GameStatusException         if the game is not ongoing
     */
    void setStarterFor(@NotNull String nickname, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException,
           GameBreakingException;

    /**
     * Set a new personal objective for a player.
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the <code>ObjectiveCard</code> to be assigned to the player
     * @throws IllegalPlayerSpaceActionException if there are already too many objectives
     * @throws GameStatusException               if the game is not ongoing
     */
    void setObjectiveFor(@NotNull String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException,
           GameBreakingException;

    /**
     * This method is used to place new playable cards on the field
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the card to be placed
     * @param position position where the player wants to place the card
     * @param isRetro  false if the card has to be placed with the front facing up, true otherwise
     * @throws IllegalCardPlacingException   if the card can't be placed in that position
     * @throws TurnsOrderException           if it's not that player turn
     * @throws IllegalPlateauActionException if there isn't any player with that nickname
     * @throws GameStatusException           if the game is not ongoing
     */
    void placeCard(@NotNull String nickname, int cardID, @NotNull Position position,
                   boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException,
           PlayerInitException;

    /**
     * This method is used to draw a card from the deck of the specified type
     *
     * @param type     type of the card to be drawn
     * @param nickname nickname of the player of interest
     * @return the ID of the card drawn
     * @throws GameStatusException        if the game is not started
     * @throws TurnsOrderException        if it's not that player turn
     * @throws GameBreakingException      if there are discrepancies between plateau and gameLogic
     * @throws EmptyDeckException         if the deck is empty
     * @throws PlayerInitException        if the player is not found
     * @throws MaxHandSizeException       if the player hand is already full
     * @throws IllegalPickActionException if the player hasn't placed a card yet
     */
    int drawFromDeckOf(@NotNull PlayableCardType type, @NotNull String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           PlayerInitException, MaxHandSizeException,
           IllegalPickActionException;

    /**
     * This method handles the changing of the turn and the final turn. If it is not the final turn,
     * it checks if it's armageddonTime and gives the turn to the next players. If it's the final
     * turn, it ends the game and prepares the final leaderboard.
     *
     * @throws GameBreakingException if there are discrepancies between plateau and gameLogic
     * @throws GameStatusException   if the game is not ongoing
     */
    void goNextTurn() throws GameBreakingException, GameStatusException;

    /**
     * Used to draw a card from the visible cards of the specified type
     *
     * @param type     type of the card to be drawn
     * @param nickname nickname of the player of interest
     * @param cardID   ID of the card to be drawn
     * @return the ID of the card drawn
     * @throws GameStatusException               if the game has not started
     * @throws TurnsOrderException               if it's not that player turn
     * @throws GameBreakingException             if there are discrepancies between plateau and
     *                                           gameLogic
     * @throws IllegalPlayerSpaceActionException if the player hand is already full
     * @throws IllegalPickActionException        if the player hasn't placed a card yet
     * @throws PlayerInitException               if the player is not found
     */
    int drawVisibleOf(@NotNull PlayableCardType type, @NotNull String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException;

    /**
     * Ends the current game and gives the possibility to add or remove players.
     * <p>
     * It doesn't clear the current state of the game. Doesn't calculate final points.
     * <p>
     * It must be called with an initGame for reinitialization
     */
    void forceEnd();

    /**
     * Retrieves the current game status
     *
     * @return the current game status as an enum
     * @see GameStatus
     */
    GameStatus getStatus();

    /**
     * Used to get the Color on the top of the specified card.
     *
     * @param type type of the card
     * @return the color of the top card
     * @throws GameStatusException if the game is not ongoing
     */
    Optional<GameColor> getDeckTop(@NotNull PlayableCardType type) throws GameStatusException;

    /**
     * Used to get a Set of the candidate objectives of the specified player
     *
     * @param nickname nickname of the player of interest
     * @return a set of the IDs of the candidate objectives
     * @throws PlayerInitException if the player is not found
     * @throws GameStatusException if the objectives have not been dealt with yet
     */
    Set<Integer> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    /**
     * Each player will get assigned a specific starter card, this method retrieves it.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>StarterCard</code> assigned to the player
     * @throws PlayerInitException if there is no player with that nickname
     */
    Optional<Integer> getStarterCard(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    /**
     * This method is used to add a listener to the model, it will be notified
     *
     * @param nickname       nickname of the player of interest
     * @param playerListener listener to be added
     */
    void addPlayerListener(@NotNull String nickname, @NotNull PlayerListener playerListener);

    /**
     * This method is used to add a listener to the model, it will be notified
     *
     * @param listener listener to be added
     */
    void addTableListener(@NotNull TableListener listener);

    /**
     * This method is used to disconnect a player from the game
     *
     * @param nickname nickname of the player of interest
     */
    void disconnectPlayer(@NotNull String nickname);

    /**
     * This method is used to reconnect a player to the game
     *
     * @param nickname       nickname of the player of interest
     * @param playerListener listener to be added
     * @throws PlayerInitException if there is no player with that nickname that can reconnect
     */
    void reconnectPlayer(@NotNull String nickname, @NotNull PlayerListener playerListener)
    throws PlayerInitException;

    /**
     * To check if a player is currently disconnected
     *
     * @param nickname nickname of the player of interest
     * @return true if the player is disconnected, false otherwise
     */
    boolean isDisconnected(@NotNull String nickname);

    /**
     * This method is used to end the game early, it will calculate the final points and the
     */
    void endGameEarly();

    /**
     * Used to save the current state of the game
     *
     * @return a data structure that contains all the information necessary to restore the game to
     * the current state
     */
    @NotNull
    GameModelMemento save();

    /**
     * Used to load an old game to the model, the current model will be replaced by the one in the
     * memento
     *
     * @param memento the data structure that contains the state of the game
     */
    void load(@NotNull GameModelMemento memento);

    /**
     * Used tu update the client with the information of the loaded game
     *
     * @param nickname nickname of the player of interest
     */
    void reSyncWith(@NotNull String nickname);
}
