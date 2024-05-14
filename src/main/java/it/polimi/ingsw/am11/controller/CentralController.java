package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.server.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.server.TableViewUpdater;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum CentralController {
    INSTANCE;

    private final GameModel model;
    private final VirtualTableView tableView;
    private final CardController cardController;
    private final GameController gameController;
    private final Map<String, VirtualPlayerView> playerViews;
    private String godPlayer = null;
    private int numOfPlayers;

    CentralController() {
        this.model = new GameLogic();
        this.tableView = new VirtualTableView();
        this.cardController = new CardController(model);
        this.gameController = new GameController(model);
        this.playerViews = new HashMap<>(8);
        this.model.addTableListener(new TableViewUpdater(tableView));
        this.numOfPlayers = - 1;
    }

    public @NotNull VirtualPlayerView connectPlayer(String nickname,
                                                    PlayerConnector playerConnector,
                                                    TableConnector tableConnector)
    throws
    GameStatusException, NumOfPlayersException, PlayerInitException {
        if (godPlayer == null) {
            godPlayer = nickname;
        } else if (numOfPlayers == - 1) {
            throw new GameStatusException("NumOfPlayers not yet set by godPlayer");
        } else if (playerViews.size() >= numOfPlayers) {
            throw new NumOfPlayersException("Max num of players reached");
        }
        this.gameController.addPlayer(nickname);
        VirtualPlayerView playerView = new VirtualPlayerView(playerConnector, nickname);
        this.playerViews.put(nickname, playerView);
        this.model.addPlayerListener(nickname,
                                     new PlayerViewUpdater(playerView));
        this.tableView.addConnector(nickname, tableConnector);
        if (numOfPlayers == playerViews.size()) {
            try {
                gameController.initGame();
            } catch (NumOfPlayersException e) {
                throw new RuntimeException(e);
            }
        }
        return playerView;
    }

    public void setNumOfPlayers(@NotNull String nickname, int val)
    throws NotGodPlayerException, GameStatusException,
           NumOfPlayersException {
        if (! nickname.equals(godPlayer)) {
            throw new NotGodPlayerException("A not god player is trying to set the num of " +
                                            "players");
        } else if (val <= 1 || val > model.getRuleSet().getMaxPlayers()) {
            throw new NumOfPlayersException("trying to add an illegal number of players");
        } else if (numOfPlayers != - 1) {
            throw new GameStatusException("num of players already set");
        }
        numOfPlayers = val;
    }

    public void playerDisconnected(String nickname) {
        model.addUnavailablePlayer(nickname);
    }

    public void playerReconnected(String nickname) {
        model.playerIsNowAvailable(nickname);
    }

    public @Nullable String getGodPlayer() {
        return godPlayer;
    }


    public CardController getCardController() {
        return cardController;
    }

    public GameController getGameController() {
        return gameController;
    }
}
