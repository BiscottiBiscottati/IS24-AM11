package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.players.PersonalSpace;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.table.PickablesTable;
import it.polimi.ingsw.am11.table.Plateau;

import java.util.Collections;
import java.util.LinkedList;

public class GameLogic implements GameModel {

    private final LinkedList<Player> playerQueue;
    private int numOfPlayers;
    private Player firstPlayer;
    private Plateau gamePlateau;
    private PickablesTable gameTable;
    private Player currentPlaying;

    public GameLogic() {
        this.playerQueue = new LinkedList<Player>();
    }

    @Override
    public void initGame() {

    }

    @Override
    public void addPlayerToTable(String nickname, PlayerColor colour) {
        if (playerQueue.size() < numOfPlayers) {
            PlayerField newField = new PlayerField();
            PersonalSpace newSpace = new PersonalSpace();

            Player newPlayer = new Player(nickname, colour, newSpace, newField);
        } else {
            //NOTE: it should throw a SetPlayersLimitReachedException
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

    @Override
    public void getExposedCards() {

    }

    @Override
    public void getPlayerHand() {


    }

    @Override
    public void getPlayerObjective() {

    }

    @Override
    public void getPlateau() {

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
