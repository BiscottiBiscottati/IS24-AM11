package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.util.Set;

public class ShownPlayableEvent extends PropertyChangeEvent {

    private final Set<Integer> playableCards;
    private final PlayableCardType cardType;
    private final Integer oldValue;
    private final Integer newValue;

    public ShownPlayableEvent(Set<Integer> source, @NotNull PlayableCardType cardType,
                              Integer oldValue,
                              Integer newValue) {
        super(source, cardType.name(), oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.cardType = cardType;
        this.playableCards = source;
    }

    @Override
    public Set<Integer> getSource() {
        return playableCards;
    }

    public PlayableCardType getCardType() {
        return cardType;
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
