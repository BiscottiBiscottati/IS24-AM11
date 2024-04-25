package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;

public class TurnChangeEvent extends PropertyChangeEvent {

    private final String previousPlayer;
    private final String currentPlayer;

    public TurnChangeEvent(Object source, String propertyName, String previousPlayer,
                           String currentPlayer) {
        super(source, propertyName, previousPlayer, currentPlayer);
        this.previousPlayer = previousPlayer;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getNewValue() {
        return this.currentPlayer;
    }

    @Override
    public String getOldValue() {
        return this.previousPlayer;
    }
}
