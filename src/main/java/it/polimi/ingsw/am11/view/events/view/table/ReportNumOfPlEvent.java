package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReportNumOfPlEvent extends TableViewEvent {
    private final Integer numOfPlayers;

    public ReportNumOfPlEvent(@NotNull Integer numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public @Nullable Integer getOldValue() {
        return null;
    }

    @Override
    public @NotNull Integer getNewValue() {
        return numOfPlayers;
    }

    @Override
    public @NotNull Integer getValueOfAction() {
        return numOfPlayers;
    }
}
