package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.*;
import it.polimi.ingsw.am11.players.utils.CardContainer;
import it.polimi.ingsw.am11.players.utils.PlayerColor;
import it.polimi.ingsw.am11.players.utils.Position;
import it.polimi.ingsw.am11.table.GameStatus;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic implements GameModel {

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
        PersonalSpace.setMaxCandidateObjectives(ruleSet.getObjectiveToChooseFrom());

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
    @Override // DONE
    public @NotNull Set<Integer> getPlayerHand(@NotNull String nickname)
    throws GameStatusException, PlayerInitException {
        if (plateau.getStatus() == GameStatus.SETUP) {
            throw new GameStatusException("the game has not started, cards hasn't been dealt");
        }
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
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.STARTING) {
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
            // TODO throws list to complete
            return plateau.getPlayerPoints(playerManager.getPlayer(nickname)
                                                        .orElseThrow(() -> new PlayerInitException(
                                                                "Player not found")));
        } catch (IllegalPlateauActionException e) {
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
            return plateau.getPlayerFinishingPosition(
                    playerManager.getPlayer(nickname)
                                 .orElseThrow(
                                         () -> new PlayerInitException(
                                                 "Player not " +
                                                 "found")));
        } catch (IllegalPlateauActionException e) {
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
        return plateau.getWinners()
                      .stream()
                      .map(Player::nickname)
                      .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * This method initialize a new game, it has to be called after all the players have joined the
     * games This method shuffle the players Set in order to randomize the turns order, it chooses
     * the first player, it clears the field, the hand and the objectives for each player, it resets
     * the plateau and the pickableTable.
     *
     * @throws IllegalNumOfPlayersException If there aren't at least two players in the game
     * @throws GameStatusException          if a game is in progress
     */
    @Override //
    public void initGame()
    throws IllegalNumOfPlayersException, GameStatusException, GameBreakingException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        if (playerManager.getNumberOfPlayers() < 2) {
            throw new IllegalNumOfPlayersException(
                    "You need at least 2 players to play this game, the current number is: " +
                    playerManager.getNumberOfPlayers());
        }

        resetAll();
        pickablesTable.initialize();
        playerManager.startingTheGame();


        try {
            PlayableCard card;
            List<PersonalSpace> spaces = playerManager.getPlayers().stream()
                                                      .map(playerManager::getPlayer)
                                                      .filter(Optional::isPresent)
                                                      .map(Optional::get)
                                                      .map(Player::space)
                                                      .toList();
            for (PersonalSpace space : spaces) {
                int numOfGolds = ruleSet.getGoldAtStart();
                for (int i = 0; i < numOfGolds; i++) {
                    card = pickablesTable.drawPlayableFrom(PlayableCardType.GOLD);
                    space.addCardToHand(card);
                }
                int numOfResources = ruleSet.getResourceAtStart();
                for (int i = 0; i < numOfResources; i++) {
                    card = pickablesTable.drawPlayableFrom(PlayableCardType.RESOURCE);
                    space.addCardToHand(card);
                }
            }
        } catch (EmptyDeckException | MaxHandSizeException e) {
            throw new GameBreakingException("Something broke while dealing cards");
        }

        try {
            for (String nickname : playerManager.getPlayers()) {
                pickStarterFor(nickname);
                pickCandidateObjectives(nickname);
            }
        } catch (IllegalPlayerSpaceActionException | EmptyDeckException |
                 IllegalPickActionException | PlayerInitException e) {
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
     * @throws EmptyDeckException  if the deck of  <code>StartingCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */

    private void pickStarterFor(@NotNull String nickname)
    throws EmptyDeckException, GameStatusException, PlayerInitException,
           IllegalPlayerSpaceActionException, IllegalPickActionException {
        if (plateau.getStatus() != GameStatus.STARTING) {
            throw new GameStatusException("this is not the right time");
        }
        if (playerManager.getStarterCard(nickname).isPresent()) {
            throw new IllegalPickActionException("This payer already has his starter");
        }
        playerManager.setStarterCard(nickname, pickablesTable.pickStarterCard());
    }

    /**
     * Pick a <code>ObjectiveCard</code> from the deck on the <code>PickableTable</code>.
     *
     * @throws EmptyDeckException  if the deck of  <code>ObjectiveCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */
    private void pickCandidateObjectives(@NotNull String nickname)
    throws EmptyDeckException, GameStatusException, PlayerInitException,
           IllegalPickActionException {
        if (plateau.getStatus() != GameStatus.STARTING) {
            throw new GameStatusException("this is not the right time");
        }
        if (! playerManager.getCandidateObjectives(nickname).isEmpty()) {
            throw new IllegalPickActionException("This payer already has his candidate objectives");
        }
        int objectiveToChooseFrom = ruleSet.getObjectiveToChooseFrom();
        for (int i = 0; i < objectiveToChooseFrom; i++) {
            playerManager.setNewCandidateObjective(nickname, pickablesTable.pickObjectiveCard());
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
    throws GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        try {
            plateau.removePlayer(playerManager.getPlayer(nickname)
                                              .orElseThrow(() -> new PlayerInitException(
                                                      "player not found")));
            playerManager.removePlayer(nickname);
        } catch (PlayerInitException ignored) {
        }
    }

    /**
     * Each player needs a <code>StartingCard</code> at the beginning of the game. This method place
     * the picked one on the field
     *
     * @param nickname nickname of the player of interest
     * @param isRetro  tell if the <code>StartingCard</code> is placed on the front (false) or on
     *                 the back (true)
     * @throws IllegalCardPlacingException if there is already a card in that position
     * @throws GameStatusException         if the game is not ongoing
     */
    @Override //
    public void setStarterFor(@NotNull String nickname, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException {
        if (plateau.getStatus() != GameStatus.STARTING) {
            throw new GameStatusException("the game is not starting");
        }
        // TODO throws to complete
        Player player = playerManager.getPlayer(nickname).orElseThrow();
        player.field().placeStartingCard(playerManager.getStarterCard(nickname).orElseThrow(),
                                         isRetro);
        if (playerManager.areTheyReady()) {
            plateau.setStatus(GameStatus.ONGOING);
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
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException {
        if (plateau.getStatus() != GameStatus.STARTING) {
            throw new GameStatusException("the game is not starting");
        }
        ObjectiveCard objectiveCard = playerManager.getCandidateObjectiveByID(nickname, cardID);
        PersonalSpace playerSpace = playerManager.getPlayer(nickname)
                                                 .orElseThrow(() -> new PlayerInitException(
                                                         "Player not found"))
                                                 .space();
        playerSpace.removeCandidateObjective(cardID);
        playerSpace.addObjective(objectiveCard);
        if (playerManager.areTheyReady()) {
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
            plateau.getStatus() == GameStatus.STARTING) {
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
        if (player.space().hasCardBeenPlaced()) {
            throw new TurnsOrderException(nickname + " has already placed a card first");
        }

        PlayableCard card = pickablesTable.getPlayableByID(cardID)
                                          .orElseThrow(() -> new IllegalCardPlacingException(
                                                  "Card not found"));
        int points;
        Optional.of(player.field().isRequirementMet(card, isRetro))
                .filter(b -> b)
                .orElseThrow(
                        () -> new IllegalCardPlacingException(
                                "Card can't be placed in that position"));
        Optional.of(playerManager.getHand(nickname).contains(cardID))
                .filter(b -> b)
                .orElseThrow(
                        () -> new NotInHandException("Card not in hand"));
        points = player.field().place(card, position, isRetro);
        player.space().pickCard(cardID);
        player.space().setCardBeenPlaced(true);

        plateau.addPlayerPoints(player, points);
    }

    @Override
    public int drawFromDeckOf(@NotNull PlayableCardType type, @NotNull String nickname)
    throws GameStatusException, TurnsOrderException, EmptyDeckException,
           IllegalPlayerSpaceActionException, PlayerInitException, MaxHandSizeException,
           GameBreakingException {

        PersonalSpace playerSpace = playerManager.getPlayer(nickname)
                                                 .orElseThrow(() -> new PlayerInitException(
                                                         "Player not found"))
                                                 .space();

        checkIfDrawAllowed(nickname);

        if (playerSpace.availableSpaceInHand() >= 1) {
            PlayableCard card = pickablesTable.drawPlayableFrom(type);
            playerSpace.addCardToHand(card);
            goNextTurn();
            return card.getId();
        } else {
            throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
        }
    }

    private void checkIfDrawAllowed(String nickname)
    throws GameStatusException, TurnsOrderException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED ||
            plateau.getStatus() == GameStatus.STARTING) {
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
            throw new TurnsOrderException(nickname + " has to place a card first");
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
    // DONE
    private void goNextTurn() throws GameBreakingException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED ||
            plateau.getStatus() == GameStatus.STARTING) {
            throw new GameStatusException("the game is not ongoing");
        }
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
            plateau.setStatus(GameStatus.LAST_TURN);
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

    @Override
    public void drawVisibleOf(@NotNull PlayableCardType type, @NotNull String nickname, int cardID)
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
                goNextTurn();
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
        if (plateau.getStatus() != GameStatus.STARTING) {
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
        if (plateau.getStatus() != GameStatus.STARTING) {
            throw new GameStatusException("the game has not started");
        }
        return playerManager.getStarterCard(nickname)
                            .map(StarterCard::getId);
    }
}

