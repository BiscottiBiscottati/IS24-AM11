package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FinalLeaderboardEvent extends TableViewEvent {
    private final @NotNull Map<String, Integer> finalLeaderboard;

    public FinalLeaderboardEvent(@NotNull Map<String, Integer> finalLeaderboard) {
        this.finalLeaderboard = finalLeaderboard;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public @Nullable Map<String, Integer> getOldValue() {
        return null;
    }

    @Override
    public @NotNull Map<String, Integer> getNewValue() {
        return finalLeaderboard;
    }

    @Override
    public @NotNull Map<String, Integer> getValueOfAction() {
        return finalLeaderboard;
    }
}
