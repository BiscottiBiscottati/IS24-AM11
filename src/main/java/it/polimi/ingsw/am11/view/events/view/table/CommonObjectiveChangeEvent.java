package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CommonObjectiveChangeEvent extends TableViewEvent {

    private final Set<Integer> oldCardId;
    private final Set<Integer> newCardId;

    public CommonObjectiveChangeEvent(Set<Integer> oldCardId, Set<Integer> newCardId) {
        this.oldCardId = oldCardId;
        this.newCardId = newCardId;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Set<Integer> getOldValue() {
        return oldCardId;
    }

    @Override
    public Set<Integer> getNewValue() {
        return newCardId;
    }

    @Override
    public @NotNull Set<Integer> getValueOfAction() {
        if (oldCardId != null) return oldCardId;
        if (newCardId != null) return newCardId;
        throw new UnsupportedOperationException("both are null cannot be called");
    }


}
