package it.polimi.ingsw.am11.cards;

public sealed interface Item permits Color, Symbol {
    boolean isAvailable();
}