package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.controller.CardController;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.GameController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.PersonalObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// I think this class calls the server controller methods
public class VirtualPlayerView {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualPlayerView.class);
    private final CardController cardController;
    private final GameController gameController;
    private final PlayerConnector connector;
    private final String nickname;

    public VirtualPlayerView(@NotNull PlayerConnector connector, @NotNull String nickname) {
        gameController = CentralController.INSTANCE.getAnyGame();
        cardController = gameController.getCardController();
        this.connector = connector;
        this.nickname = nickname;
    }

    public void setStarterCard(boolean isRetro)
    throws PlayerInitException, IllegalCardPlacingException, GameStatusException {
        LOGGER.debug("COMMAND: Starter set for {} on {}", nickname, isRetro ? "retro" : "front");
        cardController.setStarterFor(nickname, isRetro);
    }

    public void setObjectiveCard(int cardId)
    throws IllegalPlayerSpaceActionException, PlayerInitException, GameStatusException {
        LOGGER.debug("COMMAND: Objective {} set for {}", cardId, nickname);
        cardController.setObjectiveFor(nickname, cardId);
    }

    public void placeCard(int cardId, int x, int y, boolean isRetro)
    throws TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {
        LOGGER.debug("COMMAND: Placing card {} for {} on ({}, {})", cardId, nickname, x, y);
        cardController.placeCard(nickname, cardId, Position.of(x, y), isRetro);
    }

    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, EmptyDeckException, MaxHandSizeException, GameStatusException {
        LOGGER.debug("COMMAND: Drawing card of {} for {} from {}", type, nickname,
                     fromVisible ? "visible" : "deck");
        cardController.drawCard(fromVisible, type, nickname, cardId);
    }

    public void updatePlayer(@NotNull CandidateObjectiveEvent event) {
        LOGGER.debug("EVENT: Candidate objective {} sent to {}", event.getNewValue(), nickname);
        connector.sendCandidateObjective(event.getNewValue());
    }

    public void updatePlayer(@NotNull HandChangeEvent event) {
        ActionMode action = event.getAction();
        if (action == ActionMode.INSERTION) {
            LOGGER.debug("EVENT: Card {} added to hand for {}", event.getValueOfAction(), nickname);
            connector.updateHand(event.getValueOfAction(), false);
        } else if (action == ActionMode.REMOVAL) {
            LOGGER.debug("EVENT: Card {} removed from hand for {}", event.getValueOfAction(),
                         nickname);
            connector.updateHand(event.getValueOfAction(), true);
        }
    }

    public void updatePlayer(@NotNull PersonalObjectiveChangeEvent event) {
        ActionMode action = event.getAction();
        if (action == ActionMode.INSERTION) {
            LOGGER.debug("EVENT: Personal objective {} added for {}", event.getValueOfAction(),
                         nickname);
            connector.updatePersonalObjective(event.getValueOfAction(), false);
        } else if (action == ActionMode.REMOVAL) {
            LOGGER.debug("EVENT: Personal objective {} removed for {}", event.getValueOfAction(),
                         nickname);
            connector.updatePersonalObjective(event.getValueOfAction(), true);
        }

    }

    public void updatePlayer(@NotNull StarterCardEvent event) {
        LOGGER.debug("EVENT: Starter card {} sent to {}", event.getNewValue(), nickname);
        connector.sendStarterCard(event.getNewValue());
    }
}
