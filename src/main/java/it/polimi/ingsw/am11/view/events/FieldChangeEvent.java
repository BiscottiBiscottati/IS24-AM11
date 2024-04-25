package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class FieldChangeEvent extends PropertyChangeEvent {
    private final Map.Entry oldValue;
    private final Map.Entry newValue;

    public FieldChangeEvent(Object source, String player, Map.Entry oldValue, Map.Entry newValue) {
        super(source, player, oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Map.Entry getNewValue() {
        return this.newValue;
    }

    @Override
    public Map.Entry getOldValue() {
        return this.oldValue;
    }
}
