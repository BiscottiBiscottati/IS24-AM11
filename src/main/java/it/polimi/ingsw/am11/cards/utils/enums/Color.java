package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.decks.DatabaseSearchable;

/**
 * Can represent either the color of a card or
 * the colored symbols contained in a corner or center of a card.
 */
public enum Color implements CornerContainer, Item, DatabaseSearchable {
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    PURPLE("purple");

    private final String columnName;

    Color(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }
}
