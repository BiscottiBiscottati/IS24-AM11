package it.polimi.ingsw.am11.cards.util;

import it.polimi.ingsw.am11.cards.util.CornerContainer;
import it.polimi.ingsw.am11.cards.util.Item;

public enum Symbol implements CornerContainer, Item {
    GLASS,
    FEATHER,
    PAPER;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
