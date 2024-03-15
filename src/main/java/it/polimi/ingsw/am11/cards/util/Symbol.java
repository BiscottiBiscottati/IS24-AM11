package it.polimi.ingsw.am11.cards.util;

public enum Symbol implements CornerContainer, Item {
    GLASS,
    FEATHER,
    PAPER;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
