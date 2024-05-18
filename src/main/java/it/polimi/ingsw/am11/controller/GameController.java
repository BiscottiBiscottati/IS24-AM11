package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController {
    private final GameModel model;
    private final List<PlayerColor> colors;

    public GameController(GameModel model) {
        this.model = model;
        colors = new ArrayList<>(Arrays.stream(PlayerColor.values()).toList());
        Collections.shuffle(colors);
    }

    void initGame() throws NumOfPlayersException,
                           GameStatusException {
        try {
            model.initGame();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    void goNextTurn() throws
                      GameStatusException {
        try {
            // TODO to handle disconnection in the model
            model.goNextTurn();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    void addPlayer(String nickname)
    throws PlayerInitException, GameStatusException, NumOfPlayersException {
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

    void reset() {
        colors.clear();
        colors.addAll(Arrays.asList(PlayerColor.values()));
    }
}
