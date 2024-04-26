package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class FieldChangeEvent extends PropertyChangeEvent {
    private final Map.Entry<Position, CardContainer> oldValue;
    private final Map.Entry<Position, CardContainer> newValue;

    public FieldChangeEvent(Object source, String player,
                            Map.Entry<Position, CardContainer> oldValue,
                            Map.Entry<Position, CardContainer> newValue) {
        super(source, player, oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Map.Entry<Position, CardContainer> getNewValue() {
        return this.newValue;
    }

    @Override
    public Map.Entry<Position, CardContainer> getOldValue() {
        return this.oldValue;
    }
}
