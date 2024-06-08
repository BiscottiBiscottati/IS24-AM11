package it.polimi.ingsw.am11.view.events;

import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TableViewEvent extends ViewEvent {


    private final @Nullable String player;

    protected TableViewEvent(@Nullable String player) {
        this.player = player;
    }

    protected TableViewEvent() {
        this.player = null;
    }

    public abstract void updateView(@NotNull VirtualTableView virtualView);

    public @NotNull Optional<String> getPlayer() {
        return Optional.ofNullable(player);
    }

}
