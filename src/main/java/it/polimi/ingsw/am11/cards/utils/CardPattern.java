package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A record to save patterns for <code>PositioningCard</code>
 *
 * @param pattern a matrix made of arrays with the pattern
 */
public record CardPattern(Color[][] pattern) {
    /**
     * A static method for creating a new <code>CardPattern</code> from a List of Lists
     *
     * @param matrix the matrix used to create <code>CardPattern</code>
     * @return a new instance of <code>CardPattern</code>
     */
    @Contract("_ -> new")
    public static @NotNull CardPattern of(List<List<Color>> matrix) {
        Color[][] temp = new Color[3][3];
        IntStream.range(0, 3).forEach(
                x -> IntStream.range(0, 3).forEach(
                        y -> temp[x][y] = matrix.get(x).get(y)
                )
        );
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
            return IntStream.range(0, 3)
                            .filter(
                                    value -> !Arrays.equals(this.pattern[value], ((CardPattern) obj).pattern()[value])
                            )
                            .findAny()
                            .isEmpty();
        } else return false;
    }

    /**
     * @return the hash based on each element of a matrix
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(
                Arrays.stream(pattern)
                      .map(Arrays::deepHashCode)
                      .toArray()
        );
    }
}
