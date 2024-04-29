package it.polimi.ingsw.am11.view.client;

import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClientViewUpdater {
    private final PropertyChangeSupport pcs;

    public ClientViewUpdater() {
        this.pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(@NotNull ViewSection propertyName,
                                          PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(propertyName.name(), listener);
    }

    public void removePropertyChangeListener(@NotNull ViewSection propertyName,
                                             PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(propertyName.name(), listener);
    }

    public void firePropertyChange(@NotNull PropertyChangeEvent evt) {
        ViewSection.valueOf(evt.getPropertyName());
        this.pcs.firePropertyChange(evt);
    }
}
