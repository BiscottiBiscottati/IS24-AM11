package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.PersonalSpace;
import it.polimi.ingsw.am11.model.players.Player;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.PickablesTable;
import it.polimi.ingsw.am11.model.table.Plateau;
import it.polimi.ingsw.am11.model.utils.BasicRuleset;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.RuleSet;
import it.polimi.ingsw.am11.model.utils.TurnAction;
import it.polimi.ingsw.am11.model.utils.memento.GameModelMemento;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.model.utils.resilience.ReconnectionTimer;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.PersonalObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.ReconnectionEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.PlayerInfoEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic implements GameModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameLogic.class);

    private final @NotNull RuleSet ruleSet;
    private final @NotNull PlayerManager playerManager;
    private final @NotNull PickablesTable pickablesTable;
    private final @NotNull Plateau plateau;
    private final @NotNull ReconnectionTimer reconnectionTimer;
    private final @NotNull GameListenerSupport pcs;
    private boolean isLoadedGame;

    public GameLogic() {
        LOGGER.info("MODEL: Creating a new GameLogic instance");

        this.pcs = new GameListenerSupport();
        this.ruleSet = new BasicRuleset();
        setConstants();

        this.playerManager = new PlayerManager(this.pcs);
        this.pickablesTable = new PickablesTable(this.pcs);
        this.plateau = new Plateau(this.pcs);
        this.reconnectionTimer = new ReconnectionTimer(this);
        this.isLoadedGame = false;
    }

    private void setConstants() {
        LOGGER.info("MODEL: Setting constants");

        PersonalSpace.setMaxSizeofHand(ruleSet.getHandSize());
        PersonalSpace.setMaxObjectives(ruleSet.getNumOfPersonalObjective());
        PersonalSpace.setMaxCandidateObjectives(ruleSet.getObjectiveToChooseFrom());

        PlayerManager.setMaxNumberOfPlayers(ruleSet.getMaxPlayers());

        PickablesTable.setNumOfCommonObjectives(ruleSet.getNumOfCommonObjectives());
        PickablesTable.setNumOfShownPerType(ruleSet.getMaxRevealedCardsPerType());
        PickablesTable.setNumOfCandidatesObjectives(ruleSet.getObjectiveToChooseFrom());

        Plateau.setArmageddonTime(ruleSet.getPointsToArmageddon());
    }

    /**
     * Used to get the parameters to regulate various aspects of the game
     *
     * @return the rule set of the game
     */
    @Override
    public @NotNull RuleSet getRuleSet() {
        return ruleSet;
    }

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
    @Override
    public @NotNull Set<String> getPlayers() {
        return playerManager.getPlayers();
    }


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
    @Override // DONE
    public @NotNull String getCurrentTurnPlayer() throws GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game has not started, turns haven't been decided");
        }
        return playerManager.getCurrentTurnPlayer().orElseThrow();
    }

    /**
     * Retrieves the nickname of the player that is first in the order of the turns
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method
     * returns <code> null
     *
     * @return the nickname of the player that played the first turn
     * @throws GameStatusException if the game hasn't started or has ended
     */
    @Override // DONE
    public @NotNull String getFirstPlayer() throws GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException("the game has not started, turns haven't been decided");
        }
        return playerManager.getFirstPlayer().orElseThrow();
    }

    /**
     * Retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific player
     * hand. The player is identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>PlayableCards</code> in the player hand
     */
    @Override
    public @NotNull Set<Integer> getPlayerHand(@NotNull String nickname)
    throws GameStatusException, PlayerInitException {
        //FIXME to be precise cards are dealt in initGame so they are dealt
        if (Set.of(GameStatus.SETUP, GameStatus.CHOOSING_STARTERS, GameStatus.CHOOSING_OBJECTIVES)
               .contains(plateau.getStatus())) {
            throw new GameStatusException("the game is" +
                                          plateau.getStatus().name() +
                                          ", cards hasn't been dealt");
        }
        return playerManager.getPlayer(nickname)
                            .map(Player::space)
                            .map(PersonalSpace::getPlayerHand)
                            .map(Set::stream)
                            .map(playableCardStream ->
                                         playableCardStream.map(PlayableCard::getId)
                                                           .collect(Collectors.toUnmodifiableSet()))
                            .orElseThrow(() -> new PlayerInitException("Player not found"));
    }

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
    @Override // DONE
    public @NotNull Set<Integer> getPlayerObjective(@NotNull String nickname)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException("the game has not started, objectives hasn't been dealt");
        }
        return playerManager.getPlayerObjective(nickname);
    }

    /**
     * Each player has an assigned color for his pawn.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>PlayerColor</code> of the player
     */
    @Override // DONE
    public @NotNull PlayerColor getPlayerColor(@NotNull String nickname)
    throws PlayerInitException {
        return playerManager.getPlayerColor(nickname)
                            .orElseThrow(() -> new PlayerInitException("Player not found"));
    }

    @Override
    //TODO model shouldn't return something that has a reference of a Card, it should use ID
    public Map<Position, CardContainer> getPositionedCard(@NotNull String nickname)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP ||
            plateau.getStatus() == GameStatus.CHOOSING_STARTERS ||
            plateau.getStatus() == GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException(
                    "the game has not started, there are no positioned cards");
        }
        return playerManager.getPlayer(nickname)
                            .orElseThrow(() -> new PlayerInitException("Player not found"))
                            .field()
                            .getCardsPositioned();
    }

    /**
     * For placing a card on the field, a set of conditions has to be satisfied.
     * <p>
     * The new card needs to touch at least one of the corners of another placed card and all the
     * corners of placed cards touched by the new card have to not be NOT_USABLE.
     * <p>
     * This method retrieves the positions that satisfy these conditions.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>Set</code> of <code>Position</code>s that are available for placing a card
     * in the field
     */
    @Override //
    public @NotNull Set<Position> getAvailablePositions(@NotNull String nickname)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException(
                    "the game has not started, there are no positioned cards");
        }
        return playerManager.getAvailablePositions(nickname);
    }

    /**
     * During a game, players have objectives in common
     *
     * @return a set of ID of <code>ObjectiveCard</code> that represent the common objectives
     */
    @Override //
    public @NotNull Set<Integer> getCommonObjectives() throws GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException(
                    "the game has not started, there are no objectives");
        }
        return pickablesTable.getCommonObjectives()
                             .stream()
                             .map(ObjectiveCard::getId)
                             .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Set<Integer> getExposedCards(@NotNull PlayableCardType type)
    throws GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException(
                    "the game has not started, there are exposed cards");
        }
        return pickablesTable.getShownPlayable(type)
                             .stream()
                             .map(PlayableCard::getId)
                             .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Retrive the points that a player has obtained since the start of the game
     *
     * @param nickname nickname of the player of interest
     * @return the points of the player
     */
    @Override //
    public int getPlayerPoints(@NotNull String nickname)
    throws GameStatusException,
           GameBreakingException, PlayerInitException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException(
                    "the game has not started, players don't have points");
        }
        try {
            playerManager.getPlayer(nickname)
                         .orElseThrow(() -> new PlayerInitException("Player not found"));
            return plateau.getPlayerPoints(nickname);
        } catch (IllegalPlateauActionException e) {
            LOGGER.error("Plateau and player have discrepancies! Error: {}", e.getMessage());
            throw new GameBreakingException("Plateau and player manager have discrepancies");
        }
    }

    /**
     * Retrieve the ranking of a player at the end of the game, there could be more player with the
     * same ranking
     *
     * @param nickname of the player of interest
     * @return the position in the leaderboard of the player
     */
    @Override //
    public int getPlayerFinishingPosition(@NotNull String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException {
        if (plateau.getStatus() != GameStatus.ENDED) {
            throw new GameStatusException(
                    "the game has not ended, there isn't a leaderboard");
        }
        try {
            playerManager.getPlayer(nickname)
                         .orElseThrow(() -> new PlayerInitException("Player not found"));
            return plateau.getPlayerFinishingPosition(nickname);
        } catch (IllegalPlateauActionException e) {
            LOGGER.error("Plateau and player manager have discrepancies! Error: {}",
                         e.getMessage());
            throw new GameBreakingException("Plateau and player manager have discrepancies");
        }
    }

    /**
     * This method returns the winners, there could be more than on winners.
     *
     * @return a set with the names of the winners
     */
    @Override //
    public @NotNull Set<String> getWinner() throws GameStatusException {
        if (plateau.getStatus() != GameStatus.ENDED) {
            throw new GameStatusException(
                    "the game has not ended, there isn't a leaderboard");
        }
        return plateau.getWinners();
    }


    /**
     * This method initialize a new game, it has to be called after all the players have joined the
     * games This method shuffle the players Set in order to randomize the turns order, it chooses
     * the first player, it clears the field, the hand and the objectives for each player, it resets
     * the plateau and the pickableTable.
     *
     * @throws NumOfPlayersException If there aren't at least two players in the game
     * @throws GameStatusException   if a game is in progress
     */
    @Override //
    public void initGame()
    throws NumOfPlayersException, GameStatusException, GameBreakingException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }

        if (playerManager.getNumberOfPlayers() < 2) {
            throw new NumOfPlayersException(
                    "You need at least 2 players to play this game, the current number is: " +
                    playerManager.getNumberOfPlayers());
        }

        LOGGER.info("MODEL: Initializing the game...");

        SequencedMap<PlayerColor, String> collected =
                playerManager.getPlayers()
                             .stream()
                             .map(playerManager::getPlayer)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .collect(Collectors.toMap(
                                     Player::color,
                                     Player::nickname,
                                     (a, b) -> a,
                                     LinkedHashMap::new));

        LOGGER.debug("MODEL: Players info: {}", collected);

        pcs.fireEvent(new PlayerInfoEvent(collected));

        resetAll();

        try {
            LOGGER.info("MODEL: Picking starters...");
            pickStarters();
            plateau.setStatus(GameStatus.CHOOSING_STARTERS);
        } catch (IllegalPlayerSpaceActionException | GameStatusException |
                 IllegalPickActionException | PlayerInitException e) {
            LOGGER.error("Something broke while dealing starters or objectives! Error: {}",
                         e.getMessage());
            throw new GameBreakingException("Something broke while dealing starters or objectives");
        }
    }

    private void resetAll() {
        playerManager.resetAll();
        plateau.reset();
    }

    /**
     * Pick a <code>StarterCard</code> from the deck on the <code>PickableTable</code> and saves it
     * in player space
     *
     * @throws GameStatusException if the game is not ongoing
     */

    private void pickStarters()
    throws GameStatusException, PlayerInitException,
           IllegalPlayerSpaceActionException, IllegalPickActionException {
        for (String nickname : playerManager.getPlayers()) {
            StarterCard starter = pickablesTable.pickStarterCard();

            LOGGER.debug("MODEL: Picking starter with Id {} for {}", starter.getId(), nickname);

            playerManager.setStarterCard(nickname, starter);
        }
    }

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
    @Override // DONE
    public void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
    throws PlayerInitException, GameStatusException, NumOfPlayersException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }

        playerManager.addPlayerToTable(nickname, colour);
        plateau.addPlayer(nickname);

        LOGGER.info("MODEL: Added player {} with color {}", nickname, colour);
    }

    // FIXME may not even be needed as a method

    /**
     * Remove a player from the player list if present, else it does nothing.
     *
     * @param nickname nickname of the player of interest
     * @throws GameStatusException if a game is in progress
     */
    @Override // DONE
    public void removePlayer(@NotNull String nickname)
    throws GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }

        LOGGER.info("MODEL: Removing player {}", nickname);

        playerManager.removePlayer(nickname);
        plateau.removePlayer(nickname);
        pcs.removeListener(nickname); // FIXME for table listeners
    }

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
    @Override //
    public void setStarterFor(@NotNull String nickname, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException,
           GameBreakingException {
        if (plateau.getStatus() != GameStatus.CHOOSING_STARTERS) {
            throw new GameStatusException("you cannot give starters when " + plateau.getStatus() +
                                          " is the status");
        }
        Player player = playerManager.getPlayer(nickname)
                                     .orElseThrow(() -> new PlayerInitException("Player not " +
                                                                                "found"));
        StarterCard starterCard = playerManager.getStarterCard(nickname).orElseThrow();
        player.field().placeStartingCard(starterCard,
                                         isRetro);

        LOGGER.info("MODEL: Placing starter card {} for {} on its {}", starterCard.getId(),
                    nickname,
                    isRetro ? "back" : "front");

        pcs.fireEvent(new FieldChangeEvent(nickname,
                                           null,
                                           Map.entry(Position.of(0, 0),
                                                     CardContainer.of(starterCard, isRetro))));

        if (playerManager.areStarterSet()) {

            LOGGER.info("MODEL: All starters have been placed, moving to objectives");

            try {
                pickCandidateObjectives();
            } catch (EmptyDeckException | IllegalPickActionException e) {
                LOGGER.error("Something broke while dealing objectives! Error: {}", e.getMessage());
                throw new GameBreakingException(
                        "Something broke while dealing starters or objectives");
            }
            plateau.setStatus(GameStatus.CHOOSING_OBJECTIVES);
        }
    }

    /**
     * Set a new personal objective for a player.
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the <code>ObjectiveCard</code> to be assigned to the player
     * @throws IllegalPlayerSpaceActionException if there are already too many objectives
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //
    public void setObjectiveFor(@NotNull String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException,
           GameBreakingException {
        if (plateau.getStatus() != GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException("you cannot give objectives when " + plateau.getStatus() +
                                          " is the status");
        }
        ObjectiveCard objectiveCard = playerManager.getCandidateObjectiveByID(nickname, cardID);
        PersonalSpace playerSpace = playerManager.getPlayer(nickname)
                                                 .orElseThrow(() -> new PlayerInitException(
                                                         "Player not found"))
                                                 .space();
        playerSpace.addObjective(objectiveCard);

        LOGGER.info("MODEL: Assigning objective {} to {}", cardID, nickname);

        pcs.fireEvent(new PersonalObjectiveChangeEvent(nickname, null, cardID));
        if (playerManager.areObjectiveSet()) {
            LOGGER.info("MODEL: All objectives have been assigned, giving cards to players");

            giveCards();
            plateau.setStatus(GameStatus.ONGOING);
        }
    }

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
    @Override //
    public void placeCard(@NotNull String nickname, int cardID, @NotNull Position position,
                          boolean isRetro)
    throws IllegalCardPlacingException, TurnsOrderException, IllegalPlateauActionException,
           GameStatusException,
           NotInHandException, PlayerInitException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED ||
            plateau.getStatus() == GameStatus.CHOOSING_STARTERS ||
            plateau.getStatus() == GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException("the game is not ongoing");
        }
        Player player = playerManager.getPlayer(nickname)
                                     .orElseThrow(
                                             () -> new PlayerInitException("Player not found"));
        if (! Objects.equals(playerManager.getCurrentTurnPlayer(), Optional.of(nickname))) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " +
                    playerManager.getCurrentTurnPlayer().orElseThrow() +
                    " turn."
            );
        }
        if (playerManager.getCurrentAction() != TurnAction.PLACE_CARD) {
            // FIXME to throw a different exception
            throw new TurnsOrderException(nickname + " has already placed a card first");
        }
        if (player.space().hasCardBeenPlaced()) {
            throw new TurnsOrderException(nickname + " has already placed a card first");
        }

        PlayableCard card = CardDecoder.decodePlayableCard(cardID)
                                       .orElseThrow(() -> new IllegalCardPlacingException(
                                               "Card not found"));
        int points;
        Optional.of(playerManager.getHand(nickname).contains(cardID))
                .filter(b -> b)
                .orElseThrow(
                        () -> new NotInHandException("Card not in hand"));
        Optional.of(player.field())
                .filter(b -> b.isRequirementMet(card, isRetro))
                .orElseThrow(() -> new IllegalCardPlacingException("Card requirements not met!"));
        Optional.of(player.field())
                .filter(b -> b.isAvailable(position))
                .orElseThrow(() -> new IllegalCardPlacingException("Position not available!"));
        player.space().pickCard(cardID);

        LOGGER.info("MODEL: Placing card {} for {} on position {}", cardID, nickname, position);

        LOGGER.debug("MODEL: Removed card {} from hand of {}", cardID, nickname);

        pcs.fireEvent(new HandChangeEvent(nickname, cardID, null));

        points = player.field().place(card, position, isRetro);

        LOGGER.debug("MODEL: Placed card {} for {} on position {}", cardID, nickname, position);

        pcs.fireEvent(new FieldChangeEvent(nickname,
                                           null,
                                           Map.entry(position, CardContainer.of(card, isRetro))));

        player.space().setCardBeenPlaced(true);
        playerManager.setCurrentAction(TurnAction.DRAW_CARD);

        LOGGER.info("MODEL: Card placed, giving {} points to {}", points, nickname);

        plateau.addPlayerPoints(nickname, points);
    }

    @Override
    public int drawFromDeckOf(@NotNull PlayableCardType type, @NotNull String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           PlayerInitException, MaxHandSizeException, IllegalPickActionException {

        PersonalSpace playerSpace = playerManager.getPlayer(nickname)
                                                 .orElseThrow(() -> new PlayerInitException(
                                                         "Player not found"))
                                                 .space();

        checkIfDrawAllowed(nickname);

        if (playerSpace.availableSpaceInHand() >= 1) {

            LOGGER.info("MODEL: Drawing a card of type {} for {} from deck", type, nickname);

            PlayableCard card = pickablesTable.drawPlayableFrom(type);
            playerSpace.addCardToHand(card);

            LOGGER.debug("MODEL: Added in hand card {} of type {} for {}", card.getId(), type,
                         nickname);

            pcs.fireEvent(new HandChangeEvent(nickname, null, card.getId()));

            goNextTurn();
            return card.getId();
        } else {
            //TODO this exception should be transformed in a max hand size exception
            throw new MaxHandSizeException(nickname + " hand is already full");
        }
    }

    /**
     * This method handles the changing of the turn and the final turn. If it is not the final turn,
     * it checks if it's armageddonTime and gives the turn to the next players. If it's the final
     * turn, it ends the game and prepares the final leaderboard.
     *
     * @throws GameBreakingException if there are discrepancies between plateau and gameLogic
     * @throws GameStatusException   if the game is not ongoing
     */
    @Override
    public void goNextTurn() throws GameBreakingException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP ||
            plateau.getStatus() == GameStatus.ENDED ||
            plateau.getStatus() == GameStatus.CHOOSING_STARTERS ||
            plateau.getStatus() == GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException("the game is not ongoing");
        }

        if (playerManager.getNumberOfConnected() == 1) {
            reconnectionTimer.waitForReconnection();
            return;
        }

        //check if it's armageddon time
        if (Stream.of(PlayableCardType.values())
                  .map(pickablesTable::getDeckTop)
                  .allMatch(Optional::isEmpty) &&
            plateau.getStatus() == GameStatus.ONGOING) {

            LOGGER.info("MODEL: It's armageddon time!");

            plateau.activateArmageddon();
        }
        // go to the next player
        playerManager.goNextTurn();

        //handle the game end
        if (plateau.getStatus() == GameStatus.LAST_TURN && playerManager.isFirstTheCurrent()) {
            plateau.setStatus(GameStatus.ENDED);
            try {

                LOGGER.info("MODEL: Game ended, calculating points");

                countObjectivesPoints();
            } catch (IllegalPlateauActionException ex) {
                LOGGER.error("Players in game logic and plateau don't match! Error: {}",
                             ex.getMessage());
                throw new GameBreakingException("Players in game logic and plateau don't match");
            }

            LOGGER.info("MODEL: Setting final leaderboard");

            plateau.setFinalLeaderboard();

            pcs.clearListeners();
            reconnectionTimer.cancelAll();
            CentralController.INSTANCE.destroyGame();
            return;
        } else if (plateau.getStatus() == GameStatus.ARMAGEDDON &&
                   playerManager.isFirstTheCurrent()) {

            LOGGER.info("MODEL: Careful it's the last turn!");

            plateau.setStatus(GameStatus.LAST_TURN);
        }

        if (playerManager.areAllDisconnected()) {
            LOGGER.info("MODEL: All players are disconnected, waiting for reconnection");
            return;
        }

        if (playerManager.isCurrentDisconnected()) {
            LOGGER.info("MODEL: Player {} is disconnected, skipping his turn",
                        playerManager.getCurrentTurnPlayer().orElseThrow());
            goNextTurn();
        }
    }

    @Override
    public int drawVisibleOf(@NotNull PlayableCardType type, @NotNull String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException {

        PersonalSpace playerSpace = playerManager.getPlayer(nickname)
                                                 .orElseThrow(() -> new PlayerInitException(
                                                         "Player not found"))
                                                 .space();
        checkIfDrawAllowed(nickname);
        try {
            if (playerSpace.availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableVisible(cardID);
                playerSpace.addCardToHand(card);

                LOGGER.info("MODEL: Drawing a card {} of type {} for {} from visible", cardID, type,
                            nickname);

                pcs.fireEvent(new HandChangeEvent(nickname, null, card.getId()));

                goNextTurn();
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            LOGGER.error(
                    "We have lost a card due to picking it from the visible and not being able " +
                    "to put it anywhere! Error: {}", ex.getMessage());
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the visible and not being able " +
                    "to put it anywhere"
            );
        }
        return cardID;
    }

    /**
     * Ends the current game and gives the possibility to add or remove players.
     * <p>
     * It doesn't clear the current state of the game. Doesn't calculate final points.
     * <p>
     * It must be called with a initGame for reinitialization
     */
    @Override //
    public void forceEnd() {
        LOGGER.info("MODEL: Forcing the end of the game");
        plateau.setStatus(GameStatus.SETUP);
    }

    /**
     * Retrieves the current game status
     *
     * @return the current game status as an enum
     * @see GameStatus
     */
    @Override
    public @NotNull GameStatus getStatus() {
        return plateau.getStatus();
    }

    @Override
    public @NotNull Optional<Color> getDeckTop(@NotNull PlayableCardType type)
    throws GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException("the game is not ongoing");
        }
        return pickablesTable.getDeckTop(type);
    }

    @Override
    public @NotNull Set<Integer> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() != GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException("the game is not in starting phase");
        }
        return playerManager.getCandidateObjectives(nickname)
                            .stream()
                            .map(ObjectiveCard::getId)
                            .collect(Collectors.toSet());
    }

    /**
     * Each player will get assigned a specific starter card, this method retrieves it.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>StarterCard</code> assigned to the player
     * @throws PlayerInitException if there is no player with that nickname
     */

    @Override
    public @NotNull Optional<Integer> getStarterCard(@NotNull String nickname)
    throws PlayerInitException, GameStatusException {
        if (Set.of(GameStatus.SETUP, GameStatus.ENDED)
               .contains(plateau.getStatus())) {
            throw new GameStatusException("the game has not started");
        }
        return playerManager.getStarterCard(nickname)
                            .map(StarterCard::getId);
    }

    @Override
    public void addPlayerListener(@NotNull String nickname,
                                  @NotNull PlayerListener playerListener) {

        LOGGER.debug("MODEL: Adding player listener for {}", nickname);

        pcs.addListener(nickname, playerListener);
    }

    @Override
    public void addTableListener(@NotNull TableListener listener) {

        LOGGER.debug("MODEL: Adding table listener");

        pcs.addListener(listener);
    }

    @Override
    public void disconnectPlayer(@NotNull String nickname) {
        Player player = playerManager.getPlayer(nickname)
                                     .orElse(null);
        if (player == null) {
            LOGGER.error("MODEL: Player {} not found, no one to disconnect", nickname);
            return;
        }

        if (plateau.getStatus() == GameStatus.CHOOSING_STARTERS ||
            plateau.getStatus() == GameStatus.CHOOSING_OBJECTIVES) {
            LOGGER.info("MODEL: Disconnecting player {} during setup", nickname);
            reconnectionTimer.waitForTotalReconnection();
        }

        LOGGER.info("MODEL: Adding {} to unavailable players", nickname);

        playerManager.disconnectPlayer(player);
        pcs.removeListener(nickname);

        if (! isLoadedGame) {
            checkIfGameCanContinue();
            if (playerManager.isTurnOf(nickname)) {
                reconnectionTimer.disconnectCurrent(nickname, playerManager.getCurrentAction());
            }
        }
    }

    @Override
    public void reconnectPlayer(@NotNull String nickname, @NotNull PlayerListener playerListener)
    throws PlayerInitException {
        Player player =
                playerManager.getPlayer(nickname)
                             .orElseThrow(() -> new PlayerInitException("Player not found"));

        playerManager.reconnectPlayer(player);
        pcs.addListener(nickname, playerListener);
        LOGGER.info("MODEL: Reconnected player {}", nickname);

        if (reconnectionTimer.isWaitingForReconnection()) {
            reconnectionTimer.reconnect();
            if (plateau.getStatus() == GameStatus.ONGOING && ! playerManager.isTurnOf(nickname)) {
                try {
                    goNextTurn();
                } catch (GameStatusException e) {
                    LOGGER.error("MODEL: Something broke while reconnecting player! Error: {}",
                                 e.getMessage());
                    throw new RuntimeException(e);
                }
            }

        }
        if (playerManager.isTurnOf(nickname)) {
            reconnectionTimer.reconnectCurrent();
        }

        if (playerManager.areAllReconnected()) isLoadedGame = false;

        reSyncWith(nickname);
    }

    @Override
    public boolean isDisconnected(@NotNull String nickname) {
        return playerManager.isDisconnected(nickname);
    }

    @Override
    public void endGameEarly() {
        plateau.setStatus(GameStatus.ENDED);
        Player winningPlayer = playerManager.getPlayers().stream()
                                            .filter(playerManager::isConnected)
                                            .map(playerManager::getPlayer)
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .findFirst().orElseThrow();

        plateau.setWinner(winningPlayer.nickname());
        pcs.clearListeners();
        reconnectionTimer.cancelAll();
        CentralController.INSTANCE.destroyGame();
    }

    @Override
    public @NotNull GameModelMemento save() {
        return new GameModelMemento(pickablesTable.save(),
                                    plateau.save(),
                                    playerManager.save());
    }

    @Override
    public void load(@NotNull GameModelMemento memento) {
        pickablesTable.load(memento.table());
        playerManager.load(memento.playerManager());
        plateau.load(memento.plateau()
        );
        isLoadedGame = true;
        playerManager.getPlayers().forEach(this::disconnectPlayer);
    }

    @Override
    public void reSyncWith(@NotNull String nickname) {
        ReconnectionModelMemento memento = new ReconnectionModelMemento(pickablesTable.savePublic(),
                                                                        plateau.save(),
                                                                        playerManager.save(
                                                                                nickname));
        pcs.fireEvent(new ReconnectionEvent(nickname, memento));
    }

    private void checkIfGameCanContinue() {
        if (playerManager.areAllDisconnected()) {
            LOGGER.info("MODEL: All players are disconnected, stopping the game");
            CentralController.INSTANCE.destroyGame();
            reconnectionTimer.cancelAll();
        }
    }

    private void giveCards() throws GameBreakingException {
        pickablesTable.initialize();

        try {
            // giving cards to each player
            PlayableCard card;
            List<Player> players = playerManager.getPlayers().stream()
                                                .map(playerManager::getPlayer)
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .toList();
            for (Player player : players) {
                int numOfGolds = ruleSet.getGoldAtStart();
                for (int i = 0; i < numOfGolds; i++) {
                    card = pickablesTable.drawPlayableFrom(PlayableCardType.GOLD);
                    player.space().addCardToHand(card);

                    LOGGER.debug("MODEL: Giving gold card {} to {}", card.getId(),
                                 player.nickname());

                    pcs.fireEvent(new HandChangeEvent(player.nickname(),
                                                      null,
                                                      card.getId()));
                }
                int numOfResources = ruleSet.getResourceAtStart();
                for (int i = 0; i < numOfResources; i++) {
                    card = pickablesTable.drawPlayableFrom(PlayableCardType.RESOURCE);
                    player.space().addCardToHand(card);

                    LOGGER.debug("MODEL: Giving resource card {} to {}", card.getId(),
                                 player.nickname());

                    pcs.fireEvent(new HandChangeEvent(player.nickname(),
                                                      null,
                                                      card.getId()));
                }
            }
        } catch (EmptyDeckException | MaxHandSizeException e) {
            LOGGER.error("Something broke while dealing cards! Error: {}", e.getMessage());
            throw new GameBreakingException("Something broke while dealing cards");
        }

        LOGGER.info("MODEL: All cards have been given to players, choosing first player");

        playerManager.chooseFirstPlayer();
    }

    /**
     * Pick a <code>ObjectiveCard</code> from the deck on the <code>PickableTable</code>.
     *
     * @throws EmptyDeckException if the deck of  <code>ObjectiveCard</code> is empty
     */

    private void pickCandidateObjectives()
    throws EmptyDeckException, PlayerInitException,
           IllegalPickActionException {

        for (String nickname : playerManager.getPlayers()) {
            Set<ObjectiveCard> objectives = pickablesTable.pickObjectiveCandidates();

            LOGGER.debug("MODEL: Picking candidate objectives {} for {}",
                         objectives.stream()
                                   .map(ObjectiveCard::getType)
                                   .toList(),
                         nickname);

            playerManager.setCandidateObjectives(nickname,
                                                 objectives);
        }
    }

    private void checkIfDrawAllowed(@NotNull String nickname)
    throws GameStatusException, TurnsOrderException, IllegalPickActionException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED ||
            plateau.getStatus() == GameStatus.CHOOSING_STARTERS ||
            plateau.getStatus() == GameStatus.CHOOSING_OBJECTIVES) {
            throw new GameStatusException("the game is not ongoing");
        }
        // chef if current player not a player
        Optional<String> currentTurnPlayer = playerManager.getCurrentTurnPlayer();
        if (! Objects.equals(currentTurnPlayer, Optional.of(nickname))) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentTurnPlayer + " turn."
            );
        }
        if (! playerManager.getPlayer(nickname).orElseThrow().space().hasCardBeenPlaced()) {
            throw new IllegalPickActionException(nickname + " has to place a card first");
        }

    }

    /**
     * Calculate the points from common and personal objectives for each player and add them to the
     * plateau
     *
     * @throws IllegalPlateauActionException if a player is not found
     * @throws GameStatusException           if the game is not ongoing
     */
    private void countObjectivesPoints()
    throws IllegalPlateauActionException, GameStatusException, GameBreakingException {
        if (plateau.getStatus() != GameStatus.ENDED) {
            throw new GameStatusException("the game has not ended yet");
        }

        for (String nickname : playerManager.getPlayers()) {
            Player player;
            try {
                player = playerManager.getPlayer(nickname)
                                      .orElseThrow(
                                              () -> new PlayerInitException("player not found"));
            } catch (PlayerInitException e) {
                LOGGER.error("Discrepancies between playerManager and itself! Error: {}",
                             e.getMessage());
                throw new GameBreakingException("Discrepancies between playerManager and itself");
            }

            LOGGER.info("MODEL: Counting points from common objectives");

            for (ObjectiveCard commonObjective : pickablesTable.getCommonObjectives()) {
                int points = commonObjective.countPoints(player.field());

                LOGGER.debug("MODEL: Common Objective {} gives {} points to {}",
                             commonObjective.getId(),
                             points,
                             nickname);

                if (points > 0) {
                    plateau.addCounterObjective(player.nickname());
                }
                plateau.addPlayerPoints(nickname, points);
            }

            LOGGER.info("MODEL: Counting points from personal objectives");

            for (ObjectiveCard privateObjective : player.space().getPlayerObjective()) {
                int points = privateObjective.countPoints(player.field());

                LOGGER.debug("MODEL: Personal Objective {} gives {} points to {}",
                             privateObjective.getId(), points,
                             nickname);

                if (points > 0) {
                    plateau.addCounterObjective(player.nickname());
                }
                plateau.addPlayerPoints(nickname, points);
            }
        }
    }
}

