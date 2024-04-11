package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("DataFlowIssue")
class GoldCardTest {

    private static GoldCard goldClassic;
    private static PlayableCard goldSymbols;

    @BeforeAll
    static void beforeAll() {
        try {
            goldClassic = new GoldCard.Builder(1, 3, Color.RED)
                    .hasCorner(Corner.TOP_LX)
                    .hasCorner(Corner.TOP_RX, true)
                    .hasPointRequirements(PointsRequirementsType.CLASSIC)
                    .hasRequirements(Color.RED, 3)
                    .hasRequirements(Color.GREEN, 1)
                    .build();
            goldSymbols = new GoldCard.Builder(2, 2, Color.BLUE)
                    .hasCorner(Corner.TOP_LX)
                    .hasCorner(Corner.TOP_RX)
                    .hasCorner(Corner.DOWN_LX, false)
                    .hasIn(Corner.DOWN_RX, Symbol.FEATHER)
                    .hasPointRequirements(PointsRequirementsType.SYMBOLS)
                    .hasSymbolToCollect(Symbol.FEATHER)
                    .build();
        } catch (IllegalCardBuildException e) {
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
        Assertions.assertTrue(goldClassic.isFrontAvailable(Corner.TOP_LX));
        Assertions.assertTrue(goldClassic.isFrontAvailable(Corner.TOP_RX));
        Assertions.assertTrue(goldSymbols.isFrontAvailable(Corner.TOP_LX));
        Assertions.assertTrue(goldSymbols.isFrontAvailable(Corner.TOP_RX));
        Assertions.assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_LX));
        Assertions.assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_RX));
        Assertions.assertFalse(goldSymbols.isFrontAvailable(Corner.DOWN_LX));
        Assertions.assertTrue(goldSymbols.isFrontAvailable(Corner.DOWN_RX));
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
    void getItemCorner() {
        Assertions.assertSame(Availability.USABLE, goldClassic.getItemCorner(Corner.TOP_LX));
        Assertions.assertSame(Availability.USABLE, goldSymbols.getItemCorner(Corner.TOP_LX));
        Assertions.assertSame(Availability.NOT_USABLE, goldClassic.getItemCorner(Corner.DOWN_LX));
        Assertions.assertSame(Symbol.FEATHER, goldSymbols.getItemCorner(Corner.DOWN_RX));
    }

    @Test
    void checkNulls() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
    }

    @Test
    void checkBuilderNulls() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(12, 10, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(13, 10, Color.RED)
                                        .hasCorner(null)
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(14, 10, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(15, 10, Color.RED)
                                        .hasCorner(null, true)
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(16, 10, Color.RED)
                                        .hasRequirements(null, 10)
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> new GoldCard.Builder(17, 10, Color.RED)
                                        .hasPointRequirements(null)
        );
    }

    @Test
    void checkNegativePlacingRequirements() {
        Assertions.assertThrows(IllegalCardBuildException.class,
                                () -> new GoldCard.Builder(18, 3, Color.GREEN)
                                        .hasRequirements(Color.BLUE, -1)
                                        .build()
        );
    }

    @Test
    void checkNegativePoints() {
        Assertions.assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(190, -1, Color.PURPLE)
                        .build()
        );
    }

    @Test
    void checkNoPointsRequirements() {
        Assertions.assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(191, 3, Color.PURPLE)
                        .build()
        );
    }
}