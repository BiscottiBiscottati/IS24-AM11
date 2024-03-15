package it.polimi.ingsw.am11.cards.utils;

public enum Symbol implements CornerContainer, Item {
    GLASS,
    FEATHER,
    PAPER;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
