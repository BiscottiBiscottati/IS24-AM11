package it.polimi.ingsw.am11.cards.util;

public enum Availability implements CornerContainer {
    EMPTY(true),
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
