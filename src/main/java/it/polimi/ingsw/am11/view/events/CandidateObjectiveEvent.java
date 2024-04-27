package it.polimi.ingsw.am11.view.events;

import java.beans.PropertyChangeEvent;
import java.util.Set;

public class CandidateObjectiveEvent extends PropertyChangeEvent {

    private final Set<Integer> oldCardsId;
    private final Set<Integer> newCardsId;

    public CandidateObjectiveEvent(Object source, String player, Set<Integer> oldCardsId,
                                   Set<Integer> newCardsId) {
        super(source, player, oldCardsId, newCardsId);
        this.oldCardsId = oldCardsId;
        this.newCardsId = newCardsId;
    }

    @Override
    public Set<Integer> getNewValue() {
        return newCardsId;
    }

    @Override
    public Set<Integer> getOldValue() {
        return oldCardsId;
    }
}
