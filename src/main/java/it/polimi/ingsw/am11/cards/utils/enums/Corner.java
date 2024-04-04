package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.decks.DatabaseSearchable;

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
}
