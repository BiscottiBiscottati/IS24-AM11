package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import org.jetbrains.annotations.NotNull;

public class PlayerViewUpdater implements PlayerListener {

    private final VirtualPlayerView virtualView;

    public PlayerViewUpdater(VirtualPlayerView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(@NotNull PlayerViewEvent event) {
        event.updateView(virtualView);
    }
}
