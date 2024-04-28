package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.network.RMI.GameControllerInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController implements GameControllerInterface {
    private final GameModel model;
    private final List<PlayerColor> colors;

    public GameController(GameModel model) {
        this.model = model;
        colors = Arrays.asList(PlayerColor.values());
        Collections.shuffle(colors);
    }

    public void initGame() throws IllegalNumOfPlayersException,
                                  GameStatusException {
        try {
            model.initGame();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    public void goNextTurn() throws
                             GameStatusException {
        try {
            // TODO to handle disconnection in the model
            model.goNextTurn();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(String nickname)
    throws PlayerInitException, GameStatusException, MaxPlayersReachedException {
        if (colors.isEmpty()) {
            throw new PlayerInitException("No more colors available");
        }
        PlayerColor color = colors.removeFirst();
        model.addPlayerToTable(nickname, color);
    }

    public void removePlayer(String nickname) throws GameStatusException {
        model.removePlayer(nickname);
    }

    public void forceEnd() {
        model.forceEnd();
    }
}
