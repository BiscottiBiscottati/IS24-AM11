package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class ShownPlayableEvent extends TableViewEvent {

    private final @NotNull PlayableCardType cardType;
    private final Integer oldValue;
    private final Integer newValue;

    public ShownPlayableEvent(@NotNull PlayableCardType cardType,
                              Integer oldValue,
                              Integer newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.cardType = cardType;
    }

    public @NotNull PlayableCardType getCardType() {
        return cardType;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Integer getOldValue() {
        return oldValue;
    }

    @Override
    public Integer getNewValue() {
        return newValue;
    }

    @Override
    public @NotNull Object getValueOfAction() {
        if (oldValue != null) return oldValue;
        if (newValue != null) return newValue;
        throw new UnsupportedOperationException("both are null cannot be called");
    }
}
