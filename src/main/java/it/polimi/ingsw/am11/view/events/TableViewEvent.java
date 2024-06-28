package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This class is a superclass for the event that is sent to the view through the TableViewUpdater.
 * These events are used to notify an update that has to be notified to all the players.
 */
public abstract class TableViewEvent extends ViewEvent {


    private final @Nullable String player;

    /**
     * This constructor is used to create an event that has to be notified to all the players.
     *
     * @param player the player protagonist of the event.
     */
    protected TableViewEvent(@Nullable String player) {
        this.player = player;
    }

    /**
     * This constructor is used to create an event that has to be notified to all the players. The
     * player protagonist of the event is not specified.
     */
    protected TableViewEvent() {
        this.player = null;
    }

    /**
     * This method is used to update the view of the table.
     *
     * @param virtualView the view of the table.
     */
    public abstract void updateView(@NotNull VirtualTableView virtualView);

    public @NotNull Optional<String> getPlayer() {
        return Optional.ofNullable(player);
    }

}
