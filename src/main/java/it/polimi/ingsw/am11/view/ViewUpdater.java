package it.polimi.ingsw.am11.view;

import it.polimi.ingsw.am11.view.events.TurnChangeEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewUpdater implements PropertyChangeListener {

    private final VirtualView virtualView;

    public ViewUpdater(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public void propertyChange(TurnChangeEvent evt) {
        // TODO link to the virtual view
    }
}
