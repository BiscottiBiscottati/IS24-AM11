package it.polimi.ingsw.am11.view.events.listeners;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for events that should be received bya all the players in the game, if will be used by
 * the model or controller to notify the changes to the clients
 */
public interface TableListener {


    /**
     * This method is called when a TableViewEvent is fired, it will be used to notify the clients
     * of the changes
     *
     * @param event the event that has been fired
     */
    void propertyChange(@NotNull TableViewEvent event);
}
