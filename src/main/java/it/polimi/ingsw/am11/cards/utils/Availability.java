package it.polimi.ingsw.am11.cards.utils;

/**
 * Represents whether another card can cover a corner of a card.
 * <p>
 * Can be <code>USABLE</code> OR <code>NOT_USABLE</code>
 */
public enum Availability implements CornerContainer {
    USABLE(true),
    NOT_USABLE(false);

    private final boolean isAvailable;

    Availability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
}
