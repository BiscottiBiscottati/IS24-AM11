package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.Item;

/**
 * Represents the uncolored symbols contained in the corners of a card.
 * <p>
 * Can be <code>FEATHER</code>, <code>GLASS</code> or <code>PAPER</code>.
 */
public enum Symbol implements CornerContainer, Item {
    FEATHER("feather"),
    GLASS("glass"),
    PAPER("paper");

    private final String columnName;

    Symbol(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
