package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.EnumMapUtils;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

class SymbolCollectCardTest {

    private static final Map<Color, Integer> colors = new EnumMap<>(Color.class);
    private static final Map<Symbol, Integer> symbols = new EnumMap<>(Symbol.class);
    private static final HashMap<Position, CardContainer> emptyField = new HashMap<>(10);
    private static final Map<Color, Integer> cardColors = EnumMapUtils.Init(Color.class, 0);

    private static SymbolCollectCard card;
    private static SymbolCollectCard card2;

    @BeforeAll
    static void setUp() {

        Random generator = new Random();

        Arrays.stream(Color.values()).forEach(color -> colors.put(color, generator.nextInt(100)));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, generator.nextInt(100)));


        try {
            card = new SymbolCollectCard.Builder(2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.GLASS, 1)
                    .hasSymbol(Symbol.PAPER, 1)
                    .build();

            card2 = new SymbolCollectCard.Builder(2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.FEATHER)
                    .build();
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        Assertions.assertTrue(
                card.getSymbolRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 1)
        );
        Assertions.assertTrue(
                Arrays.stream(Symbol.values())
                      .filter(symbol -> symbol != Symbol.FEATHER)
                      .map(symbol -> card2.getSymbolRequirements().get(symbol))
                      .allMatch(integer -> integer == 0)
        );
        Assertions.assertEquals(2, card2.getSymbolRequirements().get(Symbol.FEATHER));
    }

    @Test
    void getColorRequirements() {
        Assertions.assertTrue(
                card.getColorRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 0)
        );
        Assertions.assertTrue(
                card2.getColorRequirements()
                     .values()
                     .stream()
                     .allMatch(value -> value == 0)
        );
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.OBJECT_COLLECT, card.getType());
        Assertions.assertSame(ObjectiveCardType.OBJECT_COLLECT, card2.getType());
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card2.getPoints());
    }

    @Test
    void checkIllegalBuild() {
        Assertions.assertThrows(
                IllegalBuildException.class,
                () -> new SymbolCollectCard.Builder(2)
                        .hasSymbol(Symbol.FEATHER)
                        .hasSymbol(Symbol.GLASS, -1)
                        .build()
        );
    }

    @Test
    void countPoints() {
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, 1));
        Assertions.assertEquals(2, card.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, 2));
        Assertions.assertEquals(4, card.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, 3));
        symbols.put(Symbol.FEATHER, 30);
        symbols.put(Symbol.PAPER, 4);
        Assertions.assertEquals(6, card.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, 1));
        symbols.put(Symbol.FEATHER, -1);
        Assertions.assertEquals(0, card.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, -1));
        Assertions.assertEquals(0, card.countPoints(emptyField, symbols, colors, cardColors));
    }
}