package it.polimi.ingsw.am11.cards.utils;

public sealed interface CornerContainer permits Availability, Color, Symbol {
    boolean isAvailable();
}
