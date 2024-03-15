package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.Item;
import it.polimi.ingsw.am11.cards.util.Symbol;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;

import java.util.EnumMap;
import java.util.HashMap;

public class PlayerField {

    private final EnumMap<Color, Integer> exposedColors;
    private final EnumMap<Symbol, Integer> exposedSymbols;
    private HashMap cardsPositioned;
    private EnumMap placedCardColors;

    public PlayerField() {
        exposedColors = new EnumMap<>(Color.class);
        exposedColors.put(Color.RED, 0);
        exposedColors.put(Color.BLUE, 0);
        exposedColors.put(Color.GREEN, 0);
        exposedColors.put(Color.PURPLE, 0);
        exposedSymbols = new EnumMap<>(Symbol.class);
        exposedSymbols.put(Symbol.FEATHER, 0);
        exposedSymbols.put(Symbol.GLASS, 0);
        exposedSymbols.put(Symbol.PAPER, 0);
    }

    public HashMap getCardsPositioned() {
        return cardsPositioned;
    }

    public EnumMap getExposedColours() {
        return exposedColors;
    }

    public EnumMap getExposedSymbols() {
        return exposedSymbols;
    }

    public EnumMap getPlacedCardColours() {
        return placedCardColors;
    }

    public int getNumberOf(Item item) {
        return 0;
    }

    public void placeStartingCard(StarterCard firstCard) {
        //place startercard in the field
    }

    public void place(PlayableCard card, int x, int y) {
        //place a new card in position (x,y)
        //not sure on how to implement, it may change
    }
}
