package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class CommonObjectiveChangeEvent extends TableViewEvent {

    // TODO to add to model
    private final Integer oldCardId;
    private final Integer newCardId;

    public CommonObjectiveChangeEvent(Integer oldCardId, Integer newCardId) {
        this.oldCardId = oldCardId;
        this.newCardId = newCardId;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Integer getOldValue() {
        return oldCardId;
    }

    @Override
    public Integer getNewValue() {
        return newCardId;
    }
}
