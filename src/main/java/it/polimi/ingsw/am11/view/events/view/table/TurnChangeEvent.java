package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class TurnChangeEvent extends TableViewEvent {

    private final String previousPlayer;
    private final String currentPlayer;

    public TurnChangeEvent(String previousPlayer,
                           String currentPlayer) {
        this.previousPlayer = previousPlayer;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public String getOldValue() {
        return this.previousPlayer;
    }

    @Override
    public String getNewValue() {
        return this.currentPlayer;
    }
}
