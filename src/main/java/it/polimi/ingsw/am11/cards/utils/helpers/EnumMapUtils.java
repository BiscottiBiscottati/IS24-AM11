package it.polimi.ingsw.am11.cards.utils.helpers;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A utility class for <code>java.util.EnumMap</code>.
 * <p>
 * Shouldn't be instantiated; use its static methods.
 */
public class EnumMapUtils {
    private EnumMapUtils() {
    }

    /**
     * Creates a new <code>EnumMap</code> with a default value of type V for every key of
     * <code>Enum</code> K.
     *
     * @param keyType      the class of subclass <code>Enum</code> to use as keys
     * @param defaultValue the value of the <code>EnumMap</code>
     * @param <K>          class that extends <code>Enum</code>
     * @param <V>          class of the <code>EnumMap</code> value
     * @return a new instance of the defined <code>EnumMap</code>
     */
    @NotNull
    public static <K extends Enum<K>, V> EnumMap<K, V> Init(
            @NotNull Class<K> keyType,
            @NotNull V defaultValue) {
        return Arrays.stream(keyType.getEnumConstants())
                     .collect(Collectors.toMap(
                             Function.identity(),
                             x -> defaultValue,
                             (x, y) -> x,
                             () -> new EnumMap<>(keyType)
                     ));
    }
}
