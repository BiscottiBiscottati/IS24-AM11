package it.polimi.ingsw.am11.persistence;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public record PlateauMemento(@NotNull Map<String, Integer> playerPoints,
                             @NotNull Map<String, Integer> objCounter,
                             @NotNull Map<String, Integer> leaderboard)
        implements Serializable {
}
