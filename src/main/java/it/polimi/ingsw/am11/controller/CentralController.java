package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.server.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.server.TableViewUpdater;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public enum CentralController {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(CentralController.class);

    private final GameModel model;
    private final CardController cardController;
    private final GameController gameController;
    private final Map<String, VirtualPlayerView> playerViews;
    private final AtomicInteger maxNumOfPlayer;
    private final AtomicReference<String> godPlayer;
    private VirtualTableView tableView;

    CentralController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.gameController = new GameController(model);
        this.playerViews = new ConcurrentHashMap<>(8);
        this.model.addTableListener(new TableViewUpdater(tableView));
        this.maxNumOfPlayer = new AtomicInteger(- 1);
        this.godPlayer = new AtomicReference<>(null);
    }

    @NotNull
    public VirtualPlayerView connectPlayer(@NotNull String nickname,
                                           @NotNull PlayerConnector playerConnector,
                                           @NotNull TableConnector tableConnector)
    throws GameStatusException, NumOfPlayersException, PlayerInitException,
           NotSetNumOfPlayerException {

        int currentMaxPlayers = maxNumOfPlayer.get();

        if (! godPlayer.compareAndSet(null, nickname) && currentMaxPlayers == - 1) {
            throw new NotSetNumOfPlayerException("NumOfPlayers not yet set by godPlayer");
        }

        synchronized (playerViews) {
            if (currentMaxPlayers != - 1 && playerViews.size() >= currentMaxPlayers) {
                throw new NumOfPlayersException("Max num of players reached");
            }

            this.gameController.addPlayer(nickname);
            VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
            this.playerViews.put(nickname, playerView);
            this.model.addPlayerListener(nickname,
                                         new PlayerViewUpdater(playerView));
            this.tableView.addConnector(nickname, tableConnector);

            if (currentMaxPlayers == playerViews.size()) {
                try {
                    gameController.initGame();
                } catch (NumOfPlayersException e) {
                    throw new RuntimeException(e);
                }
            }
            return playerView;
        }
    }

    public void setNumOfPlayers(@NotNull String nickname, int val)
    throws NotGodPlayerException, GameStatusException,
           NumOfPlayersException {

        if (val <= 1 || val > model.getRuleSet().getMaxPlayers())
            throw new NumOfPlayersException("Trying to add an illegal number of players");

        if (! Objects.equals(godPlayer.get(), nickname)) {
            throw new NotGodPlayerException("A not god player is trying to set the num of " +
                                            "players");
        }

        if (! maxNumOfPlayer.compareAndSet(- 1, val)) {
            throw new GameStatusException("num of players already set");
        }

        LOGGER.info("Num of players set to {} by {}", val, nickname);
    }

    public void playerDisconnected(String nickname) {
        model.addUnavailablePlayer(nickname);
    }

    public void playerReconnected(String nickname) {
        model.playerIsNowAvailable(nickname);
    }

    public @Nullable String getGodPlayer() {
        return godPlayer.get();
    }


    public CardController getCardController() {
        return cardController;
    }

    public GameController getGameController() {
        return gameController;
    }

    @TestOnly
    public void forceReset() {
        synchronized (playerViews) {
            this.model.forceReset();
            this.tableView = new VirtualTableView();
            this.playerViews.clear();
            this.model.addTableListener(new TableViewUpdater(tableView));

            String val = godPlayer.get();
            while (! godPlayer.compareAndSet(val, null)) {
                val = godPlayer.get();
            }

            int max = maxNumOfPlayer.get();
            while (! maxNumOfPlayer.compareAndSet(max, - 1)) {
                max = maxNumOfPlayer.get();
            }
        }
    }
}
