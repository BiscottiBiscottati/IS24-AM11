package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.*;
import it.polimi.ingsw.am11.table.GameStatus;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class GameLogic implements GameModel {

    //TODO divide game logic in smaller classes like player manager and table manager turn manager

    private final RuleSet ruleSet;
    private final PlayerManager playerManager;
    private final PickablesTable pickablesTable;
    private final Plateau plateau;

    public GameLogic() {
        ruleSet = new BasicRuleset();
        setConstants();
        this.playerManager = new PlayerManager();
        this.pickablesTable = new PickablesTable();
        this.plateau = new Plateau();
    }

    private void setConstants() {
        PersonalSpace.setMaxSizeofHand(ruleSet.getHandSize());
        PersonalSpace.setMaxObjectives(ruleSet.getNumOfPersonalObjective());

        PlayerManager.setMaxNumberOfPlayers(ruleSet.getMaxPlayers());

        PickablesTable.setNumOfObjectives(ruleSet.getNumOfCommonObjectives());
        PickablesTable.setNumOfShownPerType(ruleSet.getMaxRevealedCardsPerType());

        Plateau.setArmageddonTime(ruleSet.getPointsToArmageddon());
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
    @Override // DONE
    public Set<String> getPlayers() {
        return playerManager.getPlayers();
    }


    /**
     * Retrieves the nickname of the player whose turn it is.
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method
     * returns <code> null
     * </code>.
     *
     * @return the nickname of the player that is playing or <code> null </code> if the game hasn't
     * started
     */
    @Override // DONE
    public String getCurrentTurnPlayer() {
        return playerManager.getCurrentTurnPlayer();
    }

    /**
     * Retrieves the nickname of the player that is first in the order of the turns
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method
     * returns <code> null
     *
     * @return the nickname of the player that played the first turn or <code> null</code> if the
     * game hasn't started
     */
    @Override // DONE
    public String getFirstPlayer() {
        return playerManager.getFirstPlayer();
    }

    /**
     * Retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific player
     * hand. The player is identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>PlayableCards</code> in the player hand
     */
    @Override // DONE
    public Set<Integer> getPlayerHand(@NotNull String nickname) {
        return playerManager.getHand(nickname);
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
    public Set<Integer> getPlayerObjective(@NotNull String nickname) {
        return playerManager.getPlayerObjective(nickname);
    }

    /**
     * Each player have an assigned color for his pawn.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>PlayerColor</code> of the player
     */
    @Override // DONE
    public PlayerColor getPlayerColor(@NotNull String nickname) {
        return playerManager.getPlayerColor(nickname);
    }

    @Override
    //TODO model shouldn't return something that has a reference of a Card, it should use ID
    public Map<Position, CardContainer> getPositionedCard(@NotNull String nickname)
    throws PlayerInitException {
        return playerManager.getPlayer(nickname).field().getCardsPositioned();
    }

    /**
     * For placing a card on the field a set of conditions has to be satisfied.
     * <p>
     * The new card needs to touch at least one of the corners of another placed card and all the
     * corners of placed cards touched by the new card must not be NOT_USABLE.
     * <p>
     * This method retrieves the positions that satisfy these conditions.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>Set</code> of <code>Position</code>s that are available for placing a card
     * in the field
     */
    @Override //
    public Set<Position> getAvailablePositions(@NotNull String nickname)
    throws PlayerInitException {
        return playerManager.getPlayer(nickname).field().getAvailablePositions();
    }

    /**
     * During a game players have objectives in common
     *
     * @return a list of ID of <code>ObjectiveCard</code> that represent the common objectives
     */
    @Override //
    public List<Integer> getCommonObjectives() {
        return pickablesTable.getCommonObjectives()
                             .stream()
                             .map(ObjectiveCard::getId)
                             .toList();
    }

    @Override
    public List<Integer> getExposedCards() {
        return pickablesTable.getShownPlayable()
                             .stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    /**
     * Retrive the points that a player has obtained since the start of the game
     *
     * @param nickname nickname of the player of interest
     * @return the points of the player
     * @throws IllegalPlateauActionException if there isn't a player with that nickname
     */
    @Override //
    public int getPlayerPoints(@NotNull String nickname)
    throws IllegalPlateauActionException, PlayerInitException {
        return plateau.getPlayerPoints(playerManager.getPlayer(nickname));
    }

    //endregion

    //region GettersPlateau  javadoc

    /**
     * Retrieve the ranking of a player at the end of the game, there could be more player with the
     * same ranking
     *
     * @param nickname of the player of interest
     * @return the position in the leaderboard of the player
     */
    @Override //
    public int getPlayerFinishingPosition(@NotNull String nickname)
    throws IllegalPlateauActionException, PlayerInitException {
        return plateau.getPlayerFinishingPosition(playerManager.getPlayer(nickname));
    }

    /**
     * This method return the winners, there could be more than on winners.
     *
     * @return a list with the names of the winners
     */
    @Override //
    public List<String> getWinner() {
        return plateau.getWinners()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }

    /**
     * This method initialize a new game, it has to be called after all the players have joined the
     * games This method shuffle the players Set in order to randomize the turns order, it chooses
     * the first player, it clears the field, the hand and the objectives for each player, it resets
     * the plateau and the pickableTable.
     *
     * @throws IllegalNumOfPlayersException If there aren't at least 2 players in the game
     * @throws GameStatusException          if a game is in progress
     */
    @Override //
    public void initGame() throws IllegalNumOfPlayersException, GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        if (playerManager.getNumberOfPlayers() < 2) {
            throw new IllegalNumOfPlayersException(
                    "You need at least 2 players to play this game, the current number is: " +
                    playerManager.getNumberOfPlayers());
        }

        resetAll();
        playerManager.startingTheGame();
        pickablesTable.initialize();
    }

    public void resetAll() {
        playerManager.resetAll();
        plateau.reset();
    }

    /**
     * This method can be used to add a new player in the game during the setting up phase.
     * <p>
     * A player can choose his nickname and color.
     *
     * @param nickname nickname of the new player
     * @param colour   color of the new player
     * @throws PlayerInitException if there is another player with the chosen nickname, if the
     *                             colour is already taken, if the limit of players per game has
     *                             already been reached
     * @throws GameStatusException if a game is in progress
     */
    @Override // DONE
    public void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        } else {
            Player newPlayer = playerManager.addPlayerToTable(nickname, colour);
            //TODO
            plateau.addPlayer(newPlayer);
        }
    }

    /**
     * Remove a player from the player list if present, else it does nothing.
     *
     * @param nickname nickname of the player of interest
     * @throws GameStatusException if a game is in progress
     */
    @Override // DONE
    public void removePlayer(@NotNull String nickname)
    throws GameStatusException, PlayerInitException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        plateau.removePlayer(playerManager.getPlayer(nickname));
        playerManager.removePlayer(nickname);
    }

    /**
     * Pick a <code>StarterCard</code> from the deck on the <code>PickableTable</code>.
     *
     * @return the ID of a <code>StarterCard</code>
     * @throws EmptyDeckException  if the deck of  <code>StartingCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */
    @Override //
    public int pickStarter() throws EmptyDeckException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        return pickablesTable.pickStarterCard().getId();
    }

    /**
     * Pick a <code>ObjectiveCard</code> from the deck on the <code>PickableTable</code>.
     *
     * @return the ID of a <code>ObjectiveCard</code>
     * @throws EmptyDeckException  if the deck of  <code>ObjectiveCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */
    @Override //
    public int pickObjective() throws EmptyDeckException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        return pickablesTable.pickObjectiveCard().getId();
    }

    /**
     * Each player needs a <code>StartingCard</code> at the beginning of the game.
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the <code>StartingCard</code> that has to be placed on the
     *                 field
     * @param isRetro  tell if the <code>StartingCard</code> is placed on the front (false) or on
     *                 the back (true)
     * @throws IllegalCardPlacingException if there is already card in that position
     * @throws GameStatusException         if the game is not ongoing
     */
    @Override //
    public void setStarterFor(@NotNull String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        StarterCard starterCard = pickablesTable.getStarterByID(cardID).orElseThrow();
        playerManager.getPlayer(nickname).field().placeStartingCard(starterCard, isRetro);
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
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException {
        //The rules book says that unused objectives have to be returned to the deck, but it
        // seems like
        //a useless action
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        ObjectiveCard objectiveCard = pickablesTable.getObjectiveByID(cardID).orElseThrow();
        playerManager.getPlayer(nickname).space().addObjective(objectiveCard);
    }

    /**
     * This method handle the changing of the turn and the final turn. If it is not the final turn
     * it checks if it's armageddonTime and gives the turn to the next players. If it's the final
     * turn it ends the game and prepare the final leaderboard.
     *
     * @throws GameBreakingException if there are discrepancies between plateau and gameLogic
     * @throws GameStatusException   if the game is not ongoing
     */
    @Override // DONE
    public String goNextTurn() throws GameBreakingException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        //TODO can be done to functional
        if (Stream.of(PlayableCardType.values())
                  .map(pickablesTable::getDeckTop)
                  .allMatch(Optional::isEmpty)) {
            plateau.activateArmageddon();
        }
        playerManager.goNextTurn();
        if (plateau.getStatus() == GameStatus.LAST_TURN && playerManager.isFirstTheCurrent()) {
            plateau.setStatus(GameStatus.ENDED);
            try {
                countObjectivesPoints();
            } catch (IllegalPlateauActionException ex) {
                throw new GameBreakingException("Players in game logic and plateau don't match");
            }
            plateau.setFinalLeaderboard();
        } else if (plateau.getStatus() == GameStatus.ARMAGEDDON &&
                   playerManager.isFirstTheCurrent()) {
            plateau.setStatus(GameStatus.ARMAGEDDON);
        }
        return playerManager.getCurrentTurnPlayer();
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
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        Player player = playerManager.getPlayer(nickname);
        if (! Objects.equals(playerManager.getCurrentTurnPlayer(), nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + playerManager.getCurrentTurnPlayer() +
                    " turn."
            );
        }
        PlayableCard card = pickablesTable.getPlayableByID(cardID)
                                          .orElseThrow(() -> new IllegalCardPlacingException(
                                                  "Card not found"));
        int points = 0;
        if (player.field().isAvailable(position)) {
            if (player.field().isRequirementMet(card, isRetro)) {
                //FIXME
                player.space().pickCard(0);
                points = player.field().place(card, position, isRetro);
            }
        } else {
            throw new IllegalCardPlacingException("Chosen position is not available");
        }
        plateau.addPlayerPoints(player, points);
    }

    @Override
    public int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           IllegalPlayerSpaceActionException, PlayerInitException {
        checkIfDrawAllowed(nickname);
        try {
            if (playerManager.getPlayer(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.drawPlayableFrom(type);
                playerManager.getPlayer(nickname).space().addCardToHand(card);
                return card.getId();
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the deck and not being able to " +
                    "put it anywhere"
            );
        }
    }

    @Override
    public void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException {
        checkIfDrawAllowed(nickname);

        try {
            if (playerManager.getPlayer(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableVisible(cardID);
                playerManager.getPlayer(nickname).space().addCardToHand(card);
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the visible and not being able " +
                    "to put it anywhere"
            );
        }
    }

    /**
     * Calculate the points from common and personal objectives for each player and add them to the
     * plateau
     *
     * @throws IllegalPlateauActionException if a player is not found
     * @throws GameStatusException           if the game is not ongoing
     */
    @Override //
    public void countObjectivesPoints()
    throws IllegalPlateauActionException, GameStatusException, GameBreakingException {
        if (plateau.getStatus() != GameStatus.ENDED) {
            throw new GameStatusException("the game has not ended yet");
        }
        for (String nickname : playerManager.getPlayers()) {
            Player player;
            try {
                player = playerManager.getPlayer(nickname);
            } catch (PlayerInitException e) {
                throw new GameBreakingException("Discrepancies between playerManager and itself");
            }
            for (ObjectiveCard commonObjective : pickablesTable.getCommonObjectives()) {
                int points = commonObjective.countPoints(player.field());
                if (points > 0) {
                    plateau.addCounterObjective(player);
                }
                plateau.addPlayerPoints(player, points);
            }
            for (ObjectiveCard privateObjective : player.space().getPlayerObjective()) {
                int points = privateObjective.countPoints(player.field());
                if (points > 0) {
                    plateau.addCounterObjective(player);
                }
                plateau.addPlayerPoints(player, points);
            }
        }
    }

    /**
     * Ends the current game and gives the possibility to add or remove players.
     */
    @Override //
    public void endGame() {
        plateau.setStatus(GameStatus.SETUP);
    }

    /**
     * Retrieves the current game status
     *
     * @return the current game status as an enum
     * @see GameStatus
     */
    @Override
    public GameStatus getStatus() {
        return plateau.getStatus();
    }

    public Optional<Color> getDeckTop(PlayableCardType type) {
        return pickablesTable.getDeckTop(type);
    }

    private void checkIfDrawAllowed(String nickname)
    throws GameStatusException, TurnsOrderException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        String currentTurnPlayer = playerManager.getCurrentTurnPlayer();
        if (! Objects.equals(currentTurnPlayer, nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentTurnPlayer + " turn."
            );
        }
    }
}
