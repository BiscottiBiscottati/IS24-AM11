package it.polimi.ingsw.am11.view.server;

import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.TableConnector;
import it.polimi.ingsw.am11.view.events.*;
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

    public void updateTable(FieldChangeEvent fieldChangeEvent) {
        String nickname = fieldChangeEvent.getPropertyName();
        boolean removeMode = fieldChangeEvent.getNewValue() == null;
        Position position;
        int cardId;
        if (removeMode) {


        }
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

}
