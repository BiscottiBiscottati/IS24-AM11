package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class PlayerColorManager {
    private final @NotNull Set<PlayerColor> colors;

    PlayerColorManager() {
        this.colors = EnumSet.allOf(PlayerColor.class);
    }

    PlayerColor pullAnyColor() throws PlayerInitException {
        PlayerColor color = colors.parallelStream()
                                  .findAny()
                                  .orElseThrow(() -> new PlayerInitException(
                                          "No more colors available"));
        colors.remove(color);

        return color;
    }

    void putBack(PlayerColor color) {
        colors.add(color);
    }

    boolean isAvailable(PlayerColor color) {
        return colors.contains(color);
    }

    PlayerColor pullColor(PlayerColor color) throws PlayerInitException {
        if (colors.remove(color)) return color;
        else throw new PlayerInitException("Color not available");
    }
}
