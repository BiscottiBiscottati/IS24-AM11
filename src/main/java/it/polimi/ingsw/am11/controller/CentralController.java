package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.TableViewUpdater;
import it.polimi.ingsw.am11.view.VirtualPlayerView;
import it.polimi.ingsw.am11.view.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum CentralController {
    INSTANCE;

    private final GameModel model;
    private final VirtualTableView tableView;
    private final CardController cardController;
    private final GameController gameController;
    private final Map<String, VirtualPlayerView> playerViews;

    CentralController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.gameController = new GameController(model);
        this.playerViews = new HashMap<>(8);
        this.model.addTableListener(new TableViewUpdater(tableView));
    }

    public @NotNull VirtualPlayerView connectPlayer(String nickname,
                                                    PlayerConnector playerConnector,
                                                    TableConnector tableConnector)
    throws PlayerInitException,
           GameStatusException {
        this.gameController.addPlayer(nickname);
        VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
        this.playerViews.put(nickname, playerView);
        this.model.addPlayerListener(new PlayerViewUpdater(playerView), nickname);
        this.tableView.addConnector(nickname, tableConnector);
        return playerView;
    }

    public CardController getCardController() {
        return cardController;
    }

    public GameController getGameController() {
        return gameController;
    }
}
