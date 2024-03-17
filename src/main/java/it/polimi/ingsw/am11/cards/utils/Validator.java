package it.polimi.ingsw.am11.cards.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class Validator {
    private Validator() {
    }

    public static <K> boolean nonNegativeValues(@NotNull Map<K, Integer> map) {
        return map.values().stream().noneMatch(value -> value < 0);
    }
}
