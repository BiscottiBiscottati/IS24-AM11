package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class DeckTopChangeEvent extends TableViewEvent {

    private final PlayableCardType cardType;
    private final Color oldValue;
    private final Color newValue;

    public DeckTopChangeEvent(@NotNull PlayableCardType cardType, Color oldValue,
                              Color newValue) {
        this.cardType = cardType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Color getOldValue() {
        return this.oldValue;
    }

    @Override
    public Color getNewValue() {
        return this.newValue;
    }

    public PlayableCardType getCardType() {
        return cardType;
    }
}
