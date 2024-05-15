package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerInfoEvent extends TableViewEvent {

    private final Map<PlayerColor, String> players;

    public PlayerInfoEvent(Map<PlayerColor, String> players) {
        super();
        this.players = players;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Map<PlayerColor, String> getOldValue() {
        return null;
    }

    @Override
    public Map<PlayerColor, String> getNewValue() {
        return players;
    }

    @Override
    public @NotNull Map<PlayerColor, String> getValueOfAction() {
        return players;
    }
}
