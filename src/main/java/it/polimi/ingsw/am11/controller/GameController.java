package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.ServerTableConnector;
import it.polimi.ingsw.am11.view.events.view.table.NumOfPlayerEvent;
import it.polimi.ingsw.am11.view.server.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.server.TableViewUpdater;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameModel model;
    private final CardController cardController;
    private final Map<String, VirtualPlayerView> playerViews;
    private final PlayerColorManager playerColor;
    private final AtomicInteger maxNumOfPlayer;
    private final AtomicReference<String> godPlayer;
    private final VirtualTableView tableView;

    public GameController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.model.addTableListener(new TableViewUpdater(tableView));
        this.playerColor = new PlayerColorManager();
        this.playerViews = new ConcurrentHashMap<>(8);

        this.maxNumOfPlayer = new AtomicInteger(- 1);
        this.godPlayer = new AtomicReference<>(null);
    }

    VirtualPlayerView connectPlayer(@NotNull String nickname,
                                    @NotNull ServerPlayerConnector playerConnector,
                                    @NotNull ServerTableConnector tableConnector)
    throws NumOfPlayersException, PlayerInitException, GameStatusException,
           NotSetNumOfPlayerException {
        int currentMaxPlayers = maxNumOfPlayer.get();

        boolean isGod = godPlayer.compareAndSet(null, nickname);

        if (! isGod && currentMaxPlayers == - 1) {
            if (model.isDisconnected(nickname) && godPlayer.get().equals(nickname)) {
                LOGGER.info("God player {} trying to reconnect", nickname);

                VirtualPlayerView playerView = createPlayerView(nickname,
                                                                playerConnector
                );
                tableView.addConnector(nickname, tableConnector);

                model.reconnectPlayer(nickname, new PlayerViewUpdater(playerView));
                return playerView;
            }
            throw new NotSetNumOfPlayerException("NumOfPlayers not yet set by godPlayer");
        }

        synchronized (model) {
            if (model.isDisconnected(nickname)) {
                LOGGER.info("Player {} trying to reconnect", nickname);

                VirtualPlayerView playerView = createPlayerView(nickname,
                                                                playerConnector
                );
                tableView.addConnector(nickname, tableConnector);

                model.reconnectPlayer(nickname, new PlayerViewUpdater(playerView));
                return playerView;
            }

            if (currentMaxPlayers != - 1 && model.getPlayers().size() >= currentMaxPlayers) {
                throw new NumOfPlayersException("Max num of players reached");
            }

            LOGGER.info("Adding player {} to model", nickname);
            model.addPlayerToTable(nickname, playerColor.pullAnyColor());

            VirtualPlayerView playerView = createPlayerView(nickname,
                                                            playerConnector
            );
            tableView.addConnector(nickname, tableConnector);
            model.addPlayerListener(nickname, new PlayerViewUpdater(playerView));

            // TODO notify god player that the player has connected
            if (isGod) {
                LOGGER.info("Notifying god player {}", nickname);
                playerConnector.notifyGodPlayer();
            }

            if (currentMaxPlayers == model.getPlayers().size()) {
                try {
                    model.initGame();
                } catch (NumOfPlayersException | GameBreakingException e) {
                    throw new RuntimeException(e);
                }
            }
            return playerView;
        }
    }

    private @NotNull VirtualPlayerView createPlayerView(String nickname,
                                                        ServerPlayerConnector playerConnector) {
        VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
        playerViews.put(nickname, playerView);
        return playerView;
    }

    void setNumOfPlayers(@NotNull String nickname, int val)
    throws NotGodPlayerException, GameStatusException,
           NumOfPlayersException {

        if (val <= 1 || val > model.getRuleSet().getMaxPlayers())
            throw new NumOfPlayersException("Trying to add an illegal number of players: " + val);

        if (! Objects.equals(godPlayer.get(), nickname)) {
            throw new NotGodPlayerException("A not god player is trying to set the num of " +
                                            "players");
        }

        if (! maxNumOfPlayer.compareAndSet(- 1, val)) {
            throw new GameStatusException("Num of players already set");
        }

        tableView.updateTable(new NumOfPlayerEvent(val));

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

    void disconnectPlayer(@NotNull String nickname) {
        try {
            model.disconnectPlayer(nickname);
        } catch (PlayerInitException e) {
            return;
        }
        tableView.removeConnector(nickname);
    }

    public CardController getCardController() {
        return cardController;
    }

    String getGodPlayer() {
        return godPlayer.get();
    }
}
