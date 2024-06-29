package it.polimi.ingsw.am11.view.events.view.player;

import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReconnectionEvent extends PlayerViewEvent {

    ReconnectionModelMemento reconnectionModelMemento;

    public ReconnectionEvent(@NotNull String player,
                             @NotNull ReconnectionModelMemento reconnectionModelMemento) {
        super(player);
        this.reconnectionModelMemento = reconnectionModelMemento;
    }

    @Override
    public void updateView(@NotNull VirtualPlayerView virtualView) {
        virtualView.updatePlayer(this);
    }

    @Override
    public @Nullable ReconnectionModelMemento getOldValue() {
        return null;
    }

    @Override
    public @Nullable ReconnectionModelMemento getNewValue() {
        return reconnectionModelMemento;
    }

    @Override
    public @NotNull ReconnectionModelMemento getValueOfAction() {
        return reconnectionModelMemento;
    }
}
