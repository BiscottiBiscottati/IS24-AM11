package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.table.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class VirtualTableView {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualTableView.class);
    private final Map<String, TableConnector> connectors;

    public VirtualTableView() {
        this.connectors = new HashMap<>(8);
    }

    public void addConnector(@NotNull String nickname, @NotNull TableConnector connector) {
        this.connectors.put(nickname, connector);
    }

    public void removeConnector(@NotNull String nickname) {
        this.connectors.remove(nickname);
    }

    //TODO methods to finish
    public void updateTable(@NotNull FieldChangeEvent event) {
        AtomicReference<Map.Entry<Position, CardContainer>> entry = new AtomicReference<>();
        ActionMode mode = event.getAction();

        Consumer<TableConnector> function;

        switch (mode) {
            case INSERTION -> {
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
                        entry.get().getValue().isRetro(),
                        false);
            }
            case REMOVAL -> function = connector -> {
                entry.set(event.getOldValue());
                LOGGER.debug("EVENT: Field change for {} on ({}, {}). Removed card {}",
                             event.getPlayer().orElseThrow(),
                             entry.get().getKey().x(), entry.get().getKey().y(),
                             entry.get().getValue().getCard().getId());
                connector.updateField(
                        event.getPlayer().orElseThrow(),
                        entry.get().getKey().x(),
                        entry.get().getKey().y(),
                        entry.get().getValue().getCard().getId(),
                        entry.get().getValue().isRetro(),
                        true);
            };
            default -> {
                LOGGER.debug("Invalid ActionMode in FieldChangeEvent (most likely a clear event)");
                return;
            }
        }
        broadcast(function);
    }

    private void broadcast(@NotNull Consumer<TableConnector> action) {
        connectors.values().forEach(action);
    }

    //FIXME may use commander pattern or other

    public void updateTable(@NotNull GameStatusChangeEvent event) {
        LOGGER.debug("EVENT: Game status change from {} to {}", event.getOldValue(),
                     event.getNewValue());
        broadcast(connector -> connector.updateGameStatus(event.getNewValue()));
    }

    public void updateTable(@NotNull CommonObjectiveChangeEvent event) {
        switch (event.getAction()) {
            case INSERTION -> {
                LOGGER.debug("EVENT: Common objective {} added", event.getValueOfAction());
                broadcast(connector -> {
                    connector.updateCommonObjective(event.getValueOfAction(),
                                                    false);
                });
            }
            case REMOVAL -> {
                LOGGER.debug("EVENT: Common objective {} removed", event.getValueOfAction());
                broadcast(connector -> {
                    connector.updateCommonObjective(event.getValueOfAction(),
                                                    true);
                });
            }
            default -> LOGGER.debug("Invalid ActionMode in CommonObjectiveChangeEvent");
        }
    }

    public void updateTable(@NotNull DeckTopChangeEvent event) {
        LOGGER.debug("EVENT: {} deck top card color change to {}", event.getCardType(),
                     event.getNewValue());
        broadcast(connector -> connector.updateDeckTop(event.getCardType(),
                                                       event.getNewValue()));
    }

    public void updateTable(@NotNull PlayerPointsChangeEvent event) {
        LOGGER.debug("EVENT: Player {} points change to {}", event.getPlayer().orElseThrow(),
                     event.getNewValue());
        broadcast(connector -> connector.updatePlayerPoint(event.getPlayer().orElseThrow(),
                                                           event.getNewValue()));
    }

    public void updateTable(@NotNull ShownPlayableEvent event) {
        LOGGER.debug("EVENT: Shown playable card change from {} to {}", event.getOldValue(),
                     event.getNewValue());
        broadcast(connector -> connector.updateShownPlayable(event.getOldValue(),
                                                             event.getNewValue()));
    }

    public void updateTable(@NotNull TurnChangeEvent event) {
        LOGGER.debug("EVENT: Turn change from {} to {}", event.getOldValue(), event.getNewValue());
        broadcast(connector -> connector.updateTurnChange(event.getNewValue()));
    }

    public void updateTable(@NotNull FinalLeaderboardEvent event) {
        LOGGER.debug("EVENT: Final leaderboard sent: {}", event.getNewValue());
        broadcast(connector -> {
            connector.sendFinalLeaderboard(event.getNewValue());
        });
    }

    public void updateTable(@NotNull PlayerInfoEvent event) {
        LOGGER.debug("EVENT: Player info sent: {}", event.getNewValue());
        broadcast(connector -> {
            connector.updatePlayers(event.getNewValue());
        });
    }

    public void updateTable(@NotNull ReportNumOfPlEvent event) {
        LOGGER.debug("EVENT: Report num of pl sent: {}", event.getNewValue());
        broadcast(connector -> connector.updateNumOfPlayers(event.getNewValue()));
    }

    public void updateTable(@NotNull NumOfPlayerEvent event) {
        LOGGER.debug("EVENT: Num of players set: {}", event.getValueOfAction());
        broadcast(connector -> connector.updateNumOfPlayers(event.getValueOfAction()));
    }
}
