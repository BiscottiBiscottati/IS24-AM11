package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.events.view.table.PlayerPointsChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.ShownPlayableEvent;
import it.polimi.ingsw.am11.view.events.view.table.TurnChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.CommonObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.DeckTopChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.GameStatusChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VirtualTableView {
    private final Map<String, TableConnector> connectors;

    public VirtualTableView() {
        this.connectors = new HashMap<>(8);
    }

    public void addConnector(@NotNull String nickname, @NotNull TableConnector connector) {
        this.connectors.put(nickname, connector);
    }

    public void removeConnector(@NotNull String nickname) {
        this.connectors.remove(nickname);
    }

    //TODO methods to finish
    public void updateTable(FieldChangeEvent fieldChangeEvent) {
    }

    public void updateTable(CommonObjectiveChangeEvent commonObjectiveChangeEvent) {
        System.out.println("CommonObjectiveChangeEvent");
    }

    public void updateTable(DeckTopChangeEvent deckTopChangeEvent) {
        System.out.println("DeckTopChangeEvent");
    }

    public void updateTable(PlayerPointsChangeEvent playerPointsChangeEvent) {
        System.out.println("PlayerPointsChangeEvent");
    }

    public void updateTable(ShownPlayableEvent shownPlayableEvent) {
        System.out.println("ShownPlayableEvent");
    }

    public void updateTable(GameStatusChangeEvent gameStatusChangeEvent) {
        System.out.println("GameStatusChangeEvent");
    }

    public void updateTable(TurnChangeEvent turnChangeEvent) {
        System.out.println("TurnChangeEvent");
    }

}
