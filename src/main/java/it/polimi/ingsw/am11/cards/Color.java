package it.polimi.ingsw.am11.cards;

public enum Color implements CornerContainer {
    RED,
    BLUE,
    GREEN,
    PURPLE;


    @Override
    public boolean isAvailable() {
        return true;
    }
}
