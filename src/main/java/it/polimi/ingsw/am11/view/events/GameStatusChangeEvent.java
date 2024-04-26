package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.model.table.GameStatus;

import java.beans.PropertyChangeEvent;

public class GameStatusChangeEvent extends PropertyChangeEvent {

    private final GameStatus oldValue;
    private final GameStatus newValue;

    public GameStatusChangeEvent(Object source, GameStatus oldValue,
                                 GameStatus newValue) {
        super(source, "gameStatusChange", oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Object getNewValue() {
        return this.newValue;
    }

    @Override
    public Object getOldValue() {
        return this.oldValue;
    }
}
