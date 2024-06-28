package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class PlayerPointsChangeEvent extends TableViewEvent {

    private final Integer oldValue;
    private final @NotNull Integer newValue;

    public PlayerPointsChangeEvent(String player, Integer oldValue, @NotNull Integer newValue) {
        super(player);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Integer getOldValue() {
        return oldValue;
    }

    @Override
    public @NotNull Integer getNewValue() {
        return newValue;
    }

    @Override
    public @NotNull Integer getValueOfAction() {
        if (oldValue != null) return oldValue;
        return newValue;
    }

}
