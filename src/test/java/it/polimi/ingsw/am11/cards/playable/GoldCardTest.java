package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertSame(PlayableCardType.GOLD, goldClassic.getType());
        assertSame(PlayableCardType.GOLD, goldSymbols.getType());
    }

    @Test
    void isCornerAvail() {
        assertTrue(goldClassic.isFrontAvailable(Corner.TOP_LX));
        assertTrue(goldClassic.isFrontAvailable(Corner.TOP_RX));
        assertTrue(goldSymbols.isFrontAvailable(Corner.TOP_LX));
        assertTrue(goldSymbols.isFrontAvailable(Corner.TOP_RX));
        assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_LX));
        assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_RX));
        assertFalse(goldSymbols.isFrontAvailable(Corner.DOWN_LX));
        assertTrue(goldSymbols.isFrontAvailable(Corner.DOWN_RX));
    }

    @Test
    void getPlacingRequirements() {
        for (Color color : Color.values()) {
            assertEquals(
                    0,
                    goldSymbols.getPlacingRequirements()
                               .getOrDefault(color, 0)
            );
        }
        assertEquals(
                3,
                goldClassic.getPlacingRequirements().get(Color.RED)
        );
        assertEquals(
                1,
                goldClassic.getPlacingRequirements().get(Color.GREEN)
        );
    }

    @Test
    void getPointsRequirements() {
        assertSame(PointsRequirementsType.CLASSIC, goldClassic.getPointsRequirements());
        assertSame(PointsRequirementsType.SYMBOLS, goldSymbols.getPointsRequirements());
    }

    @Test
    void getSymbolToCollect() {
        assertSame(Symbol.FEATHER, goldSymbols.getSymbolToCollect().orElseThrow());
        assertTrue(goldClassic.getSymbolToCollect().isEmpty());
    }

    @Test
    void getItemCorner() {
        assertSame(Availability.USABLE, goldClassic.getItemCorner(Corner.TOP_LX));
        assertSame(Availability.USABLE, goldSymbols.getItemCorner(Corner.TOP_LX));
        assertSame(Availability.NOT_USABLE, goldClassic.getItemCorner(Corner.DOWN_LX));
        assertSame(Symbol.FEATHER, goldSymbols.getItemCorner(Corner.DOWN_RX));
    }

    @Test
    void checkNulls() {
        assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
        assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
    }

    @Test
    void checkBuilderNulls() {
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(12, 10, null));
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(13, 10, Color.RED)
                             .hasCorner(null)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(14, 10, null));
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(15, 10, Color.RED)
                             .hasCorner(null, true)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(16, 10, Color.RED)
                             .hasRequirements(null, 10)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(17, 10, Color.RED)
                             .hasPointRequirements(null)
        );
    }

    @Test
    void checkNegativePlacingRequirements() {
        assertThrows(IllegalCardBuildException.class,
                     () -> new GoldCard.Builder(18, 3, Color.GREEN)
                             .hasRequirements(Color.BLUE, - 1)
                             .build()
        );
    }

    @Test
    void checkNegativePoints() {
        assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(190, - 1, Color.PURPLE)
                        .build()
        );
    }

    @Test
    void checkNoPointsRequirements() {
        assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(191, 3, Color.PURPLE)
                        .build()
        );
    }
}