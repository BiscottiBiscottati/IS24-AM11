package it.polimi.ingsw.am11.view.events.view.player;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

public class StarterCardEvent extends PlayerViewEvent {

    //TODO to add to model
    private final Integer oldCardId;
    private final Integer newCardId;

    public StarterCardEvent(Object source, String player, Integer oldCardId,
                            Integer newCardId) {
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
    public Integer getNewValue() {
        return newCardId;
    }
}
