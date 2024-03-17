package it.polimi.ingsw.am11.cards.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapUtils {
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
