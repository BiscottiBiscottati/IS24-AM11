package it.polimi.ingsw.am11.cards.utils.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

/**
 * Represent the four corners of a card.
 */
public enum Corner {
    DOWN_LX,
    DOWN_RX,
    TOP_LX,
    TOP_RX;

    private static final Map<Corner, List<Corner>> mapToSingletonList = Map.of(
            DOWN_LX, List.of(DOWN_LX),
            DOWN_RX, List.of(DOWN_RX),
            TOP_LX, List.of(TOP_LX),
            TOP_RX, List.of(TOP_RX)
    );

    public @NotNull @Unmodifiable List<Corner> toSingletonList() {
        return mapToSingletonList.get(this);
    }

}
