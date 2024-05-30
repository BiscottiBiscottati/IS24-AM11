package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.ServerChatConnector;
import it.polimi.ingsw.am11.network.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.ServerTableConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum CentralController {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(CentralController.class);

    private final List<GameController> gameControllers;

    private final ChatController chatController;

    CentralController() {
        this.gameControllers = new ArrayList<>(4);
        this.chatController = new ChatController();
    }

    @NotNull
    public synchronized VirtualPlayerView connectPlayer(@NotNull String nickname,
                                                        @NotNull ServerPlayerConnector playerConnector,
                                                        @NotNull ServerTableConnector tableConnector,
                                                        @NotNull ServerChatConnector chatConnector)
    throws GameStatusException, NumOfPlayersException, PlayerInitException,
           NotSetNumOfPlayerException {
        if (gameControllers.isEmpty()) createNewGame();

        VirtualPlayerView view = gameControllers.stream()
                                                .findFirst()
                                                .orElseThrow()
                                                .connectPlayer(nickname, playerConnector,
                                                               tableConnector);

        chatController.addPlayer(nickname, chatConnector);
        return view;
    }

    public synchronized void createNewGame() {
        LOGGER.info("CONTROLLER: Creating new game");
        GameController newGame = new GameController();
        this.gameControllers.add(newGame);
    }

    public synchronized void disconnectPlayer(String nickname) {
        LOGGER.info("CONTROLLER: Player {} disconnected", nickname);
        gameControllers.stream().findFirst()
                       .orElseThrow()
                       .disconnectPlayer(nickname);
        chatController.removePlayer(nickname);
    }

    public synchronized void setNumOfPlayers(@NotNull String nickname, int val)
    throws NotGodPlayerException, GameStatusException,
           NumOfPlayersException {
        gameControllers.stream().findFirst()
                       .orElseThrow()
                       .setNumOfPlayers(nickname, val);
    }

    public void destroyGame() {
        LOGGER.info("CONTROLLER: Destroying current Game and recreating a new one");
        gameControllers.clear();
        chatController.clear();
        createNewGame();
    }

    public synchronized @Nullable String getGodPlayer() {
        return gameControllers.stream().findFirst()
                              .orElseThrow()
                              .getGodPlayer();
    }

    public synchronized GameController getAnyGame() {
        if (gameControllers.isEmpty()) createNewGame();

        return gameControllers.stream().findFirst().orElseThrow();
    }

    public ChatController getChatController() {
        return chatController;
    }
}
