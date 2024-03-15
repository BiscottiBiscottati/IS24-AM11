package it.polimi.ingsw.am11.cards.utils;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public final class Validator {
    private Validator() {
    }

    public static <K extends Enum<K>> boolean nonNegativeValues(@NotNull EnumMap<K, Integer> map) {
        return map.values().stream().noneMatch(value -> value < 0);
    }
}
