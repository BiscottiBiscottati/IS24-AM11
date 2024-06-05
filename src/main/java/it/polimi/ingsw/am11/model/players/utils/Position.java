package it.polimi.ingsw.am11.model.players.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Represents a position in a 2D space with x and y coordinates.
 */
public record Position(int x, int y) implements Serializable {

    /**
     * Factory method to create a new <code>Position</code> instance.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @return a new Position instance with the provided x and y coordinates
     */
    @Contract("_, _ -> new")
    public static @NotNull Position of(int x, int y) {
        return new Position(x, y);
    }

    @JsonCreator
    public static @NotNull Position of(@NotNull String s) {
        String[] parts = s.split(";");
        return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return x + ";" + y;
    }

}
