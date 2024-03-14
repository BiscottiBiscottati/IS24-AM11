package it.polimi.ingsw.am11.cards;

public sealed interface AnySymbol permits Color, Symbol {
    boolean isAvailable();
}