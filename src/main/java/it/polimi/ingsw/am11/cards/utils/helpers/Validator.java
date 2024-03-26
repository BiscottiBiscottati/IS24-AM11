package it.polimi.ingsw.am11.cards.utils.helpers;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Used for validations of data structure.
 * <p>
 * Uses only static methods for checking.
 */
public final class Validator {
    private Validator() {
    }

    /**
     * Checks if in a given <code>Map</code> all instantiated values are positive.
     *
     * @param map The map to check
     * @param <K> The ket type of the map
     * @return if all values are positive, false otherwise
     */
    public static <K> boolean nonNegativeValues(@NotNull Map<K, Integer> map) {
        return map.values().stream().noneMatch(value -> value < 0);
    }
}
