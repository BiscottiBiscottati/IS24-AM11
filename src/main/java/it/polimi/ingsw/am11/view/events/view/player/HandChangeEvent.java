package it.polimi.ingsw.am11.view.events.view.player;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

public class HandChangeEvent extends PlayerViewEvent {
    private final Integer previousCard;
    private final Integer currentCard;

    public HandChangeEvent(String player, Integer oldCardId,
                           Integer newCardId) {
        super(player);
        this.previousCard = oldCardId;
        this.currentCard = newCardId;
    }

    @Override
    public void updateView(@NotNull VirtualPlayerView virtualView) {
        virtualView.updatePlayer(this);
    }

    @Override
    public Integer getOldValue() {
        return this.previousCard;
    }

    @Override
    public Integer getNewValue() {
        return this.currentCard;
    }

    @Override
    public @NotNull Integer getValueOfAction() {
        if (previousCard != null) return previousCard;
        if (currentCard != null) return currentCard;
        throw new UnsupportedOperationException("both are null cannot be called");
    }
}
