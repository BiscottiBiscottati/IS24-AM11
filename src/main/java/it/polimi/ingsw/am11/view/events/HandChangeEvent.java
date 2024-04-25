package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;

public class HandChangeEvent extends PropertyChangeEvent {
    private final int previousCard;
    private final int currentCard;

    public HandChangeEvent(Object source, String propertyName, Integer oldCardId,
                           Integer newCardId) {
        super(source, propertyName, oldCardId, newCardId);
        this.previousCard = oldCardId;
        this.currentCard = newCardId;
    }

    @Override
    public Integer getNewValue() {
        return this.currentCard;
    }

    @Override
    public Integer getOldValue() {
        return this.previousCard;
    }
}
