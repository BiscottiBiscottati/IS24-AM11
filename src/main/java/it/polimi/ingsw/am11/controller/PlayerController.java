package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerController {

    private final GameModel model;
    private final List<PlayerColor> colors;

    public PlayerController(GameModel model) {
        this.model = model;
        colors = Arrays.asList(PlayerColor.values());
        Collections.shuffle(colors);
    }

    public void addPlayer(String nickname)
    throws PlayerInitException, GameStatusException {
        if (colors.isEmpty()) {
            throw new PlayerInitException("No more colors available");
        }
        PlayerColor color = colors.removeFirst();
        model.addPlayerToTable(nickname, color);
    }

    public void removePlayer(String nickname) throws GameStatusException {
        model.removePlayer(nickname);
    }
}
