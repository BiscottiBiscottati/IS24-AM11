package it.polimi.ingsw.am11.view;

import it.polimi.ingsw.am11.controller.CardController;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.GameController;
import it.polimi.ingsw.am11.network.PlayerConnector;
import org.jetbrains.annotations.NotNull;

public class VirtualPlayerView {
    private static final CardController cardController;
    private static final GameController gameController;

    static {
        cardController = CentralController.INSTANCE.getCardController();
        gameController = CentralController.INSTANCE.getGameController();
    }

    private final PlayerConnector connector;
    private final String nickname;

    public VirtualPlayerView(@NotNull PlayerConnector connector, @NotNull String nickname) {
        this.connector = connector;
        this.nickname = nickname;
    }
}
