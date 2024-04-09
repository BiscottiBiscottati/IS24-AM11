package it.polimi.ingsw.am11.cards.starter;

import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

@SuppressWarnings("DataFlowIssue")
class StarterCardTest {

    private StarterCard starter;

    @BeforeEach
    void setUp() throws IllegalCardBuildException {
        starter = new StarterCard.Builder(1)
                .hasCenterColors(Set.of(Color.RED, Color.BLUE))
                .hasCenterColor(Color.BLUE)
                .hasCenterColor(Color.GREEN)
                .hasItemFrontIn(Corner.TOP_LX, Availability.USABLE)
                .hasItemFrontIn(Corner.TOP_RX, Color.RED)
                .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
                .build();

    }

    @Test
    void isFrontAvail() {
        Assertions.assertTrue(starter.isFrontAvail(Corner.TOP_RX));
        Assertions.assertTrue(starter.isFrontAvail(Corner.TOP_LX));
        Assertions.assertFalse(starter.isFrontAvail(Corner.DOWN_RX));
        Assertions.assertFalse(starter.isFrontAvail(Corner.DOWN_LX));
    }

    @Test
    void checkRetroColorIn() {
        Assertions.assertSame(Color.BLUE, starter.checkRetroColorIn(Corner.TOP_RX));
        Assertions.assertSame(Color.GREEN, starter.checkRetroColorIn(Corner.TOP_LX));
        Assertions.assertSame(Color.RED, starter.checkRetroColorIn(Corner.DOWN_RX));
        Assertions.assertSame(Color.PURPLE, starter.checkRetroColorIn(Corner.DOWN_LX));

    }

    @Test
    void getCenterColorsFront() {
        Assertions.assertEquals(Set.of(Color.RED, Color.BLUE, Color.GREEN), starter.getCenterColorsFront());
    }

    @Test
    void checkIllegalBuild() {

        Assertions.assertThrows(
                IllegalCardBuildException.class,
                () -> new StarterCard.Builder(54)
                        .hasCenterColors(Set.of(Color.RED, Color.BLUE))
                        .hasItemFrontIn(Corner.DOWN_LX, Color.PURPLE)
                        .hasItemFrontIn(Corner.TOP_LX, Color.RED)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalCardBuildException.class,
                () -> new StarterCard.Builder(100)
                        .hasCenterColors(Set.of(Color.RED, Color.BLUE))
                        .hasItemFrontIn(Corner.DOWN_LX, Color.PURPLE)
                        .hasItemFrontIn(Corner.TOP_LX, Symbol.FEATHER)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .build()
        );

    }

    @Test
    void checkBuilderNulls() {
        // if JetBrain Annotator is not set, it will throw NullPointerException
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(1)
                        .hasCenterColor(null)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(1)
                        .hasItemFrontIn(null, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(2)
                        .hasCenterColors(null)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(3)
                        .hasColorRetroIn(null, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(4)
                        .hasColorRetroIn(Corner.TOP_LX, null)
                        .hasColorRetroIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorRetroIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorRetroIn(Corner.DOWN_RX, Color.RED)
                        .hasColorRetroIn(Corner.DOWN_LX, Color.PURPLE)
        );
    }

    @Test
    void checkNulls() {
        // if JetBrain Annotator is not set, it will throw NullPointerException
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> starter.checkRetroColorIn(null)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> starter.isFrontAvail(null)
        );
    }

    @Test
    void isColorEqual() {
        Arrays.stream(Color.values()).forEach(
                color -> Assertions.assertFalse(starter.isColorEqual(color))
        );
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, starter.getId());
    }

    @Test
    void checkFront() {
        Assertions.assertEquals(Color.RED, starter.checkFront(Corner.TOP_RX));
        Assertions.assertEquals(Availability.USABLE, starter.checkFront(Corner.TOP_LX));
        Assertions.assertEquals(Availability.NOT_USABLE, starter.checkFront(Corner.DOWN_RX));
        Assertions.assertEquals(Availability.NOT_USABLE, starter.checkFront(Corner.DOWN_LX));
    }
}