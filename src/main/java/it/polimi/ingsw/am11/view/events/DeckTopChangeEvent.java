package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;

public class DeckTopChangeEvent extends PropertyChangeEvent {

    private final Color oldValue;
    private final Color newValue;

    public DeckTopChangeEvent(Object source, @NotNull PlayableCardType propertyName, Color oldValue,
                              Color newValue) {
        super(source, propertyName.name(), oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public Color getNewValue() {
        return this.newValue;
    }

    @Override
    public Color getOldValue() {
        return this.oldValue;
    }
}
