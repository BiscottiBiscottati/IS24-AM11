package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.view.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.TableViewUpdater;
import it.polimi.ingsw.am11.view.VirtualPlayerView;
import it.polimi.ingsw.am11.view.VirtualTableView;

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

    public void connectPlayer(String nickname, VirtualPlayerView playerView)
    throws PlayerInitException,
           GameStatusException {
        this.gameController.addPlayer(nickname);
        this.playerViews.put(nickname, playerView);
        this.model.addPlayerListener(new PlayerViewUpdater(playerView), nickname);
    }

    public CardController getCardController() {
        return cardController;
    }

    public GameController getGameController() {
        return gameController;
    }
}
