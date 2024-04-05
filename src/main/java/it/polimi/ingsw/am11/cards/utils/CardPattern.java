package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * A record to save patterns for <code>PositioningCard</code>
 *
 * @param pattern a matrix made of Lists with the pattern
 */
public record CardPattern(List<List<Color>> pattern) {
    /**
     * A static method for creating a new <code>CardPattern</code> from a array of arrays
     *
     * @param matrix the matrix used to create <code>CardPattern</code>
     * @return a new instance of <code>CardPattern</code>
     */
    @Contract("_ -> new")
    public static @NotNull CardPattern of(Color[][] matrix) {
        List<List<Color>> temp = Arrays.stream(matrix)
                                       .map(Arrays::asList)
                                       .toList();
        return new CardPattern(temp);
    }

    /**
     * @param obj the reference object with which to compare.
     * @return true if each element of pattern is equal otherwise false
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardPattern) {
            return this.pattern.equals(((CardPattern) obj).pattern);
        } else return false;
    }

    /**
     * @return the hash based on each element of a matrix
     */
    @Override
    public int hashCode() {
        return this.pattern.hashCode();
    }
}