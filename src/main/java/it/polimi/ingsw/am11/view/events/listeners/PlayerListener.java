package it.polimi.ingsw.am11.view.events.listeners;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;

/**
 * Listener for events that should be received by a single player, it will be used by the model or
 * controller to notify the changes to the clients
 */
public interface PlayerListener {

    /**
     * This method is called when a PlayerViewEvent is fired, it will be used to notify the client
     *
     * @param event the event that has been fired
     */
    void propertyChange(PlayerViewEvent event);
}
