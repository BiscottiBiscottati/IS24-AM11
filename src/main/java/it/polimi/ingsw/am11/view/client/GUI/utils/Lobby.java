package it.polimi.ingsw.am11.view.client.GUI.utils;

import it.polimi.ingsw.am11.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final List<Player> players;

    public Lobby() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public boolean isFull() {
        return this.players.size() == 4;
    }
}
