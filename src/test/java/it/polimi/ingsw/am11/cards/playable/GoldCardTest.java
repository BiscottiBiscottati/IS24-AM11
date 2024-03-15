package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoldCardTest {

    private GoldCard goldClassic;
    private PlayableCard goldSymbols;

    @BeforeEach
    void setUp() {
        goldClassic = new GoldCard.Builder(3, Color.RED)
                .hasCorner(Corner.TOP_LX)
                .hasCorner(Corner.TOP_RX, true)
                .hasPointsRequirements(PointsRequirementsType.CLASSIC)
                .hasRequirements(Color.RED, 3)
                .hasRequirements(Color.GREEN, 1)
                .build();

        goldSymbols = new GoldCard.Builder(2, Color.BLUE)
                .hasCorner(Corner.TOP_LX)
                .hasCorner(Corner.TOP_RX)
                .hasCorner(Corner.DOWN_LX, false)
                .hasPointsRequirements(PointsRequirementsType.SYMBOLS)
                .hasSymbolToCollect(Symbol.FEATHER)
                .build();
    }

    @Test
    void getType() {
        Assertions.assertSame(PlayableCardType.GOLD, goldClassic.getType());
        Assertions.assertSame(PlayableCardType.GOLD, goldSymbols.getType());
    }

    @Test
    void isCornerAvail() {
        Assertions.assertTrue(goldClassic.isAvailable(Corner.TOP_LX));
        Assertions.assertTrue(goldClassic.isAvailable(Corner.TOP_RX));
        Assertions.assertTrue(goldSymbols.isAvailable(Corner.TOP_LX));
        Assertions.assertTrue(goldSymbols.isAvailable(Corner.TOP_RX));
        Assertions.assertFalse(goldClassic.isAvailable(Corner.DOWN_LX));
        Assertions.assertFalse(goldClassic.isAvailable(Corner.DOWN_RX));
        Assertions.assertFalse(goldSymbols.isAvailable(Corner.DOWN_LX));
        Assertions.assertFalse(goldSymbols.isAvailable(Corner.DOWN_RX));
    }

    @Test
    void getPlacingRequirements() {
        for (Color color : Color.values()) {
            Assertions.assertEquals(
                    0,
                    goldSymbols.getPlacingRequirements()
                            .getOrDefault(color, 0)
            );
        }
        Assertions.assertEquals(
                3,
                goldClassic.getPlacingRequirements().get(Color.RED)
        );
        Assertions.assertEquals(
                1,
                goldClassic.getPlacingRequirements().get(Color.GREEN)
        );
    }

    @Test
    void getPointsRequirements() {
        Assertions.assertSame(PointsRequirementsType.CLASSIC, goldClassic.getPointsRequirements());
        Assertions.assertSame(PointsRequirementsType.SYMBOLS, goldSymbols.getPointsRequirements());
    }

    @Test
    void getSymbolToCollect() {
        Assertions.assertSame(Symbol.FEATHER, goldSymbols.getSymbolToCollect().orElseThrow());
        Assertions.assertTrue(goldClassic.getSymbolToCollect().isEmpty());
    }

    @Test
    void checkItemCorner() {
        Assertions.assertTrue(goldClassic.checkItemCorner(Corner.TOP_LX).isAvailable());
        Assertions.assertTrue(goldSymbols.checkItemCorner(Corner.TOP_LX).isAvailable());
        Assertions.assertFalse(goldClassic.checkItemCorner(Corner.DOWN_LX).isAvailable());
        Assertions.assertFalse(goldSymbols.checkItemCorner(Corner.DOWN_RX).isAvailable());
    }
}