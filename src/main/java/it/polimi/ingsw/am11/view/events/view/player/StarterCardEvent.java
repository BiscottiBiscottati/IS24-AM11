package it.polimi.ingsw.am11.view.events.view.player;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

public class StarterCardEvent extends PlayerViewEvent {

    private final Integer oldCardId;
    private final @NotNull Integer newCardId;

    public StarterCardEvent(@NotNull String player, Integer oldCardId,
                            @NotNull Integer newCardId) {
        super(player);
        this.oldCardId = oldCardId;
        this.newCardId = newCardId;
    }

    @Override
    public void updateView(@NotNull VirtualPlayerView virtualView) {
        virtualView.updatePlayer(this);
    }

    @Override
    public Integer getOldValue() {
        return oldCardId;
    }

    @Override
    public @NotNull Integer getNewValue() {
        return newCardId;
    }

    @Override
    public @NotNull Integer getValueOfAction() {
        if (oldCardId != null) return oldCardId;
        return newCardId;
    }


}
