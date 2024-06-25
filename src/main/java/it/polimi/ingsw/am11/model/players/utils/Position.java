package it.polimi.ingsw.am11.model.players.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
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

    /**
     * Factory method to create a new <code>Position</code> instance from a string.
     * <p>
     * The string should contain the x and y coordinates separated by a semicolon.
     * <p>
     * This method is used by Jackson library to deserialize a Position object from a JSON string.
     * <p>
     * Important: This method is necessary to serialize and deserialize into JSON format for saving
     * the game state in SQLite database.
     *
     * @param s String - The string containing the x and y coordinates.
     * @return Position - A new Position instance with the coordinates parsed from the string.
     */
    @JsonCreator
    public static @NotNull Position of(@NotNull String s) {
        String[] parts = s.split(";");
        return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    /**
     * It is used to convert the Position object to a string representation.
     * <p>
     * The string representation includes the x and y coordinates separated by a semicolon.
     * <p>
     * This method is also used by the Jackson library for serialization.
     *
     * @return String - A string representation of the Position object.
     */
    @Override
    public @NotNull String toString() {
        return x + ";" + y;
    }

    public @NotNull Position getPositionOn(Corner corner) {
        return switch (corner) {
            case TOP_LX -> new Position(x - 1, y + 1);
            case TOP_RX -> new Position(x + 1, y + 1);
            case DOWN_LX -> new Position(x - 1, y - 1);
            case DOWN_RX -> new Position(x + 1, y - 1);
        };
    }


}
