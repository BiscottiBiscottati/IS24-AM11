package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.SequencedMap;

public class PlayerInfoEvent extends TableViewEvent {

    private final SequencedMap<PlayerColor, String> players;

    public PlayerInfoEvent(SequencedMap<PlayerColor, String> players) {
        super();
        this.players = players;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public SequencedMap<PlayerColor, String> getOldValue() {
        return null;
    }

    @Override
    public SequencedMap<PlayerColor, String> getNewValue() {
        return players;
    }

    @Override
    public @NotNull SequencedMap<PlayerColor, String> getValueOfAction() {
        return players;
    }
}
