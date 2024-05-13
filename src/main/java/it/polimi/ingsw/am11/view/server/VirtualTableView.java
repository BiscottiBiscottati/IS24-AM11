package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.table.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class VirtualTableView {
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
                connector.updateField(
                        event.getPlayer().orElseThrow(),
                        entry.get().getKey().x(),
                        entry.get().getKey().y(),
                        entry.get().getValue().getCard().getId(),
                        entry.get().getValue().isRetro(),
                        true);
            };
            default -> {
//                System.out.println("Invalid ActionMode " + mode + " in FieldChangeEvent " +
//                                   event.getOldValue() + " " + event.getNewValue());
                return;
            }
        }
        broadcast(function);
    }

    private void broadcast(Consumer<TableConnector> action) {
        connectors.values().forEach(action);
    }

    //FIXME may use commander pattern or other

    public void updateTable(@NotNull CommonObjectiveChangeEvent event) {
        switch (event.getAction()) {
            case INSERTION -> {
                broadcast(connector -> connector.updateCommonObjective(event.getValueOfAction(),
                                                                       false));
            }
            case REMOVAL -> {
                broadcast(connector -> connector.updateCommonObjective(event.getValueOfAction(),
                                                                       true));
            }
            default -> System.out.println("Invalid ActionMode");
        }
    }

    public void updateTable(@NotNull DeckTopChangeEvent event) {
        broadcast(connector -> connector.updateDeckTop(event.getCardType(),
                                                       event.getNewValue()));
    }

    public void updateTable(@NotNull PlayerPointsChangeEvent event) {
        broadcast(connector -> connector.updatePlayerPoint(event.getPlayer().orElseThrow(),
                                                           event.getNewValue()));
    }

    public void updateTable(@NotNull ShownPlayableEvent event) {
        broadcast(connector -> connector.updateShownPlayable(event.getOldValue(),
                                                             event.getNewValue()));
    }

    public void updateTable(GameStatusChangeEvent event) {
        broadcast(connector -> connector.updateGameStatus(event.getNewValue()));
    }

    public void updateTable(@NotNull TurnChangeEvent event) {
        broadcast(connector -> connector.updateTurnChange(event.getNewValue()));
    }

    public void updateTable(PlayerAddedEvent event) {

    }

    public void updateTable(FinalLeaderboardEvent event) {
        broadcast(connector -> connector.sendFinalLeaderboard(event.getNewValue()));
    }

}
