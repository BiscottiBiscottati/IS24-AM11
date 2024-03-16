package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoldCardTest {

    private GoldCard goldClassic;
    private PlayableCard goldSymbols;

    @BeforeEach
    void setUp() {
        try {
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
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
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
        Assertions.assertSame(Availability.USABLE, goldClassic.checkItemCorner(Corner.TOP_LX));
        Assertions.assertSame(Availability.USABLE, goldSymbols.checkItemCorner(Corner.TOP_LX));
        Assertions.assertSame(Availability.NOT_USABLE, goldClassic.checkItemCorner(Corner.DOWN_LX));
        Assertions.assertSame(Availability.NOT_USABLE, goldSymbols.checkItemCorner(Corner.DOWN_RX));
    }

    @Test
    void checkNulls() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldClassic.checkItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldSymbols.isAvailable(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldClassic.checkItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldSymbols.isAvailable(null));
    }

    @Test
    void checkBuilderNulls() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, null).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, Color.RED)
                                        .hasCorner(null)
                                        .build()
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, null).build());
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, Color.RED)
                                        .hasCorner(null, true)
                                        .build()
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, Color.RED)
                                        .hasSymbolToCollect(null)
                                        .build()
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, Color.RED)
                                        .hasRequirements(null, 10)
                                        .build()
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(10, Color.RED)
                                        .hasPointsRequirements(null)
                                        .build()
        );
    }

    @Test
    void checkNegativePlacingRequirements() {
        Assertions.assertThrows(IllegalBuildException.class,
                                () -> new GoldCard.Builder(3, Color.GREEN)
                                        .hasRequirements(Color.BLUE, -1)
                                        .build()
        );
    }

    @Test
    void checkNegativePoints() {
        Assertions.assertThrows(
                IllegalBuildException.class,
                () -> new GoldCard.Builder(-1, Color.PURPLE)
                        .build()
        );
    }
}