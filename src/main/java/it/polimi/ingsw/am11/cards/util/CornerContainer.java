package it.polimi.ingsw.am11.cards.util;

public sealed interface CornerContainer permits Availability, Color, Symbol {
    boolean isAvailable();
}
