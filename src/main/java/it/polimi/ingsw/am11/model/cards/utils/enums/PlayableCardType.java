package it.polimi.ingsw.am11.model.cards.utils.enums;

import it.polimi.ingsw.am11.model.cards.utils.DatabaseSearchable;

/**
 * Type of <code>PlayableCard</code> except starter.
 * <p>
 * Can be <code>GOLD</code> or <code>RESOURCE</code>.
 */
public enum PlayableCardType implements DatabaseSearchable {
    GOLD("gold", "gold"),
    RESOURCE("resource", "res");

    private final String typeName;
    private final String columnName;

    PlayableCardType(String typeName, String columnName) {
        this.typeName = typeName;
        this.columnName = columnName;
    }

    public String getName() {
        return this.typeName;
    }

    @Override
    public String toString() {
        return this.typeName;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }
}
