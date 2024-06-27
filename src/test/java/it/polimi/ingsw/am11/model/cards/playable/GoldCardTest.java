package it.polimi.ingsw.am11.model.cards.playable;

import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.*;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    static GoldCard goldClassic;
    static PlayableCard goldSymbols;
    static PlayableCard goldCovering;
    static Deck<ResourceCard> resourceDeck;
    static Deck<StarterCard> starterDeck;

    @BeforeAll
    static void beforeAll() {
        try {
            goldClassic = new GoldCard.Builder(1, 3, GameColor.RED)
                    .hasCorner(Corner.TOP_LX)
                    .hasCorner(Corner.TOP_RX, true)
                    .hasPointRequirements(PointsRequirementsType.CLASSIC)
                    .hasRequirements(GameColor.RED, 3)
                    .hasRequirements(GameColor.GREEN, 1)
                    .build();
            goldSymbols = new GoldCard.Builder(2, 2, GameColor.BLUE)
                    .hasCorner(Corner.TOP_LX)
                    .hasCorner(Corner.TOP_RX)
                    .hasCorner(Corner.DOWN_LX, false)
                    .hasIn(Corner.DOWN_RX, Symbol.FEATHER)
                    .hasPointRequirements(PointsRequirementsType.SYMBOLS)
                    .hasSymbolToCollect(Symbol.FEATHER)
                    .build();
            goldCovering = new GoldCard.Builder(3, 2, GameColor.PURPLE)
                    .hasCorner(Corner.TOP_LX)
                    .hasCorner(Corner.TOP_RX)
                    .hasPointRequirements(PointsRequirementsType.COVERING_CORNERS)
                    .build();
        } catch (IllegalCardBuildException e) {
            fail(e);
        }

        resourceDeck = ResourceDeckFactory.createDeck();
        starterDeck = StarterDeckFactory.createDeck();

    }

    @BeforeEach
    void setUp() {
        resourceDeck.reset();
        starterDeck.reset();
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
        for (GameColor color : GameColor.values()) {
            assertEquals(
                    0,
                    goldSymbols.getPlacingRequirements()
                               .getOrDefault(color, 0)
            );
        }
        assertEquals(
                3,
                goldClassic.getPlacingRequirements().get(GameColor.RED)
        );
        assertEquals(
                1,
                goldClassic.getPlacingRequirements().get(GameColor.GREEN)
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

    @Disabled("Disabled because of not null assertions depends on IDE settings")
    @Test
    void checkNulls() {
        assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
        assertThrows(IllegalArgumentException.class, () -> goldClassic.getItemCorner(null));
        assertThrows(IllegalArgumentException.class, () -> goldSymbols.isFrontAvailable(null));
    }

    @Disabled("Disabled because of not null assertions depends on IDE settings")
    @Test
    void checkBuilderNulls() {
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(12, 10, null));
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(13, 10, GameColor.RED)
                             .hasCorner(null)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(14, 10, null));
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(15, 10, GameColor.RED)
                             .hasCorner(null, true)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(16, 10, GameColor.RED)
                             .hasRequirements(null, 10)
        );
        assertThrows(IllegalArgumentException.class,
                     () -> new GoldCard.Builder(17, 10, GameColor.RED)
                             .hasPointRequirements(null)
        );
    }

    @Test
    void checkNegativePlacingRequirements() {
        assertThrows(IllegalCardBuildException.class,
                     () -> new GoldCard.Builder(18, 3, GameColor.GREEN)
                             .hasRequirements(GameColor.BLUE, - 1)
                             .build()
        );
    }

    @Test
    void checkNegativePoints() {
        assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(190, - 1, GameColor.PURPLE)
                        .build()
        );
    }

    @Test
    void checkNoPointsRequirements() {
        assertThrows(
                IllegalCardBuildException.class,
                () -> new GoldCard.Builder(191, 3, GameColor.PURPLE)
                        .build()
        );
    }

    @Test
    void isFrontAvailable() {
        assertTrue(goldClassic.isFrontAvailable(Corner.TOP_LX));
        assertTrue(goldClassic.isFrontAvailable(Corner.TOP_RX));
        assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_LX));
        assertFalse(goldClassic.isFrontAvailable(Corner.DOWN_RX));
    }

    @Test
    void getPlacingRequirementsOf() {
        assertEquals(3, goldClassic.getPlacingRequirementsOf(GameColor.RED));
        assertEquals(1, goldClassic.getPlacingRequirementsOf(GameColor.GREEN));
        assertEquals(0, goldClassic.getPlacingRequirementsOf(GameColor.BLUE));
        assertEquals(0, goldClassic.getPlacingRequirementsOf(GameColor.PURPLE));
    }

    @Test
    void isAvailable() {
        assertTrue(goldClassic.isAvailable(Corner.TOP_LX, true));
        assertTrue(goldClassic.isAvailable(Corner.TOP_RX, true));
        assertFalse(goldClassic.isAvailable(Corner.DOWN_LX, false));
        assertFalse(goldClassic.isAvailable(Corner.DOWN_RX, false));
    }

    @Test
    void countPoints() {
        PlayerField field = new PlayerField();
        assertEquals(3, goldClassic.countPoints(field, Position.of(0, 0)));

        try {
            field.placeStartingCard(starterDeck.draw().orElseThrow(), true);
            field.place(resourceDeck.draw().orElseThrow(), Position.of(1, - 1), true);
            field.place(resourceDeck.draw().orElseThrow(), Position.of(1, 1), true);
            field.place(goldSymbols, Position.of(- 1, - 1), false);
            field.place(goldSymbols, Position.of(- 1, 1), false);
        } catch (IllegalCardPlacingException e) {
            fail(e);
        }

        assertEquals(4, goldCovering.countPoints(field, Position.of(2, 0)));
        try {
            assertEquals(6, field.place(goldSymbols, Position.of(2, 0), false));
        } catch (IllegalCardPlacingException e) {
            fail(e);
        }

    }
}