package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.view.VirtualPlayerView;
import it.polimi.ingsw.am11.view.VirtualTableView;

import java.util.HashMap;
import java.util.Map;

public class CentralController {
    private final GameModel model;
    private final VirtualTableView tableView;
    private final CardController cardController;
    private final GameController gameController;
    private final Map<String, VirtualPlayerView> playerViews;

    public CentralController(GameModel model) {
        this.model = model;
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.gameController = new GameController(model);
        this.playerViews = new HashMap<>(8);
    }

    public void connectPlayer(String nickname, VirtualPlayerView playerView)
    throws PlayerInitException,
           GameStatusException {
        this.gameController.addPlayer(nickname, new PlayerViewUpdater(playerView));
        this.playerViews.put(nickname, playerView);
    }
}
