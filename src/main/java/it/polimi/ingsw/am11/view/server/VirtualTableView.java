package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.table.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
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
    public void updateTable(@NotNull FieldChangeEvent fieldChangeEvent) {
        Map.Entry<Position, CardContainer> entry = fieldChangeEvent.getNewValue();
        ActionMode mode = fieldChangeEvent.getAction();

        Consumer<TableConnector> function = null;

        switch (mode) {
            case INSERTION -> function = connector -> connector.updateField(
                    fieldChangeEvent.getPlayer().orElseThrow(),
                    entry.getKey().x(),
                    entry.getKey().y(),
                    entry.getValue().getCard().getId(),
                    entry.getValue().isRetro(),
                    false);
            case REMOVAL -> function = connector -> connector.updateField(
                    fieldChangeEvent.getPlayer().orElseThrow(),
                    entry.getKey().x(),
                    entry.getKey().y(),
                    entry.getValue().getCard().getId(),
                    entry.getValue().isRetro(),
                    true);
            default -> {
                System.out.println("Invalid ActionMode");
                return;
            }
        }
        broadcast(function);
    }

    private void broadcast(Consumer<TableConnector> action) {
        connectors.values().forEach(action);
    }

    public void updateTable(CommonObjectiveChangeEvent event) {
        System.out.println("CommonObjectiveChangeEvent");
    }

    public void updateTable(DeckTopChangeEvent event) {
        System.out.println("DeckTopChangeEvent");
    }

    public void updateTable(PlayerPointsChangeEvent event) {
        System.out.println("PlayerPointsChangeEvent");
    }

    public void updateTable(ShownPlayableEvent event) {
        System.out.println("ShownPlayableEvent");
    }

    public void updateTable(GameStatusChangeEvent event) {
        System.out.println("GameStatusChangeEvent");
    }

    public void updateTable(TurnChangeEvent event) {
        System.out.println("TurnChangeEvent");
    }

    public void updateTable(PlayerAddedEvent event) {
        System.out.println("PlayerAddedEvent");
    }

}
