package it.polimi.ingsw.am11.view;

import it.polimi.ingsw.am11.network.TableConnector;
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

}
