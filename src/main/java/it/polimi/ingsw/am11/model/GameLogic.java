package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PersonalSpace;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;

import java.util.*;

public class GameLogic implements GameModel {

    private final RuleSet ruleSet = new BasicRuleset();
    private final Map<String, Player> players;
    private final LinkedList<Player> playersQueue;
    private final PickablesTable pickablesTable;
    private final Plateau plateau;
    private Player firstPlayer;
    private Plateau gamePlateau;
    private PickablesTable gameTable;
    private Player currentPlaying;


    public GameLogic() {
        this.players = new HashMap<>(8);
        this.playersQueue = new LinkedList<Player>();
        this.pickablesTable = new PickablesTable(ruleSet.getNumOfCommonObjectives(),
                                                 ruleSet.getMaxRevealedCardsPerType());
        this.plateau = new Plateau(ruleSet.getPointsToArmageddon());
    }

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
            playersQueue.add(newPlayer);
            plateau.addPlayer(newPlayer);
        }
    }

    @Override
    public void shufflePlayers() {
        Collections.shuffle(playersQueue);
    }

    @Override
    public void setStartingPlayer() {
        firstPlayer = playersQueue.getFirst();
        currentPlaying = playersQueue.removeFirst();
    }

    @Override
    public void goNextTurn() {
        playersQueue.addLast(currentPlaying);
        currentPlaying = playersQueue.removeFirst();
    }

    @Override
    public void placeCard() {

    }

    @Override
    public void drawCardFrom() {

    }

    @Override
    public void getExposedCards() {

    }

    @Override
    public List<Integer> getPlayerHand(String nickname) {
        return players.get(nickname)
                      .getSpace()
                      .getPlayerHand()
                      .stream()
                      .map(p -> p.getId())
                      .toList();
    }

    @Override
    public List<Integer> getPlayerObjective(String nickname) {
        return players.get(nickname)
                      .getSpace()
                      .getPlayerObjective()
                      .stream()
                      .map(p -> p.getId())
                      .toList();
    }

    @Override
    public int getPlayerPoints(String nickname) throws IllegalPlateauActionException {
        return plateau.getPlayerPoints(players.get(nickname));
    }


    @Override
    public void getPlayerInfo() {

    }

    @Override
    public void getCurrentTurnPlayer() {

    }

    @Override
    public void getCommonObjectives() {

    }

    @Override
    public void isFinished() {

    }

    @Override
    public void getResults() {

    }

    @Override
    public void getWinner() {

    }
}
