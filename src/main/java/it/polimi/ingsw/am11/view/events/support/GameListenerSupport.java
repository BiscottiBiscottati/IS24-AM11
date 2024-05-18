package it.polimi.ingsw.am11.view.events.support;

import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameListenerSupport {
    private final Map<String, PlayerListener> playerListeners;
    private final Set<TableListener> tableListeners;

    public GameListenerSupport() {
        this.playerListeners = new HashMap<>(8);
        this.tableListeners = new HashSet<>(8);
    }

    public void addListener(String player, PlayerListener listener) {
        if (this.playerListeners.containsKey(player)) {
            throw new IllegalArgumentException("A listener is already registered for the player");
        }
        this.playerListeners.put(player, listener);
    }

    public void addListener(TableListener listener) {
        tableListeners.add(listener);
    }

    public void removeListener(PlayerListener listener) {
        this.playerListeners.values().remove(listener);
    }

    public void removeListener(TableListener listener) {
        tableListeners.remove(listener);
    }

    public void removeListener(String player) {
        this.playerListeners.remove(player);
    }

    public void fireEvent(@NotNull PlayerViewEvent event) {
        String player = event.getPlayer();

        if (this.playerListeners.containsKey(player)) {
            this.playerListeners.get(player).propertyChange(event);
        }
    }

    public void fireEvent(@NotNull TableViewEvent event) {
        for (TableListener listener : tableListeners) {
            listener.propertyChange(event);
        }
    }

    public void clearListeners() {
        this.playerListeners.clear();
        this.tableListeners.clear();
    }

}
