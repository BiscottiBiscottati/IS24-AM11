package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.*;
import it.polimi.ingsw.am11.players.field.PlayerField;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameLogic implements GameModel {

    //TODO divede game logic in smaller classes like player manager and table manager turn manager
    //
    //TODO isfinished
    //
    //TODO check if action is in player turn
    //
    //
    //TODO

    private final RuleSet ruleSet = new BasicRuleset();
    private final Map<String, Player> players;
    private final LinkedList<Player> playerQueue;
    private final PickablesTable pickablesTable;
    private final Plateau plateau;
    private Player firstPlayer;
    private Player currentPlaying;
    private boolean lastTurn;

    //region Constructor DONE
    public GameLogic() {
        this.players = new HashMap<>(8);
        this.playerQueue = new LinkedList<Player>();
        this.pickablesTable = new PickablesTable(ruleSet.getNumOfCommonObjectives(),
                                                 ruleSet.getMaxRevealedCardsPerType());
        this.plateau = new Plateau(ruleSet.getPointsToArmageddon());
        this.lastTurn = false;
    }
    //endregion

    //region GetterGameStatus DONE
    @Override //DONE
    public List<String> getPlayers() {
        return players.values()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }

    @Override //DONE
    public String getCurrentTurnPlayer() {
        return currentPlaying.nickname();
    }

    @Override //DONE
    public String getFirstPlayer() {
        return firstPlayer.nickname();
    }

    @Override //DONE
    public boolean isArmageddonTime() {
        return plateau.isArmageddonTime();
    }

    //endregion

    //region GettersPlayer DONE
    @Override //DONE
    public List<Integer> getPlayerHand(@NotNull String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerHand()
                      .stream()
                      .map(PlayableCard::getId)
                      .toList();
    }

    @Override //DONE
    public List<Integer> getPlayerObjective(@NotNull String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerObjective()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .toList();
    }

    @Override //DONE
    public PlayerColor getPlayerColor(@NotNull String nickname) {
        return players.get(nickname).color();
    }

    @Override //DONE
    public Map<Position, CardContainer> getPositionedCard(@NotNull String nickname) {
        return players.get(nickname).field().getCardsPositioned();
    }

    @Override //DONE
    public Set<Position> getAvailablePositions(@NotNull String nickname) {
        return players.get(nickname).field().getAvailablePositions();
    }
    //endregion

    //region GettersPickableTable DONE
    @Override //DONE
    public List<Integer> getCommonObjectives() {
        return pickablesTable.getCommonObjectives()
                             .stream()
                             .map(ObjectiveCard::getId)
                             .toList();
    }

    @Override //DONE
    public List<Integer> getExposedGoldsCrd() {
        return pickablesTable.getShownGold().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    @Override //DONE
    public List<Integer> getExposedResourcesCrd() {
        return pickablesTable.getShownResources().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }


    @Override //DONE
    public Optional<Color> getResourceDeckTop() {
        return pickablesTable.getResourceDeckTop();
    }

    @Override //DONE
    public Optional<Color> getGoldDeckTop() {
        return pickablesTable.getGoldDeckTop();
    }

    //endregion

    //region GettersPlateau DONE
    @Override //DONE
    public int getPlayerPoints(@NotNull String nickname) throws IllegalPlateauActionException {
        return plateau.getPlayerPoints(players.get(nickname));
    }

    @Override //DONE
    public int getPlayerFinishingPosition(@NotNull String nickname) {
        return plateau.getPlayerFinishingPosition(players.get(nickname));
    }

    @Override //DONE
    public List<String> getWinner() {
        return plateau.getWinners()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }
    //endregion

    //region GameInitialization DONE
    @Override //DONE
    public void initGame() throws IllegalNumOfPlayersException, EmptyDeckException {
        if (players.size() < 2) {
            throw new IllegalNumOfPlayersException(
                    "You need at least 2 players to play this game, the current number is: " + players.size()
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

    @Override //DONE
    public void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour) throws PlayerInitException {
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
                    "You are trying to add too many players, the limit is " + ruleSet.getMaxPlayers()
            );
        } else {
            PersonalSpace newSpace = new PersonalSpace(ruleSet.getHandSize(), ruleSet.getNumOfPersonalObjective());
            Player newPlayer = new Player(nickname, colour, newSpace);
            players.put(nickname, newPlayer);
            playerQueue.add(newPlayer);
            plateau.addPlayer(newPlayer);
        }
    }

    @Override //DONE
    public void shufflePlayers() {
        Collections.shuffle(playerQueue);
    }


    @Override //DONE
    public void setStartingPlayer() {
        firstPlayer = playerQueue.getFirst();
        currentPlaying = playerQueue.removeFirst();
    }

    @Override //DONE
    public int pickStarter() throws EmptyDeckException {
        return pickablesTable.pickStarterCard().getId();
    }

    @Override //DONE
    public void setStarterFor(@NotNull String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException {
        StarterCard starterCard = pickablesTable.getStarterByID(cardID).orElseThrow();
        players.get(nickname).field().placeStartingCard(starterCard, isRetro);
    }

    @Override //DONE
    public int pickObjective() throws EmptyDeckException {
        return pickablesTable.pickObjectiveCard().getId();
    }

    @Override //DONE
    public void setObjectiveFor(@NotNull String nickname, int cardID) throws IllegalPlayerSpaceActionException {
        //The rules book says that unused objectives have to be returned to the deck, but it seems like
        //a useless action
        ObjectiveCard objectiveCard = pickablesTable.getObjectiveByID(cardID).orElseThrow();
        players.get(nickname).space().addObjective(objectiveCard);
    }

    //endregion

    //region TurnsActions DONE
    @Override //DONE
    public void goNextTurn() {
        if (pickablesTable.getGoldDeckTop().isPresent() &&
            pickablesTable.getResourceDeckTop().isPresent()) {
            plateau.activateArmageddon();
        }
        playerQueue.addLast(currentPlaying);
        currentPlaying = playerQueue.removeFirst();
        if (lastTurn && currentPlaying == firstPlayer) {
            //set gameStatus to ended
        } else if (isArmageddonTime() && currentPlaying == firstPlayer) {
            lastTurn = true;
        }
    }

    @Override
    public void placeCard(@NotNull String nickname, int ID, @NotNull Position position, boolean isRetro)
    throws IllegalCardPlacingException, TurnsOrderException, IllegalPlateauActionException {
        Player player = players.get(nickname);
        if (currentPlaying != player) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        PlayableCard card = pickablesTable.getPlayableByID(ID).orElseThrow();
        if (player.field().isAvailable(position)) {
            try {
                if (player.field().isRequirementMet(card, isRetro)) {
                    int points = player.field().place(card, position, isRetro);
                    player.space().pickCard(card);
                    plateau.addPlayerPoints(player, points);
                }
            } catch (IllegalPlateauActionException | IllegalCardPlacingException ex) {
                throw ex;
            }
        } else {
            throw new IllegalCardPlacingException("Chosen position is not available");
        }
    }

    @Override
    public int drawFromGoldDeck(@NotNull String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException {
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableCardFrom(PlayableCardType.GOLD);
                players.get(nickname).space().addCardToHand(card);
                return card.getId();
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the deck and not being able to put it anywhere"
            );
        }
    }

    @Override
    public int drawFromResourceDeck(@NotNull String nickname)
    throws
    GameBreakingException,
    EmptyDeckException,
    IllegalPlayerSpaceActionException {
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickPlayableCardFrom(PlayableCardType.RESOURCE);
                players.get(nickname).space().addCardToHand(card);
                return card.getId();
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the deck and not being able to put it anywhere"
            );
        }
    }

    @Override
    public void drawVisibleGold(@NotNull String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException {
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickGoldVisible(ID);
                players.get(nickname).space().addCardToHand(card);
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the visibles and not being able to put it anywhere"
            );
        }

    }

    @Override
    public void drawVisibleResource(@NotNull String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException {
        try {
            if (players.get(nickname).space().availableSpaceInHand() >= 1) {
                PlayableCard card = pickablesTable.pickResourceVisibles(ID);
                players.get(nickname).space().addCardToHand(card);
            } else {
                throw new IllegalPlayerSpaceActionException(nickname + " hand is already full");
            }
        } catch (MaxHandSizeException ex) {
            throw new GameBreakingException(
                    "We have lost a card due to picking it from the visibles and not being able to put it anywhere"
            );
        }


    }

    //endregion

    //region GameEnding


    @Override
    //TODO add possibility of signaling how many objectives a player has completed
    public void countObjectivesPoints() throws IllegalPlateauActionException {
        for (Player player : players.values()) {
            for (ObjectiveCard commonObjective : pickablesTable.getCommonObjectives()) {
                plateau.addPlayerPoints(player, commonObjective.countPoints(player.field()));
            }
            for (ObjectiveCard privateObjective : player.space().getPlayerObjective()) {
                plateau.addPlayerPoints(player, privateObjective.countPoints(player.field()));
            }
        }
    }

    //endregion
}
