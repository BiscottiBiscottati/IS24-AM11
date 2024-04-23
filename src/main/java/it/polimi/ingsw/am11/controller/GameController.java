package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.IllegalNumOfPlayersException;

public class GameController {
    private final GameModel model;

    public GameController(GameModel model) {
        this.model = model;

    }

    public void initGame() throws IllegalNumOfPlayersException,
                                  GameStatusException,
                                  GameBreakingException {
        model.initGame();
    }

    public void goNextTurn() throws GameBreakingException,
                                    GameStatusException {
        model.goNextTurn();
    }

    public void forceEnd() {
        model.forceEnd();
    }
}
