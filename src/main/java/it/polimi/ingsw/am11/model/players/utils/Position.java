package it.polimi.ingsw.am11.model.players.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a position in a 2D space with x and y coordinates.
 */
public record Position(int x, int y) {

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

}
