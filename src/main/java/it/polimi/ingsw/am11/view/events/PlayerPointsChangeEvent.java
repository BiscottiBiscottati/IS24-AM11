package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;

public class PlayerPointsChangeEvent extends PropertyChangeEvent {

    private final Integer oldValue;
    private final Integer newValue;

    public PlayerPointsChangeEvent(Object source, String player, Integer oldValue,
                                   Integer newValue) {
        super(source, player, oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Integer getNewValue() {
        return newValue;
    }

    @Override
    public Integer getOldValue() {
        return oldValue;
    }
}
