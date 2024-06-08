package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.DatabaseSearchable;

/**
 * Represent the four corners of a card.
 * <p>
 * Can be <code>DOWN_LX</code>, <code>DOWN_RX</code>, <code>TOP_LX</code> or <code>TOP_RX</code>.
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
