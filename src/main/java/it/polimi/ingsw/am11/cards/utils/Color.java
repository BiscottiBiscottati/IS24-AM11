package it.polimi.ingsw.am11.cards.utils;

/**
 * Can represent either the color of a card or
 * the colored symbols contained in a corner or center of a card.
 */
public enum Color implements CornerContainer, Item {
    RED,
    BLUE,
    GREEN,
    PURPLE;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
