package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerViewEvent extends ViewEvent {

    private final @NotNull String player;

    protected PlayerViewEvent(@NotNull String player) {
        this.player = player;
    }

    public @NotNull String getPlayer() {
        return player;
    }

    public abstract void updateView(@NotNull VirtualPlayerView virtualView);

}
