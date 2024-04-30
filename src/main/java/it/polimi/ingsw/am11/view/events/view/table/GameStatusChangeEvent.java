package it.polimi.ingsw.am11.view.events.view.table;

import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.server.VirtualTableView;
import org.jetbrains.annotations.NotNull;

public class GameStatusChangeEvent extends TableViewEvent {

    private final GameStatus oldValue;
    private final GameStatus newValue;

    public GameStatusChangeEvent(GameStatus oldValue,
                                 GameStatus newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void updateView(@NotNull VirtualTableView virtualView) {
        virtualView.updateTable(this);
    }

    @Override
    public Object getOldValue() {
        return this.oldValue;
    }

    @Override
    public Object getNewValue() {
        return this.newValue;
    }
}
