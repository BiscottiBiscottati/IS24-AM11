package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.Players.Colours;

public interface GameModel {
    public void initGame();
    public void addPlayerToTable(String nickname, Colours colour);
    public void shufflePlayers();
    public void setStartingPlayer();
    public void goNextTurn();
    public void placeCard();
    public void drawCardFrom();

    public void getExposedCards();
    public void getPlayerHand();
    public void getPlayerObjective();
    public void getPlateau();
    public void getPlayerInfo();
    public void getCurrentTurnPlayer();
    public void getCommonObjectives();
    public void isFinished();
    public void getResults();
    public void getWinner();
}
