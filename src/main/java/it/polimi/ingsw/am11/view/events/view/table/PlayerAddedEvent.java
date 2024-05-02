package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerAddedEvent extends TableViewEvent {

    private final List<String> players;
    private final String player;

    public PlayerAddedEvent(List<String> players, @NotNull String playerAdded) {
        super(playerAdded);
        this.player = playerAdded;
        this.players = players;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public String getOldValue() {
        return null;
    }

    @Override
    public String getNewValue() {
        return player;
    }

    @Override
    public @NotNull String getValueOfAction() {
        return player;
    }
}
