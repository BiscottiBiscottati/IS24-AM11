package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.Players.PersonalSpace;
import it.polimi.ingsw.am11.Players.Player;
import it.polimi.ingsw.am11.Players.Colours;
import it.polimi.ingsw.am11.Players.PlayerField;
import it.polimi.ingsw.am11.Table.PickablesTable;
import it.polimi.ingsw.am11.Table.Plateau;

import java.util.*;

public class GameLogic implements GameModel{

    private int numOfPlayers;
    private LinkedList<Player> playerQueue;
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
    public void addPlayerToTable(String nickname, Colours colour) {
        if(playerQueue.size() < numOfPlayers) {
            PlayerField newField = new PlayerField();
            PersonalSpace newSpace = new PersonalSpace();

            Player newPlayer = new Player(nickname, colour, newSpace, newField);
        }
        else{
            //NOTE: it should throw a SetPlayersLimitReachedException
        }
    }

    @Override
    public void shufflePlayers(){
        Collections.shuffle(playerQueue);

    };

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
