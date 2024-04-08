package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.*;
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

    //region Constructor
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
                      .map(Player::getNickname)
                      .toList();
    }

    @Override //DONE
    public String getCurrentTurnPlayer() {
        return currentPlaying.getNickname();
    }

    @Override //DONE
    public String getFirstPlayer() {
        return firstPlayer.getNickname();
    }

    //endregion

    //region GettersPlayer
    @Override //DONE
    public List<Integer> getPlayerHand(String nickname) {
        return players.get(nickname)
                      .getSpace()
                      .getPlayerHand()
                      .stream()
                      .map(PlayableCard::getId)
                      .toList();
    }

    @Override //DONE
    public List<Integer> getPlayerObjective(String nickname) {
        return players.get(nickname)
                      .getSpace()
                      .getPlayerObjective()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .toList();
    }

    @Override //DONE
    public PlayerColor getPlayerColor(String nickname) {
        return players.get(nickname).getColor();
    }

    @Override //FIXME
    public Map<Position, CardContainer> getPositionedCard(String nickname) {
        return null;
    }

    @Override //DONE
    public Set<Position> getAvailablePositions(String nickname) {
        return players.get(nickname).getField().getAvailablePositions();
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
    public List<Integer> getExposedCards() {
        return pickablesTable.getShownCards().stream()
                             .map(PlayableCard::getId)
                             .toList();
    }

    @Override //DONE
    public Color getResourceDeckTop() {
        return pickablesTable.getResourceDeckTop();
    }

    @Override //DONE
    public Color getGoldDeckTop() {
        return pickablesTable.getGoldDeckTop();
    }

    //endregion

    //region GettersPlateau DONE
    @Override //DONE
    public int getPlayerPoints(String nickname) throws IllegalPlateauActionException {
        return plateau.getPlayerPoints(players.get(nickname));
    }

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
        return plateau.getWinners().stream().map(Player::getNickname).toList();
    }
    //endregion

    //region GameInitialization

    //get launched after all players are ready:
    //- prepare player fields;
    //- prepare decks;
    //-
    @Override
    public void initGame() {

    }

    @Override
    public void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException {
        if (players.containsKey(nickname)) {
            throw new PlayerInitException(nickname + " is already in use");
        } else if (players.values()
                          .stream()
                          .map(Player::getColor)
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

    @Override
    public void shufflePlayers() {
        Collections.shuffle(playerQueue);
    }


    @Override
    public void setStartingPlayer() {
        firstPlayer = playerQueue.getFirst();
        currentPlaying = playerQueue.removeFirst();
    }

    //endregion

    //region TurnsActions
    @Override
    public void goNextTurn() {
        playerQueue.addLast(currentPlaying);
        currentPlaying = playerQueue.removeFirst();
    }

    @Override
    public void placeCard() {

    }

    @Override
    public void drawCardFrom() {

    }
    //endregion
}
