package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.decks.DatabaseSearchable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

/**
 * Represent the four corners of a card.
 */
public enum Corner implements DatabaseSearchable {
    DOWN_LX("down_lx"),
    DOWN_RX("down_rx"),
    TOP_LX("top_lx"),
    TOP_RX("top_rx");

    private final String columnName;

    Corner(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }

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
