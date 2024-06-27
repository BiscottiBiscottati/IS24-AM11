package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

/**
 * This class is a superclass for the event that are sent to the view through the PlayerViewUpdater.
 * These events are used to notify an update that have to be notified to a specific player.
 */
public abstract class PlayerViewEvent extends ViewEvent {

    private final @NotNull String player;

    protected PlayerViewEvent(@NotNull String player) {
        this.player = player;
    }

    /**
     * This method returns the player that has to be notified.
     *
     * @return the player that has to be notified.
     */
    public @NotNull String getPlayer() {
        return player;
    }

    /**
     * This method is used to update the view of the player that has to be notified.
     *
     * @param virtualView the view of the player that has to be notified.
     */
    public abstract void updateView(@NotNull VirtualPlayerView virtualView);

}
