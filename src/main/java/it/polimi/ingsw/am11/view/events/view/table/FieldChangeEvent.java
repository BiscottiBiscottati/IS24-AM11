package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FieldChangeEvent extends TableViewEvent {

    private final Map.Entry<Position, CardContainer> oldValue;
    private final Map.Entry<Position, CardContainer> newValue;

    public FieldChangeEvent(String player,
                            Map.Entry<Position, CardContainer> oldValue,
                            Map.Entry<Position, CardContainer> newValue) {
        super(player);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Map.Entry<Position, CardContainer> getOldValue() {
        return this.oldValue;
    }

    @Override
    public Map.Entry<Position, CardContainer> getNewValue() {
        return this.newValue;
    }

    @Override
    public @NotNull Map.Entry<Position, CardContainer> getValueOfAction() {
        if (oldValue != null) return oldValue;
        if (newValue != null) return newValue;
        throw new UnsupportedOperationException("both are null cannot be called");
    }
}
