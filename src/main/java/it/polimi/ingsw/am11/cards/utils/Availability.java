package it.polimi.ingsw.am11.cards.utils;

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
