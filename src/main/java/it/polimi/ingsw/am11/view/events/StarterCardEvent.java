package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;

public class StarterCardEvent extends PropertyChangeEvent {

    private final Integer oldCardId;
    private final Integer newCardId;

    public StarterCardEvent(Object source, String player, Integer oldCardId,
                            Integer newCardId) {
        super(source, player, oldCardId, newCardId);
        this.oldCardId = oldCardId;
        this.newCardId = newCardId;
    }

    @Override
    public Integer getNewValue() {
        return newCardId;
    }

    @Override
    public Integer getOldValue() {
        return oldCardId;
    }
}
