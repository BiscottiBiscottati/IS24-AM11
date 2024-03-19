package it.polimi.ingsw.am11.players;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Position(int x, int y) implements Comparable<Position> {
    @Contract("_, _ -> new")
    public static @NotNull Position of(int x, int y) {
        return new Position(x, y);
    }

    @Override
    public int compareTo(@NotNull Position o) {
        if (this.x == o.x) {
            return Integer.compare(this.y, o.y);
        }
        return Integer.compare(this.x, o.x);
    }
}
