package it.polimi.ingsw.am11.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TableViewUpdater implements PropertyChangeListener {
    private final VirtualTableView virtualView;

    public TableViewUpdater(VirtualTableView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO
    }
}
