package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.view.events.*;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TableViewUpdater implements PropertyChangeListener {
    private final VirtualTableView virtualView;

    public TableViewUpdater(VirtualTableView virtualView) {
        this.virtualView = virtualView;
    }

    @Override
    public void propertyChange(@NotNull PropertyChangeEvent evt) {
        switch (evt) {
            case FieldChangeEvent fieldEvt -> virtualView.updateTable(fieldEvt);

            case CommonObjectiveChangeEvent commonObjectiveEvt ->
                    virtualView.updateTable(commonObjectiveEvt);

            case ShownPlayableEvent showPlayableEvt -> virtualView.updateTable(showPlayableEvt);

            case PlayerPointsChangeEvent playerPointsEvt ->
                    virtualView.updateTable(playerPointsEvt);

            case DeckTopChangeEvent deckTopEvt -> virtualView.updateTable(deckTopEvt);

            default -> throw new IllegalArgumentException("Unexpected value: " + evt);
        }

    }
}
