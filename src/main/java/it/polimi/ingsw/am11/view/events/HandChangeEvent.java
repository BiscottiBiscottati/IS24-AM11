package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;

public class HandChangeEvent extends PropertyChangeEvent {
    private final Integer previousCard;
    private final Integer currentCard;

    public HandChangeEvent(Object source, String player, Integer oldCardId,
                           Integer newCardId) {
        super(source, player, oldCardId, newCardId);
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
