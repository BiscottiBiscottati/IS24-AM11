package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

public abstract class PlayerViewEvent extends ViewEvent {

    private final String player;

    protected PlayerViewEvent(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public abstract void updateView(VirtualPlayerView virtualView);

}
