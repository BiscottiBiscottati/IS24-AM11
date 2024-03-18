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

class ColorCollectCardTest {

    private static final Map<Color, Integer> colors = new EnumMap<>(Color.class);
    private static final Map<Symbol, Integer> symbols = new EnumMap<>(Symbol.class);
    private static final HashMap<Position, CardContainer> emptyField = new HashMap<>(10);
    private static final Map<Color, Integer> cardColors = EnumMapUtils.Init(Color.class, 0);
    private static ColorCollectCard card;
    private static ObjectiveCard card2;
    private static ColorCollectCard card3;

    private static ColorCollectCard cardWithMixedColors;

    @BeforeAll
    static void setUp() {

        Random generator = new Random();

        Arrays.stream(Color.values()).forEach(color -> colors.put(color, generator.nextInt(100)));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, generator.nextInt(100)));


        try {
            card = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .build();

            card2 = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE, 3)
                    .build();

            card3 = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE, 3)
                    .hasColor(Color.BLUE)
                    .build();

            cardWithMixedColors = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.GREEN)
                    .hasColor(Color.RED)
                    .hasColor(Color.PURPLE)
                    .build();
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        Assertions.assertTrue(card.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        Assertions.assertTrue(card2.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        Assertions.assertTrue(card3.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
    }

    @Test
    void getColorRequirements() {
        Assertions.assertEquals(3, card.getColorRequirements().get(Color.BLUE));
        Assertions.assertEquals(3, card2.getColorRequirements().get(Color.BLUE));
        Assertions.assertEquals(4, card3.getColorRequirements().get(Color.BLUE));

        for (Color color : Arrays.stream(Color.values())
                                 .filter(key -> key != Color.BLUE)
                                 .toList()) {
            Assertions.assertEquals(0, card.getColorRequirements().get(color));
            Assertions.assertEquals(0, card2.getColorRequirements().get(color));
            Assertions.assertEquals(0, card3.getColorRequirements().get(color));
        }
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card.getType());
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card2.getType());
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card3.getType());
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card.getPoints());
    }

    @Test
    void countPoints() {

        colors.put(Color.BLUE, 6);
        Assertions.assertEquals(4, card.countPoints(emptyField, symbols, colors, cardColors));
        colors.put(Color.BLUE, 8);
        Assertions.assertEquals(4, card.countPoints(emptyField, symbols, colors, cardColors));
        colors.put(Color.BLUE, 12);
        Assertions.assertEquals(8, card.countPoints(emptyField, symbols, colors, cardColors));
        colors.put(Color.BLUE, 0);
        Assertions.assertEquals(0, card.countPoints(emptyField, symbols, colors, cardColors));
        colors.put(Color.BLUE, -1);
        Assertions.assertEquals(0, card.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Color.values()).forEach(color -> colors.put(color, 1));
        Assertions.assertEquals(2, cardWithMixedColors.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Color.values()).forEach(color -> colors.put(color, 1));
        colors.put(Color.BLUE, 30);
        Assertions.assertEquals(2, cardWithMixedColors.countPoints(emptyField, symbols, colors, cardColors));
        Arrays.stream(Color.values()).forEach(color -> colors.put(color, 1));
        colors.put(Color.BLUE, -1);
        Assertions.assertEquals(0, cardWithMixedColors.countPoints(emptyField, symbols, colors, cardColors));
    }
}