package it.polimi.ingsw.am11.cards;

public enum Symbol implements CornerContainer {
    GLASS,
    FEATHER,
    PAPER;


    @Override
    public boolean isAvailable() {
        return false;
    }
}
