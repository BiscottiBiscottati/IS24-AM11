package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.controller.CardController;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.GameController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.PersonalObjectiveChangeEvent;
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

    public void setStarterCard(boolean isRetro)
    throws PlayerInitException, IllegalCardPlacingException, GameStatusException {
        cardController.setStarterFor(nickname, isRetro);
    }

    public void setObjectiveCard(int cardId)
    throws IllegalPlayerSpaceActionException, PlayerInitException, GameStatusException {
        cardController.setObjectiveFor(nickname, cardId);
    }

    public void placeCard(int cardId, int x, int y, boolean isRetro)
    throws TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {
        cardController.placeCard(nickname, cardId, Position.of(x, y), isRetro);
    }

    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, EmptyDeckException, MaxHandSizeException, GameStatusException {
        cardController.drawCard(fromVisible, type, nickname, cardId);
    }

    public void updatePlayer(CandidateObjectiveEvent event) {
    }

    public void updatePlayer(HandChangeEvent event) {

    }

    public void updatePlayer(PersonalObjectiveChangeEvent event) {

    }

    public void updatePlayer(StarterCardEvent event) {

    }

}
