package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import org.jetbrains.annotations.NotNull;

public class TableViewUpdater implements TableListener {
    private final VirtualTableView virtualView;

    public TableViewUpdater(VirtualTableView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(@NotNull TableViewEvent event) {
        event.updateView(virtualView);
    }
}
