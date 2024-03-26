package it.polimi.ingsw.am11.cards.utils.enums;

import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.Item;

/**
 * Represents the uncolored symbols contained in the corners of a card.
 */
public enum Symbol implements CornerContainer, Item {
    FEATHER,
    GLASS,
    PAPER;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
