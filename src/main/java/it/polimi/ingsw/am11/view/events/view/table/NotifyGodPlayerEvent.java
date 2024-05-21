package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotifyGodPlayerEvent extends TableViewEvent {

    public NotifyGodPlayerEvent() {
        super();
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public @Nullable Object getOldValue() {
        return null;
    }

    @Override
    public @Nullable Object getNewValue() {
        return null;
    }

    @Override
    public @NotNull Object getValueOfAction() {
        return null;
    }
}
