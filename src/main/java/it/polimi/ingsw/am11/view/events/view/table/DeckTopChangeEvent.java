package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeckTopChangeEvent extends TableViewEvent {

    private final @NotNull PlayableCardType cardType;
    private final GameColor oldValue;
    private final GameColor newValue;

    public DeckTopChangeEvent(@NotNull PlayableCardType cardType, GameColor oldValue,
                              GameColor newValue) {
        this.cardType = cardType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public @Nullable GameColor getOldValue() {
        return this.oldValue;
    }

    @Override
    public @Nullable GameColor getNewValue() {
        return this.newValue;
    }

    @Override
    public @NotNull GameColor getValueOfAction() {
        return newValue;
    }

    public @NotNull PlayableCardType getCardType() {
        return cardType;
    }


}
