package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;

import java.util.List;

public interface GameModel {
    void initGame();

    void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException;

    void shufflePlayers();

    void setStartingPlayer();

    void goNextTurn();

    void placeCard();

    void drawCardFrom();

    void getExposedCards();

    List<Integer> getPlayerHand(String nickname);

    List<Integer> getPlayerObjective(String nickname);

    int getPlayerPoints(String nickname) throws IllegalPlateauActionException;

    void getPlayerInfo();

    void getCurrentTurnPlayer();

    void getCommonObjectives();

    void isFinished();

    void getResults();

    void getWinner();
}
