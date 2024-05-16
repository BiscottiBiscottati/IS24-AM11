package it.polimi.ingsw.am11.model.cards.utils.enums;

/**
 * Type of <code>PlayableCard</code> except starter.
 * <p>
 * Can be <code>GOLD</code> or <code>RESOURCE</code>.
 */
public enum PlayableCardType {
    GOLD("gold"),
    RESOURCE("resource");

    private final String typeName;

    PlayableCardType(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return this.typeName;
    }

    @Override
    public String toString() {
        return this.typeName;
    }
}
