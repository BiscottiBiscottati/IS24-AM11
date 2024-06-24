package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.utils.persistence.SavesManager;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;
import it.polimi.ingsw.am11.view.events.view.table.NumOfPlayerEvent;
import it.polimi.ingsw.am11.view.server.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.server.TableViewUpdater;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final @NotNull GameModel model;
    private final @NotNull CardController cardController;
    private final @NotNull PlayerColorManager playerColor;
    private final @NotNull AtomicInteger maxNumOfPlayer;
    private final @NotNull AtomicReference<String> godPlayer;
    private final @NotNull VirtualTableView tableView;

    GameController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.model.addTableListener(new TableViewUpdater(tableView));
        this.playerColor = new PlayerColorManager();

        this.maxNumOfPlayer = new AtomicInteger(- 1);
        this.godPlayer = new AtomicReference<>(null);
    }

    @NotNull
    VirtualPlayerView connectPlayer(@NotNull String nickname,
                                    @NotNull ServerPlayerConnector playerConnector,
                                    @NotNull ServerTableConnector tableConnector)
    throws NumOfPlayersException, PlayerInitException, GameStatusException,
           NotSetNumOfPlayerException {
        int currentMaxPlayers = maxNumOfPlayer.get();

        boolean isGodSet = godPlayer.compareAndSet(null, nickname);

        if (! isGodSet && currentMaxPlayers == - 1) {
            LOGGER.info("CONTROLLER: NotSetNumOfPlayerException thrown for {}", nickname);
            throw new NotSetNumOfPlayerException("NumOfPlayers not yet set by godPlayer");
        }

        synchronized (model) {
            // check if the player is already in the game
            if (model.isDisconnected(nickname)) {
                LOGGER.info("CONTROLLER: Player {} trying to reconnect", nickname);

                VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
                tableView.addConnector(nickname, tableConnector);

                model.reconnectPlayer(nickname, new PlayerViewUpdater(playerView));
                return playerView;
            }

            if (currentMaxPlayers != - 1 && model.getPlayers().size() >= currentMaxPlayers) {
                throw new NumOfPlayersException("Max num of players reached");
            }

            LOGGER.info("CONTROLLER: Adding player {} to model", nickname);
            model.addPlayerToTable(nickname, playerColor.pullAnyColor());

            VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
            tableView.addConnector(nickname, tableConnector);
            model.addPlayerListener(nickname, new PlayerViewUpdater(playerView));

            if (isGodSet) {
                LOGGER.info("CONTROLLER: Notifying god player {}", nickname);
                playerConnector.notifyGodPlayer();
            }

            if (currentMaxPlayers == model.getPlayers().size()) {
                try {
                    model.initGame();
                    cardController.saveToDisk();
                } catch (NumOfPlayersException | GameBreakingException e) {
                    throw new RuntimeException(e);
                }
            }
            return playerView;
        }
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

        LOGGER.info("CONTROLLER: Num of players set to {} by {}", val, nickname);

    }

    void disconnectPlayer(@NotNull String nickname) {
        String god = godPlayer.get();
        int currentMaxPlayers = maxNumOfPlayer.get();
        if (currentMaxPlayers == - 1 && god.equals(nickname)) {
            CentralController.INSTANCE.destroyGame();
            return;
        }
        model.disconnectPlayer(nickname);
        tableView.removeConnector(nickname);
    }

    public @NotNull CardController getCardController() {
        return cardController;
    }

    boolean loadMostRecent() {
        return SavesManager.loadMostRecentGame()
                           .map(memento -> {
                               maxNumOfPlayer.set(memento.playerManager().players().size());
                               model.load(memento);
                               return true;
                           })
                           .orElse(false);
    }

    void reSyncPlayer(@NotNull String nickname) {
        model.reSyncWith(nickname);
    }
}
