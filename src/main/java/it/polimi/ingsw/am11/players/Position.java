package it.polimi.ingsw.am11.players;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Position {
    private final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
