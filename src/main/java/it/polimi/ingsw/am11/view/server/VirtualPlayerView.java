package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.controller.CardController;
import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.GameController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.player.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// I think this class calls the server controller

/**
 * This class is a virtual view for a player in the server. It is used to send commands to the
 * server controller and to receive updates from the server controller.
 */
public class VirtualPlayerView {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualPlayerView.class);
    private final @NotNull CardController cardController;
    private final @NotNull ServerPlayerConnector connector;
    private final @NotNull String nickname;

    public VirtualPlayerView(@NotNull ServerPlayerConnector connector, @NotNull String nickname) {
        GameController gameController = CentralController.INSTANCE.getAnyGame();
        cardController = gameController.getCardController();
        this.connector = connector;
        this.nickname = nickname;
    }


    /**
     * Used by the client to set the number of players in the game.
     *
     * @param numOfPlayers the number of players to play with
     * @throws NumOfPlayersException if the number of players is not valid
     * @throws NotGodPlayerException if the player is not a god player
     * @throws GameStatusException   if the game status is not valid, for example, if it's ongoing
     */
    public void setNumOfPlayers(int numOfPlayers)
    throws NumOfPlayersException, NotGodPlayerException, GameStatusException {
        LOGGER.debug("COMMAND: Num of players set to {} from {}", numOfPlayers, nickname);
        CentralController.INSTANCE.setNumOfPlayers(nickname, numOfPlayers);
    }


    /**
     * Used by the client to set a previously given <code>StarterCard</code>.
     *
     * @param isRetro true if the starter card is placed on the retro side, false if it's placed on
     *                the front side
     * @throws GameStatusException if the game status is not valid, for example, if it's ongoing
     */
    public void setStarterCard(boolean isRetro)
    throws GameStatusException {
        LOGGER.debug("COMMAND: Starter set for {} on {}", nickname, isRetro ? "retro" : "front");
        try {
            cardController.setStarterFor(nickname, isRetro);
        } catch (PlayerInitException | IllegalCardPlacingException e) {
            throw new GameBreakingException("Discrepancies between view and game");
        }
    }


    /**
     * Used by the client to choose between the previously given candidate
     * <code>ObjectiveCard</code> (2 for standard rules).
     *
     * @param cardId the id of the objective card to set
     * @throws IllegalPlayerSpaceActionException if the player is not allowed to set the objective
     * @throws PlayerInitException               if the player is not initialized
     * @throws GameStatusException               if the game status is not valid, for example, if
     *                                           it's ongoing
     */
    public void setObjectiveCard(int cardId)
    throws IllegalPlayerSpaceActionException, PlayerInitException, GameStatusException {
        LOGGER.debug("COMMAND: Objective {} set for {}", cardId, nickname);
        cardController.setObjectiveFor(nickname, cardId);
    }


    /**
     * Used by the client to place a card on his field.
     *
     * @param cardId  id of the card to place
     * @param x       x position of <code>Position</code>
     * @param y       y position of <code>Position</code>
     * @param isRetro true if to place on the retro, false otherwise
     * @throws TurnsOrderException           if it's not the turn of this player
     * @throws PlayerInitException           if the player has not been initialized
     * @throws IllegalCardPlacingException   if it's not allowed to place a card in that location
     * @throws NotInHandException            if the card trying to place is not in hand
     * @throws IllegalPlateauActionException if player not found
     * @throws GameStatusException           if the status of the game doesn't allow placing of
     *                                       cards
     */
    public void placeCard(int cardId, int x, int y, boolean isRetro)
    throws TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {
        LOGGER.debug("COMMAND: Placing card {} for {} on ({}, {})", cardId, nickname, x, y);
        cardController.placeCard(nickname, cardId, Position.of(x, y), isRetro);
    }

    /**
     * Used by the client to draw a card either from deck or from a table of visible cards
     *
     * @param fromVisible true if to draw from visible, false otherwise
     * @param type        type of the card <code>GOLD</code> or <code>RESOURCE</code>
     * @param cardId      id of the card to draw if from visible
     * @throws IllegalPlayerSpaceActionException if you can't draw the card
     * @throws TurnsOrderException               if it's not your turn
     * @throws IllegalPickActionException        if the card is not on the table
     * @throws PlayerInitException               if the player doesn't exist
     * @throws EmptyDeckException                if the deck is empty
     * @throws MaxHandSizeException              if the hand is full
     * @throws GameStatusException               if the game status doesn't allow drawing
     */
    public void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, EmptyDeckException, MaxHandSizeException, GameStatusException {
        LOGGER.debug("COMMAND: Drawing card of {} for {} from {}", type, nickname,
                     fromVisible ? "visible" : "deck");
        cardController.drawCard(fromVisible, type, nickname, cardId);
    }


    /**
     * Used by the client if it need a complete retransmission of the game data.
     */
    public void syncMeUp() {
        LOGGER.debug("COMMAND: Syncing up for {}", nickname);
        CentralController.INSTANCE.reSyncPlayer(nickname);
    }


    /**
     * Sends to the client a set of candidate objectives
     *
     * @param event the event containing the candidate objectives
     */
    public void updatePlayer(@NotNull CandidateObjectiveEvent event) {
        LOGGER.debug("EVENT: Candidate objective {} sent to {}", event.getNewValue(), nickname);
        connector.sendCandidateObjective(event.getNewValue());
    }


    /**
     * Sends to the client a change in hand.
     * <p>
     * It can be a remove action when placing or a draw action during drawing phase.
     *
     * @param event the event containing the hand change
     */
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

    /**
     * Sends to the player the confirmation of the chosen objective.
     *
     * @param event the event containing the personal objective chosen
     */
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

    /**
     * Sends to the player the starter card given after which they will need to choose in with front
     * to place it.
     *
     * @param event the event containing a starter card
     */
    public void updatePlayer(@NotNull StarterCardEvent event) {
        LOGGER.debug("EVENT: Starter card {} sent to {}", event.getNewValue(), nickname);
        connector.sendStarterCard(event.getNewValue());
    }

    /**
     * Sends to the player all information that should be available.
     * <p>
     * This does not include private information, like other players' hand or the deck order of
     * cards.
     *
     * @param event the event containing all the information of the current game.
     */
    public void updatePlayer(@NotNull ReconnectionEvent event) {
        LOGGER.debug("EVENT: Game model memento sent to {}", nickname);
        connector.sendReconnection(event.getValueOfAction());
    }
}
