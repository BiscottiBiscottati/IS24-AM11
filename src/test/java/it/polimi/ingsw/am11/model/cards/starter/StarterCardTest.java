package it.polimi.ingsw.am11.model.cards.starter;

import it.polimi.ingsw.am11.model.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class StarterCardTest {

    private StarterCard starter;

    @BeforeEach
    void setUp() throws IllegalCardBuildException {
        starter = new StarterCard.Builder(1)
                .hasCenterColors(Set.of(GameColor.RED, GameColor.BLUE))
                .hasCenterColor(GameColor.BLUE)
                .hasCenterColor(GameColor.GREEN)
                .hasItemFrontIn(Corner.TOP_LX, Availability.USABLE)
                .hasItemFrontIn(Corner.TOP_RX, GameColor.RED)
                .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
                .build();

    }

    @Test
    void isFrontAvail() {
        assertTrue(starter.isFrontAvail(Corner.TOP_RX));
        assertTrue(starter.isFrontAvail(Corner.TOP_LX));
        assertFalse(starter.isFrontAvail(Corner.DOWN_RX));
        assertFalse(starter.isFrontAvail(Corner.DOWN_LX));
    }

    @Test
    void checkRetroColorIn() {
        assertSame(GameColor.BLUE, starter.checkRetroColorIn(Corner.TOP_RX));
        assertSame(GameColor.GREEN, starter.checkRetroColorIn(Corner.TOP_LX));
        assertSame(GameColor.RED, starter.checkRetroColorIn(Corner.DOWN_RX));
        assertSame(GameColor.PURPLE, starter.checkRetroColorIn(Corner.DOWN_LX));

    }

    @Test
    void getCenterColorsFront() {
        assertEquals(Set.of(GameColor.RED, GameColor.BLUE, GameColor.GREEN),
                     starter.getCenterColorsFront());
    }

    @Test
    void checkIllegalBuild() {

        assertThrows(
                IllegalCardBuildException.class,
                () -> new StarterCard.Builder(54)
                        .hasCenterColors(Set.of(GameColor.RED, GameColor.BLUE))
                        .hasItemFrontIn(Corner.DOWN_LX, GameColor.PURPLE)
                        .hasItemFrontIn(Corner.TOP_LX, GameColor.RED)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .build()
        );
        assertThrows(
                IllegalCardBuildException.class,
                () -> new StarterCard.Builder(100)
                        .hasCenterColors(Set.of(GameColor.RED, GameColor.BLUE))
                        .hasItemFrontIn(Corner.DOWN_LX, GameColor.PURPLE)
                        .hasItemFrontIn(Corner.TOP_LX, Symbol.FEATHER)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .build()
        );

    }

    @Disabled("Disabled because of not null assertions depends on IDE settings")
    @Test
    void checkBuilderNulls() {
        // if JetBrain Annotator is not set, it will throw NullPointerException
        assertThrows(
                RuntimeException.class,
                () -> new StarterCard.Builder(1)
                        .hasCenterColor(null)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
        );
        assertThrows(
                RuntimeException.class,
                () -> new StarterCard.Builder(1)
                        .hasItemFrontIn(null, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
        );
        assertThrows(
                RuntimeException.class,
                () -> new StarterCard.Builder(2)
                        .hasCenterColors(null)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
        );
        assertThrows(
                RuntimeException.class,
                () -> new StarterCard.Builder(3)
                        .hasColorRetroIn(null, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(4)
                        .hasColorRetroIn(Corner.TOP_LX, null)
                        .hasColorRetroIn(Corner.TOP_RX, GameColor.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, GameColor.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, GameColor.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, GameColor.PURPLE)
        );
    }

    @Disabled("Disabled because of not null assertions depends on IDE settings")
    @Test
    void checkNulls() {
        // if JetBrain Annotator is not set, it will throw NullPointerException
        assertThrows(
                RuntimeException.class,
                () -> starter.checkRetroColorIn(null)
        );
        assertThrows(
                RuntimeException.class,
                () -> starter.isFrontAvail(null)
        );
    }

    @Test
    void isColorEqual() {
        Arrays.stream(GameColor.values()).forEach(
                color -> assertFalse(starter.isColorEqual(color))
        );
    }

    @Test
    void getId() {
        assertEquals(1, starter.getId());
    }

    @Test
    void checkFront() {
        assertEquals(GameColor.RED, starter.checkFront(Corner.TOP_RX));
        assertEquals(Availability.USABLE, starter.checkFront(Corner.TOP_LX));
        assertEquals(Availability.NOT_USABLE, starter.checkFront(Corner.DOWN_RX));
        assertEquals(Availability.NOT_USABLE, starter.checkFront(Corner.DOWN_LX));
    }
}