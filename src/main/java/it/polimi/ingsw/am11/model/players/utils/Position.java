package it.polimi.ingsw.am11.model.players.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Position(int x, int y) {
    @Contract("_, _ -> new")
    public static @NotNull Position of(int x, int y) {
        return new Position(x, y);
    }

}
