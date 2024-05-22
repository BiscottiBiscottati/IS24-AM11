package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum CentralController {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(CentralController.class);

    private final List<GameController> gameControllers;

    CentralController() {
        this.gameControllers = new ArrayList<>(4);
    }

    @NotNull
    public synchronized VirtualPlayerView connectPlayer(@NotNull String nickname,
                                                        @NotNull PlayerConnector playerConnector,
                                                        @NotNull TableConnector tableConnector)
    throws GameStatusException, NumOfPlayersException, PlayerInitException,
           NotSetNumOfPlayerException {
        if (gameControllers.isEmpty()) initGame();

        return gameControllers.stream()
                              .findFirst()
                              .orElseThrow()
                              .connectPlayer(nickname, playerConnector, tableConnector);
    }

    @Contract(" -> new")
    public synchronized @NotNull GameController initGame() {
        LOGGER.info("Creating new game");
        GameController newGame = new GameController();
        this.gameControllers.add(newGame);
        return newGame;
    }

    public synchronized void setNumOfPlayers(@NotNull String nickname, int val)
    throws NotGodPlayerException, GameStatusException,
           NumOfPlayersException {
        gameControllers.stream().findFirst()
                       .orElseThrow()
                       .setNumOfPlayers(nickname, val);
    }

    public void playerDisconnected(String nickname) {
        //TODO to implement
    }

    public void playerReconnected(String nickname) {
        //TODO to implement
    }

    public synchronized @Nullable String getGodPlayer() {
        return gameControllers.stream().findFirst()
                              .orElseThrow()
                              .getGodPlayer();
    }

    public synchronized GameController getAnyGame() {
        if (gameControllers.isEmpty()) initGame();

        return gameControllers.stream().findFirst().orElseThrow();
    }

    @TestOnly
    public void forceReset() {
        gameControllers.clear();
        initGame();
    }
}
