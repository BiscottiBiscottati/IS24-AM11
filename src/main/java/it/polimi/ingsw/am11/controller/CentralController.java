package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.ServerNetworkManager;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum CentralController {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(CentralController.class);

    private final @NotNull List<GameController> gameControllers;

    private final @NotNull ChatController chatController;

    private ServerNetworkManager networkManager;

    CentralController() {
        this.gameControllers = new ArrayList<>(4);
        this.chatController = new ChatController();
    }

    public void setNetworkManager(@NotNull ServerNetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @NotNull
    public synchronized VirtualPlayerView connectPlayer
            (@NotNull String nickname,
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

    public synchronized void loadMostRecent() {
        destroyGame();
        LOGGER.info("CONTROLLER: Loading most recent game");

        GameController gameController = gameControllers.stream().findFirst().orElseThrow();

        if (! gameController.loadMostRecent())
            LOGGER.info("CONTROLLER: No recent game found");
        else LOGGER.info("CONTROLLER: Loaded most recent game");
    }

    public void destroyGame() {
        LOGGER.info("CONTROLLER: Destroying current Game and recreating a new one");
        gameControllers.clear();
        if (networkManager != null) networkManager.kickAllPlayers();
        chatController.clear();
        createNewGame();
    }

    public synchronized void disconnectPlayer(@NotNull String nickname) {
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

    public synchronized @NotNull GameController getAnyGame() {
        if (gameControllers.isEmpty()) createNewGame();

        return gameControllers.stream().findFirst().orElseThrow();
    }

    public synchronized void reSyncPlayer(@NotNull String nickname) {
        gameControllers.stream().findFirst()
                       .orElseThrow()
                       .reSyncPlayer(nickname);
    }

    public @NotNull ChatController getChatController() {
        return chatController;
    }
}
