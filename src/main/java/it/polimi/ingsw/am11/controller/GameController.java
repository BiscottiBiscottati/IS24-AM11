package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.server.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.server.TableViewUpdater;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameModel model;
    private final CardController cardController;
    private final List<PlayerColor> colors;
    private final Map<String, VirtualPlayerView> playerViews;
    private final AtomicInteger maxNumOfPlayer;
    private final AtomicReference<String> godPlayer;
    private final VirtualTableView tableView;

    public GameController(GameModel model) {
        this.model = model;
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.model.addTableListener(new TableViewUpdater(tableView));

        this.playerViews = new ConcurrentHashMap<>(8);
        this.maxNumOfPlayer = new AtomicInteger(- 1);
        this.godPlayer = new AtomicReference<>(null);

        colors = new ArrayList<>(Arrays.stream(PlayerColor.values()).toList());
        Collections.shuffle(colors);
    }

    public GameController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.model.addTableListener(new TableViewUpdater(tableView));

        this.playerViews = new ConcurrentHashMap<>(8);
        this.maxNumOfPlayer = new AtomicInteger(- 1);
        this.godPlayer = new AtomicReference<>(null);

        colors = new ArrayList<>(Arrays.stream(PlayerColor.values()).toList());
        Collections.shuffle(colors);
    }

    VirtualPlayerView connectPlayer(@NotNull String nickname,
                                    @NotNull PlayerConnector playerConnector,
                                    @NotNull TableConnector tableConnector)
    throws NumOfPlayersException, PlayerInitException, GameStatusException,
           NotSetNumOfPlayerException {
        int currentMaxPlayers = maxNumOfPlayer.get();

        if (! godPlayer.compareAndSet(null, nickname) && currentMaxPlayers == - 1) {
            throw new NotSetNumOfPlayerException("NumOfPlayers not yet set by godPlayer");
        }

        synchronized (playerViews) {
            if (currentMaxPlayers != - 1 && playerViews.size() >= currentMaxPlayers) {
                throw new NumOfPlayersException("Max num of players reached");
            }


            if (colors.isEmpty()) {
                throw new PlayerInitException("No more colors available");
            }
            PlayerColor color = colors.removeFirst();
            model.addPlayerToTable(nickname, color);

            VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
            playerViews.put(nickname, playerView);
            model.addPlayerListener(nickname, new PlayerViewUpdater(playerView));
            tableView.addConnector(nickname, tableConnector);

            if (currentMaxPlayers == playerViews.size()) {
                try {
                    initGame();
                } catch (NumOfPlayersException e) {
                    throw new RuntimeException(e);
                }
            }
            return playerView;
        }
    }

    void initGame() throws NumOfPlayersException,
                           GameStatusException {
        try {
            model.initGame();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    void setNumOfPlayers(@NotNull String nickname, int val)
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

    void goNextTurn() throws
                      GameStatusException {
        try {
            // TODO to handle disconnection in the model
            model.goNextTurn();
        } catch (GameBreakingException e) {
            throw new RuntimeException(e);
        }
    }

    void removePlayer(String nickname) throws GameStatusException {
        model.removePlayer(nickname);
    }

    public CardController getCardController() {
        return cardController;
    }

    String getGodPlayer() {
        return godPlayer.get();
    }
}
