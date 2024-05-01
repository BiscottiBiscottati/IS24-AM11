package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TableViewEvent extends ViewEvent {


    private final String player;

    protected TableViewEvent(@Nullable String player) {
        this.player = player;
    }

    protected TableViewEvent() {
        this.player = null;
    }

    public abstract void updateView(VirtualTableView virtualView);

    public Optional<String> getPlayer() {
        return Optional.ofNullable(player);
    }

}
