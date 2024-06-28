package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.table.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * This class is a virtual view for the parts of the model that are in common for all players, such
 * as the field, the common objectives, the deck top cards, the player points, the shown playable
 * cards, the turn, the final leaderboard. It is used to receive updates from the server
 * controller.
 */
public class VirtualTableView {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualTableView.class);
    private final @NotNull Map<String, ServerTableConnector> connectors;

    public VirtualTableView() {
        this.connectors = new ConcurrentHashMap<>(8);
    }

    /**
     * Method to add a connector to the virtual view for a player
     *
     * @param nickname  the nickname of the player
     * @param connector the connector to add
     */
    public void addConnector(@NotNull String nickname, @NotNull ServerTableConnector connector) {
        this.connectors.put(nickname, connector);
    }

    /**
     * Method to remove a connector from the virtual view for a player
     *
     * @param nickname the nickname of the player
     */
    public void removeConnector(@NotNull String nickname) {
        this.connectors.remove(nickname);
    }

    /**
     * Method used to notify the virtual view of a change in the field
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull FieldChangeEvent event) {
        AtomicReference<Map.Entry<Position, CardContainer>> entry = new AtomicReference<>();
        ActionMode mode = event.getAction();

        Consumer<ServerTableConnector> function;

        if (mode == ActionMode.INSERTION) {
            entry.set(event.getNewValue());
            LOGGER.debug("EVENT: Field change for {} on ({}, {}). Added card {}",
                         event.getPlayer().orElseThrow(),
                         entry.get().getKey().x(), entry.get().getKey().y(),
                         entry.get().getValue().getCard().getId());
            function = connector -> connector.updateField(
                    event.getPlayer().orElseThrow(),
                    entry.get().getKey().x(),
                    entry.get().getKey().y(),
                    entry.get().getValue().getCard().getId(),
                    entry.get().getValue().isRetro()
            );
        } else {
            LOGGER.debug("Invalid ActionMode in FieldChangeEvent (most likely a clear event)");
            return;
        }
        broadcast(function);
    }

    private void broadcast(@NotNull Consumer<ServerTableConnector> action) {
        connectors.values().forEach(action);
    }

    /**
     * Method used to notify the virtual view of a change in the game status of the model
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull GameStatusChangeEvent event) {
        LOGGER.debug("EVENT: Game status change from {} to {}", event.getOldValue(),
                     event.getNewValue());
        broadcast(connector -> connector.updateGameStatus(event.getValueOfAction()));
    }

    /**
     * Method used to notify the virtual view of a change in the common objectives, it could be an
     * insertion or a removal
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull CommonObjectiveChangeEvent event) {
        switch (event.getAction()) {
            case INSERTION -> {
                LOGGER.debug("EVENT: Common objective {} added", event.getValueOfAction());
                broadcast(connector -> connector.updateCommonObjective(event.getValueOfAction(),
                                                                       false));
            }
            case REMOVAL -> {
                LOGGER.debug("EVENT: Common objective {} removed", event.getValueOfAction());
                broadcast(connector -> connector.updateCommonObjective(event.getValueOfAction(),
                                                                       true));
            }
            default -> LOGGER.debug("Invalid ActionMode in CommonObjectiveChangeEvent");
        }
    }

    /**
     * Method used to notify the virtual view of a change in the deck top card color
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull DeckTopChangeEvent event) {
        LOGGER.debug("EVENT: {} deck top card color change to {}", event.getCardType(),
                     event.getNewValue());
        broadcast(connector -> connector.updateDeckTop(event.getCardType(),
                                                       event.getNewValue()));
    }

    /**
     * Method used to notify the virtual view of a change in the player points
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull PlayerPointsChangeEvent event) {
        LOGGER.debug("EVENT: Player {} points change to {}", event.getPlayer().orElseThrow(),
                     event.getNewValue());
        broadcast(connector -> connector.updatePlayerPoint(event.getPlayer().orElseThrow(),
                                                           event.getNewValue()));
    }

    /**
     * Method used to notify the virtual view of a change in the shown playable cards
     *
     * @param event the event that contains the change
     */
    public void updateTable(@NotNull ShownPlayableEvent event) {
        LOGGER.debug("EVENT: Shown playable card change from {} to {}", event.getOldValue(),
                     event.getNewValue());
        broadcast(connector -> connector.updateShownPlayable(event.getOldValue(),
                                                             event.getNewValue()));
    }

    /**
     * Method used to notify the virtual view of a turn change
     *
     * @param event the event that contains the new turn
     */
    public void updateTable(@NotNull TurnChangeEvent event) {
        LOGGER.debug("EVENT: Turn change from {} to {}", event.getOldValue(), event.getNewValue());
        broadcast(connector -> connector.updateTurnChange(event.getNewValue()));
    }

    /**
     * Method used to notify the virtual view that the final leaderboard has been calculated. It
     * sends the final leaderboard to the view
     *
     * @param event the event that contains the final leaderboard
     */
    public void updateTable(@NotNull FinalLeaderboardEvent event) {
        LOGGER.debug("EVENT: Final leaderboard sent: {}", event.getNewValue());
        broadcast(connector -> connector.sendFinalLeaderboard(event.getNewValue()));
    }

    /**
     * Method used to notify the virtual with the names and colors of the players in the game
     *
     * @param event the event that contains the names and colors
     */
    public void updateTable(@NotNull PlayerInfoEvent event) {
        LOGGER.debug("EVENT: Player info sent: {}", event.getValueOfAction());
        broadcast(connector -> connector.updatePlayers(event.getValueOfAction()));
    }

    /**
     * Update the number of players in the game
     *
     * @param event the event that contains the new number of players
     */
    public void updateTable(@NotNull NumOfPlayerEvent event) {
        LOGGER.debug("EVENT: Num of players set: {}", event.getValueOfAction());
        broadcast(connector -> connector.updateNumOfPlayers(event.getValueOfAction()));
    }
}
