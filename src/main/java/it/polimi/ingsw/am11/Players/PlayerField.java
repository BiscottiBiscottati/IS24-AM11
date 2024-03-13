package it.polimi.ingsw.am11.Players;

import java.util.EnumMap;
import java.util.HashMap;

public class PlayerField {

    private HashMap cardsPositioned;
    private EnumMap exposedColours;
    private EnumMap exposedSymbols;
    private EnumMap placedCardColours;

    public PlayerField(HashMap cardsPositioned, EnumMap exposedColours, EnumMap exposedSymbols, EnumMap placedCardColours) {
        this.cardsPositioned = cardsPositioned;
        this.exposedColours = exposedColours;
        this.exposedSymbols = exposedSymbols;
        this.placedCardColours = placedCardColours;
    }

    public HashMap getCardsPositioned() {
        return cardsPositioned;
    }

    public EnumMap getExposedColours() {
        return exposedColours;
    }

    public EnumMap getExposedSymbols() {
        return exposedSymbols;
    }

    public EnumMap getPlacedCardColours() {
        return placedCardColours;
    }
}
