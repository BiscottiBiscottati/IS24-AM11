package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.view.VirtualPlayerView;
import it.polimi.ingsw.am11.view.VirtualTableView;

import java.util.ArrayList;
import java.util.List;

public class CentralController {
    private final GameModel model;
    private final VirtualTableView tableView;
    private final CardController cardController;
    private final GameController gameController;
    private final List<VirtualPlayerView> playerViews;

    public CentralController(GameModel model, VirtualTableView tableView) {
        this.model = model;
        this.tableView = tableView;
        this.cardController = new CardController(model);
        this.gameController = new GameController(model);
        this.playerViews = new ArrayList<>(8);
    }
}
