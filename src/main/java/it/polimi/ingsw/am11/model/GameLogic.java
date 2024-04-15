package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.*;
import it.polimi.ingsw.am11.players.field.PlayerField;
import it.polimi.ingsw.am11.table.GameStatus;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameLogic implements GameModel {

    //TODO divide game logic in smaller classes like player manager and table manager turn manager

    private final RuleSet ruleSet = new BasicRuleset();
    private final Map<String, Player> players;
    private final LinkedList<Player> playerQueue;
    private final PickablesTable pickablesTable;
    private final Plateau plateau;
    private final boolean lastTurn;
    private Player firstPlayer;
    private Player currentPlaying;

    //region Constructor DONE
    public GameLogic() {
        this.players = new HashMap<>(8);
        this.playerQueue = new LinkedList<>();
        this.pickablesTable = new PickablesTable(ruleSet.getNumOfCommonObjectives(),
                                                 ruleSet.getMaxRevealedCardsPerType());
        this.plateau = new Plateau(ruleSet.getPointsToArmageddon());
        this.lastTurn = false;
    }
    //endregion

    //region GetterGameStatus DONE javadoc

    /**
     * Retrieves the nicknames of all the players of the current game.
     * <p>
     * The nickname is unique in the game and can be used to identify each specific
     * <code> Player </code>.
     * <p>
     * Nicknames are chosen at the beginning of the game.
     *
     * @return a list of the nicknames of the players
     */
    @Override //DONE
    public List<String> getPlayers() {
        return players.values()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }


    /**
     * Retrieves the nickname of the player whose turn it is.
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method returns <code> null
     * </code>.
     *
     * @return the nickname of the player that is playing or <code> null </code> if the game hasn't started
     */
    @Override //DONE
    public String getCurrentTurnPlayer() {
        return currentPlaying.nickname();
    }

    /**
     * Retrieves the nickname of the player that is first in the order of the turns
     * <p>
     * If the game has not been initialized with the method <code> .initGame </code> this method returns <code> null
     *
     * @return the nickname of the player that played the first turn or <code> null</code> if the game hasn't started
     */
    @Override //DONE
    public String getFirstPlayer() {
        return firstPlayer.nickname();
    }

    /**
     * When some conditions are met, (by default when the decks are empty or a player has reached a points' goal) the
     * final phase of the game starts.
     * <p>
     * Upon entering the final phase the current round of turns is completed and the last round is played.
     *
     * @return true if the game is in the final phase, false otherwise
     */
    @Override //DONE
    public boolean isArmageddonTime() {
        return plateau.isArmageddonTime();
    }

    //endregion

    //region GettersPlayer

    /**
     * Retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific player hand. The player is
     * identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>PlayableCards</code> in the player hand
     */
    @Override //DONE
    public List<Integer> getPlayerHand(@NotNull String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerHand()
                      .stream()
                      .map(PlayableCard::getId)
                      .toList();
    }

    /**
     * Each player can have one or more personal objectives represented by the <code>ObjectiveCards</code>.
     * <p>
     * This method retrieves the identifier (ID) of all the <code>PlayableCards</code> in a specific player hand.
     * <p>
     * The player is identified by his nickname.
     *
     * @param nickname Nickname of the player of interest
     * @return a <code>List</code> of the IDs of the <code>ObjectiveCards</code> of the player
     */
    @Override //DONE
    public List<Integer> getPlayerObjective(@NotNull String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerObjective()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .toList();
    }

    /**
     * Each player have an assigned color for his pawn.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>PlayerColor</code> of the player
     */
    @Override //DONE
    public PlayerColor getPlayerColor(@NotNull String nickname) {
        return players.get(nickname).color();
    }

    @Override //TODO model shouldn't return something that has a reference of a Card, it should use ID
    public Map<Position, CardContainer> getPositionedCard(@NotNull String nickname) {
        return players.get(nickname).field().getCardsPositioned();
    }

    /**
     * For placing a card on the field a set of conditions has to be satisfied.
     * <p>
     * The new card needs to touch at least one of the corners of another placed card and all the corners of placed
     * cards touched by the new card must not be NOT_USABLE.
     * <p>
     * This method retrieves the positions that satisfy these conditions.
     *
     * @param nickname Nickname of the player of interest
     * @return the <code>Set</code> of <code>Position</code>s that are available for placing a card in the field
     */
    @Override //DONE
    public Set<Position> getAvailablePositions(@NotNull String nickname) {
        return players.get(nickname).field().getAvailablePositions();
    }

    //endregion

    //region GettersPickableTable DONE javadoc

    /**
     * During a game players have objectives in common
     *
     * @return a list of ID of <code>ObjectiveCard</code> that represent the common objectives
     */
    @Override //DONE
    public List<Integer> getCommonObjectives() {
        return pickablesTable.getCommonObjectives()
                             .stream()
                             .map(ObjectiveCard::getId)
                             .toList();
    }

    /**
     * During the game some of the pickable cards are visible
     *
     * @return a list of ID of <code>GoldCard</code> exposed on the table
     */
    @Override //DONE
    public List<Integer> getExposedGoldsCrd() {
        return pickablesTable.getShownGold().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    /**
     * During the game some of the pickable cards are visible
     *
     * @return a list of ID of <code>ResourceCard</code> exposed on the table
     */
    @Override //DONE
    public List<Integer> getExposedResourcesCrd() {
        return pickablesTable.getShownResources().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    /**
     * @return the color of the card on top of the <code>ResourceCard</code>s deck
     */
    @Override //DONE
    public Optional<Color> getResourceDeckTop() {
        return pickablesTable.getResourceDeckTop();
    }

    /**
     * @return the color of the card on top of the <code>GoldCard</code>s deck
     */
    @Override //DONE
    public Optional<Color> getGoldDeckTop() {
        return pickablesTable.getGoldDeckTop();
    }

    //endregion

    //region GettersPlateau DONE javadoc

    /**
     * Retrive the points that a player has obtained since the start of the game
     *
     * @param nickname nickname of the player of interest
     * @return the points of the player
     * @throws IllegalPlateauActionException if there isn't a player with that nickname
     */
    @Override //DONE
    public int getPlayerPoints(@NotNull String nickname) throws IllegalPlateauActionException {
        return plateau.getPlayerPoints(players.get(nickname));
    }

    /**
     * Retrieve the ranking of a player at the end of the game, there could be more player whit the same ranking
     *
     * @param nickname of the player of interest
     * @return the position in the leaderboard of the player
     */
    @Override //DONE
    public int getPlayerFinishingPosition(@NotNull String nickname) {
        return plateau.getPlayerFinishingPosition(players.get(nickname));
    }

    /**
     * This method return the winners, there could be more than on winners.
     *
     * @return a list with the names of the winners
     */
    @Override //DONE
    public List<String> getWinner() {
        return plateau.getWinners()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }
    //endregion

    //region GameInitialization DONE javadoc

    /**
     * This method initialize a new game, it has to be called after all the players have joined the games This method
     * shuffle the players Set in order to randomize the turns order, it chooses the first player, it clears the field,
     * the hand and the objectives for each player, it resets the plateau and the pickableTable.
     *
     * @throws IllegalNumOfPlayersException If there aren't at least 2 players in the game
     * @throws GameStatusException          if a game is in progress
     */
    @Override //DONE
    public void initGame() throws IllegalNumOfPlayersException, GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        if (players.size() < 2) {
            throw new IllegalNumOfPlayersException(
                    "You need at least 2 players to play this game, the current number is: " +
                    players.size()
            );
        }
        shufflePlayers();
        setStartingPlayer();
        players.values().stream()
               .map(Player::space)
               .forEach(PersonalSpace::clearAll);
        players.values().stream()
               .map(Player::field)
               .forEach(PlayerField::clearAll);
        plateau.reset();
        pickablesTable.initialize();
    }

    /**
     * This method can be used to add a new player in the game during the setting up phase.
     * <p>
     * A player can choose his nickname and color.
     *
     * @param nickname nickname of the new player
     * @param colour   color of the new player
     * @throws PlayerInitException if there is another player with the chosen nickname, if the colour is already taken,
     *                             if the limit of players per game has already been reached
     * @throws GameStatusException if a game is in progress
     */
    @Override //DONE
    public void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
    throws PlayerInitException, GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        if (players.containsKey(nickname)) {
            throw new PlayerInitException(nickname + " is already in use");
        } else if (players.values()
                          .stream()
                          .map(Player::color)
                          .anyMatch(playerColor -> playerColor.equals(colour))) {
            throw new PlayerInitException(
                    "Colour already in use: " + colour
            );
        } else if (players.size() < ruleSet.getMaxPlayers()) {
            throw new PlayerInitException(
                    "You are trying to add too many players, the limit is " +
                    ruleSet.getMaxPlayers()
            );
        } else {
            PersonalSpace newSpace = new PersonalSpace(ruleSet.getHandSize(),
                                                       ruleSet.getNumOfPersonalObjective());
            Player newPlayer = new Player(nickname, colour, newSpace);
            players.put(nickname, newPlayer);
            playerQueue.add(newPlayer);
            plateau.addPlayer(newPlayer);
        }
    }

    /**
     * Remove a player from the player list.
     *
     * @param nickname nickname of the player of interest
     * @throws GameStatusException if a game is in progress
     */
    @Override //DONE
    public void removePlayer(@NotNull String nickname) throws GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        players.remove(nickname);
        plateau.removePlayer();
    }

    /**
     * Shuffle the players in order to randomize the turns order.
     *
     * @throws GameStatusException if a game is in progress
     */
    @Override //DONE
    public void shufflePlayers() throws GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        Collections.shuffle(playerQueue);
    }

    /**
     * Set the player that plays the first turn.
     *
     * @throws GameStatusException if a game is in progress
     */
    @Override //DONE
    public void setStartingPlayer() throws GameStatusException {
        if (plateau.getStatus() != GameStatus.SETUP) {
            throw new GameStatusException("A game is in progress");
        }
        firstPlayer = playerQueue.getFirst();
        currentPlaying = playerQueue.removeFirst();
    }

    /**
     * Pick a <code>StarterCard</code> from the deck on the <code>PickablesTable</code>.
     *
     * @return the ID of a <code>StarterCard</code>
     * @throws EmptyDeckException  if the deck of  <code>StartingCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */
    @Override //DONE
    public int pickStarter() throws EmptyDeckException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        return pickablesTable.pickStarterCard().getId();
    }

    /**
     * Each player needs a <code>StartingCard</code> at the beginning of the game.
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the <code>StartingCard</code> that has to be placed on the field
     * @param isRetro  tell if the <code>StartingCard</code> is placed on the front (false) or on the back (true)
     * @throws IllegalCardPlacingException if there is already card in that position
     * @throws GameStatusException         if the game is not ongoing
     */
    @Override //DONE
    public void setStarterFor(@NotNull String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        StarterCard starterCard = pickablesTable.getStarterByID(cardID).orElseThrow();
        players.get(nickname).field().placeStartingCard(starterCard, isRetro);
    }

    /**
     * Pick a <code>ObjectiveCard</code> from the deck on the <code>PickablesTable</code>.
     *
     * @return the ID of a <code>ObjectiveCard</code>
     * @throws EmptyDeckException  if the deck of  <code>ObjectiveCard</code> is empty
     * @throws GameStatusException if the game is not ongoing
     */
    @Override //DONE
    public int pickObjective() throws EmptyDeckException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        return pickablesTable.pickObjectiveCard().getId();
    }

    /**
     * Set a new personal objective for a player.
     *
     * @param nickname nickname of the player of interest
     * @param cardID   identifier of the <code>ObjectiveCard</code> to be assigned to the player
     * @throws IllegalPlayerSpaceActionException if there are already too many objectives
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //DONE
    public void setObjectiveFor(@NotNull String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException {
        //The rules book says that unused objectives have to be returned to the deck, but it seems like
        //a useless action
        if (plateau.getStatus() != GameStatus.ONGOING) {
            throw new GameStatusException("the game is not ongoing");
        }
        ObjectiveCard objectiveCard = pickablesTable.getObjectiveByID(cardID).orElseThrow();
        players.get(nickname).space().addObjective(objectiveCard);
    }

    //endregion

    //region TurnsActions DONE javadoc

    /**
     * This method handle the changing of the turn and the final turn. If it is not the final turn it checks if it's
     * armageddonTime and gives the turn to the next players. If it's the final turn it ends the game and prepare the
     * final leaderboard.
     *
     * @throws GameBreakingException if there are discrepancies between plateau and gameLogic
     * @throws GameStatusException   if the game is not ongoing
     */
    @Override //DONE
    public void goNextTurn() throws GameBreakingException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        if (pickablesTable.getGoldDeckTop().isPresent() &&
            pickablesTable.getResourceDeckTop().isPresent()) {
            plateau.activateArmageddon();
        }
        playerQueue.addLast(currentPlaying);
        currentPlaying = playerQueue.removeFirst();
        if (lastTurn && currentPlaying == firstPlayer) {
            plateau.setStatus(GameStatus.ENDED);
            try {
                countObjectivesPoints();
            } catch (IllegalPlateauActionException ex) {
                throw new GameBreakingException("Players in game logic and plateau don't match");
            }
            plateau.setFinalLeaderboard(); //FIXME it may be wrong
        } else if (isArmageddonTime() && currentPlaying == firstPlayer) {
            plateau.setStatus(GameStatus.ARMAGEDDON);
        }
    }

    /**
     * This method is used to place new playable cards on the field
     *
     * @param nickname nickname of the player of interest
     * @param ID       identifier of the card to be placed
     * @param position position where the player wants to place the card
     * @param isRetro  false if the card has to be placed with the front facing up, true otherwise
     * @throws IllegalCardPlacingException   if the card can't be placed in that position
     * @throws TurnsOrderException           if it's not that player turn
     * @throws IllegalPlateauActionException if there isn't any player with that nickname
     * @throws GameStatusException           if the game is not ongoing
     */
    @Override //DONE
    public void placeCard(@NotNull String nickname, int ID, @NotNull Position position, boolean isRetro)
    throws IllegalCardPlacingException, TurnsOrderException, IllegalPlateauActionException, GameStatusException,
           NotInHandException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        Player player = players.get(nickname);
        if (currentPlaying != player) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        PlayableCard card = pickablesTable.getPlayableByID(ID).orElseThrow();
        if (player.field().isAvailable(position)) {
            if (player.field().isRequirementMet(card, isRetro)) {
                int points = player.field().place(card, position, isRetro);
                player.space().pickCard(card);
                plateau.addPlayerPoints(player, points);
            }
        } else {
            throw new IllegalCardPlacingException("Chosen position is not available");
        }
    }

    /**
     * This method transfer a <code>GoldCard</code> from the top of the gold deck to the hand of the player
     *
     * @param nickname nickname of the player of interest
     * @return the ID of the picked <code>GoldCard</code>
     * @throws GameBreakingException             if an unexpected modification of the hand has caused a card to be lost
     * @throws EmptyDeckException                if the deck is empty
     * @throws IllegalPlayerSpaceActionException if the hand is already full
     * @throws TurnsOrderException               if it's not that player turn
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //DONE
    public int drawFromGoldDeck(@NotNull String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException,
           TurnsOrderException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        if (currentPlaying != players.get(nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableCardFrom(PlayableCardType.GOLD);
                players.get(nickname).space().addCardToHand(card);
                assert card != null;
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

    /**
     * This method transfer a <code>ResourceCard</code> from the top of the resource deck to the hand of the player
     *
     * @param nickname nickname of the player of interest
     * @return the ID of the picked <code>ResourceCard</code>
     * @throws GameBreakingException             if an unexpected modification of the hand has caused a card to be lost
     * @throws EmptyDeckException                if the deck is empty
     * @throws IllegalPlayerSpaceActionException if the hand is already full
     * @throws TurnsOrderException               if it's not that player turn
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //DONE
    public int drawFromResourceDeck(@NotNull String nickname)
    throws
    GameBreakingException,
    EmptyDeckException,
    IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        if (currentPlaying != players.get(nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableCardFrom(PlayableCardType.RESOURCE);
                players.get(nickname).space().addCardToHand(card);
                assert card != null;
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

    /**
     * This method transfer a <code>GoldCard</code> from the visible gold cards to the hand of the player
     *
     * @param nickname nickname of the player of interest
     * @param ID       identifier of the card to be picked
     * @throws GameBreakingException             if an unexpected modification of the hand has caused a card to be lost
     * @throws IllegalPickActionException        if the specified card can't be found
     * @throws IllegalPlayerSpaceActionException if the hand is already full
     * @throws TurnsOrderException               if it's not that player turn
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //DONE
    public void drawVisibleGold(@NotNull String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        if (currentPlaying != players.get(nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickGoldVisible(ID);
                players.get(nickname).space().addCardToHand(card);
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
     * This method transfer a <code>ResourceCard</code> from the visible resource cards to the hand of the player.
     *
     * @param nickname nickname of the player of interest
     * @param ID       identifier of the card to be picked
     * @throws GameBreakingException             if an unexpected modification of the hand has caused a card to be lost
     * @throws IllegalPickActionException        if the specified card can't be found
     * @throws IllegalPlayerSpaceActionException if the hand is already full
     * @throws TurnsOrderException               if it's not that player turn
     * @throws GameStatusException               if the game is not ongoing
     */
    @Override //DONE
    public void drawVisibleResource(@NotNull String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException {
        if (plateau.getStatus() == GameStatus.SETUP || plateau.getStatus() == GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        if (currentPlaying != players.get(nickname)) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickResourceVisible(ID);
                players.get(nickname).space().addCardToHand(card);
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

    //endregion

    //region GameEnding DONE javadoc

    /**
     * calculate the points from common and personal objectives for each player and add them to the plateau
     *
     * @throws IllegalPlateauActionException if a player is not found
     * @throws GameStatusException           if the game is not ongoing
     */
    @Override //DONE
    public void countObjectivesPoints() throws IllegalPlateauActionException, GameStatusException {
        if (plateau.getStatus() != GameStatus.ENDED) {
            throw new GameStatusException("the game is not ongoing");
        }
        for (Player player : players.values()) {
            for (ObjectiveCard commonObjective : pickablesTable.getCommonObjectives()) {
                int points = commonObjective.countPoints(player.field());
                if (points > 0) {
                    plateau.addCounterObjective(player, 1);
                }
                plateau.addPlayerPoints(player, points);
            }
            for (ObjectiveCard privateObjective : player.space().getPlayerObjective()) {
                int points = privateObjective.countPoints(player.field());
                if (points > 0) {
                    plateau.addCounterObjective(player, 1);
                }
                plateau.addPlayerPoints(player, points);
            }
        }
    }

    /**
     * Ends the current game and gives the possiblity to add or remove players.
     */
    @Override //DONE
    public void endGame() {
        plateau.setStatus(GameStatus.SETUP);
    }


    //endregion
}
