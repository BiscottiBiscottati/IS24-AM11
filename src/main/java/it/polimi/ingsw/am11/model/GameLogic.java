package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.*;
import it.polimi.ingsw.am11.players.field.PlayerField;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;

import java.util.*;

public class GameLogic implements GameModel {

    private final RuleSet ruleSet = new BasicRuleset();
    private final Map<String, Player> players;
    private final LinkedList<Player> playerQueue;
    private final PickablesTable pickablesTable;
    private final Plateau plateau;
    private Player firstPlayer;
    private Player currentPlaying;

    //region Constructor DONE
    public GameLogic() {
        this.players = new HashMap<>(8);
        this.playerQueue = new LinkedList<Player>();
        this.pickablesTable = new PickablesTable(ruleSet.getNumOfCommonObjectives(),
                                                 ruleSet.getMaxRevealedCardsPerType());
        this.plateau = new Plateau(ruleSet.getPointsToArmageddon());
    }
    //endregion

    //region GetterGameStatus DONE
    @Override //DONE
    public List<String> getPlayerListInOrder() {
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

    //endregion

    //region GettersPlayer
    @Override //DONE
    public List<Integer> getPlayerHand(String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerHand()
                      .stream()
                      .map(PlayableCard::getId)
                      .toList();
    }

    @Override //DONE
    public List<Integer> getPlayerObjective(String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerObjective()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .toList();
    }

    @Override //DONE
    public PlayerColor getPlayerColor(String nickname) {
        return players.get(nickname).color();
    }

    @Override //FIXME
    public Map<Position, CardContainer> getPositionedCard(String nickname) {
        return null;
    }

    @Override //DONE
    public Set<Position> getAvailablePositions(String nickname) {
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

    @Override
    public List<Integer> getExposedGoldsCrd() {
        return pickablesTable.getShownGold().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    @Override
    public List<Integer> getExposedResourcesCrd() {
        return pickablesTable.getShownResources().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }


    // TODO getResourceDeckTop and getGoldDeckTop can be combined into one method with argument the deckType to look
    @Override //DONE
    public Optional getResourceDeckTop() {
        return pickablesTable.getResourceDeckTop();
    }

    @Override //DONE
    public Optional getGoldDeckTop() {
        return pickablesTable.getGoldDeckTop();
    }

    //endregion

    // TODO the check for null can be done inside here may save time
    //region GettersPlateau DONE
    @Override //DONE
    public int getPlayerPoints(String nickname) throws IllegalPlateauActionException {
        return plateau.getPlayerPoints(players.get(nickname));
    }

    // TODO can be renamed into isArmageddonTime same as plateau to be less verbose
    @Override //DONE
    public boolean getIsArmageddonTime() {
        return plateau.isArmageddonTime();
    }

    @Override //DONE
    public int getPlayerFinishingPosition(String nickname) {
        return plateau.getPlayerFinihingPosition(players.get(nickname));
    }

    @Override //DONE
    public List<String> getWinner() {
        return plateau.getWinners()
                      .stream()
                      .map(Player::nickname)
                      .toList();
    }
    //endregion

    //region GameInitialization
    @Override //DONE
    public void initGame() throws IllegalNumOfPlayersException {
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
        pickablesTable.resetToInitialCondition();
        pickablesTable.shuffleAllDecks();
        pickablesTable.pickCommonObjectives();
        for (int i = 0; i < ruleSet.getMaxRevealedCardsPerType(); i++) {
            try {
                if (pickablesTable.supplyGoldVisibles().isEmpty()) {
                    plateau.setGoldDeckEmptyness(true);
                }
                if (pickablesTable.supplyResourceVisibles().isEmpty()) {
                    plateau.setResourceDeckEmptyness(true);
                }
            } catch (IllegalPickActionException e) {
                System.out.println(e);
                break;
            }

        }

    }

    // TODO I believe that the assignment of colors of each player
    //  can be done internally without the player having to choose.
    //  We could also make Player initialize automatically the field and the space when creating a instance of Player.

    // RESPONCE 1: I think that the possibility to chose his preferred color is a right
    // that every player should have, isn't this a free country?

    // RESPONCE 2: It is true that the player could initialize his own field and space,
    // if you want we can change, but this still works fine.
    @Override
    public void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException {
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
            PlayerField newField = new PlayerField();
            PersonalSpace newSpace = new PersonalSpace(ruleSet.getHandSize(), ruleSet.getNumOfPersonalObjective());
            Player newPlayer = new Player(nickname, colour, newSpace, newField);
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
        try {
            return pickablesTable.pickStarterCard().getId();
        } catch (EmptyDeckException ex) {
            throw ex;
        }
    }

    @Override //DONE
    public void setStarterFor(String nickname, int cardID, boolean isRetro) throws IllegalCardPlacingException {
        StarterCard starterCard = pickablesTable.getStarterByID(cardID).orElseThrow();
        players.get(nickname).field().placeStartingCard(starterCard, isRetro);
    }

    @Override //DONE
    public int pickObjective() throws EmptyDeckException {
        return pickablesTable.pickObjectiveCard().getId();
    }

    @Override //DONE
    public void setObjectiveFor(String nickname, int cardID) throws IllegalPlayerSpaceActionException {
        //The rules book says that unused objectives have to be returned to the deck, but it seems like
        //a useless action
        ObjectiveCard objectiveCard = pickablesTable.getObjectiveByID(cardID).orElseThrow();
        try {
            players.get(nickname).space().addObjective(objectiveCard);
        } catch (IllegalPlayerSpaceActionException e) {
            throw e;
        }
    }

    //endregion

    //region TurnsActions DONE
    @Override //DONE
    public void goNextTurn() {
        playerQueue.addLast(currentPlaying);
        currentPlaying = playerQueue.removeFirst();
    }

    @Override //DONE
    public void placeCard(String nickname, int ID, Position position, boolean isRetro) throws IllegalCardPlacingException, TurnsOrderException, IllegalPlateauActionException {
        Player player = players.get(nickname);
        if (currentPlaying != player) {
            throw new TurnsOrderException(
                    "It's not " + nickname + " turn, it's " + currentPlaying.nickname() + " turn."
            );
        }
        PlayableCard card = pickablesTable.getPlayableByID(ID).orElseThrow();
        if (player.field().isAvailable(position)) {
            try {
                if (player.field().isRequirementMet(card)) {
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

    @Override //DONE
    public int drawFromGoldDeck(String nickname)
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
        } catch (EmptyDeckException ex) {
            plateau.setGoldDeckEmptyness(true);
            throw ex;
        }
    }

    @Override //DONE
    public int drawFromResourceDeck(String nickname)
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
        } catch (EmptyDeckException ex) {
            plateau.setGoldDeckEmptyness(true);
            throw ex;
        }
    }

    @Override //DONE
    public void drawVisibleGold(String nickname, int ID)
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
        } catch (IllegalPickActionException ex) {
            throw ex;
        }

    }

    @Override //DONE
    public void drawVisibleResource(String nickname, int ID)
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
        } catch (IllegalPickActionException ex) {
            throw ex;
        }


    }

    //endregion
}
