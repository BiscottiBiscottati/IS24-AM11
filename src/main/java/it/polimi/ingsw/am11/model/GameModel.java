package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.Players.PlayerColor;

public interface GameModel {
    void initGame();

    void addPlayerToTable(String nickname, PlayerColor colour);

    void shufflePlayers();

    void setStartingPlayer();

    void goNextTurn();

    void placeCard();

    void drawCardFrom();

    void getExposedCards();

    void getPlayerHand();

    void getPlayerObjective();

    void getPlateau();

    void getPlayerInfo();

    void getCurrentTurnPlayer();

    void getCommonObjectives();

    void isFinished();

    void getResults();

    void getWinner();
}
