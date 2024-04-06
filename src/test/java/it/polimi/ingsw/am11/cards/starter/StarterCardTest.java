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
                .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
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
    void getRetroColorIn() {
        Assertions.assertSame(Color.BLUE, starter.getRetroColorIn(Corner.TOP_RX));
        Assertions.assertSame(Color.GREEN, starter.getRetroColorIn(Corner.TOP_LX));
        Assertions.assertSame(Color.RED, starter.getRetroColorIn(Corner.DOWN_RX));
        Assertions.assertSame(Color.PURPLE, starter.getRetroColorIn(Corner.DOWN_LX));

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
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalCardBuildException.class,
                () -> new StarterCard.Builder(100)
                        .hasCenterColors(Set.of(Color.RED, Color.BLUE))
                        .hasItemFrontIn(Corner.DOWN_LX, Color.PURPLE)
                        .hasItemFrontIn(Corner.TOP_LX, Symbol.FEATHER)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .build()
        );

    }

    @Test
    void checkBuilderNulls() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(1)
                        .hasCenterColor(null)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                        .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(1)
                        .hasItemFrontIn(null, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                        .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(2)
                        .hasCenterColors(null)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                        .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(3)
                        .hasColorBackIn(null, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                        .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                        .build()
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new StarterCard.Builder(4)
                        .hasColorBackIn(Corner.TOP_LX, null)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                        .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                        .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                        .build()
        );
    }

    @Test
    void checkNulls() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> starter.getRetroColorIn(null)
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
    void getFront() {
        Assertions.assertEquals(Color.RED, starter.getFront(Corner.TOP_RX));
        Assertions.assertEquals(Availability.USABLE, starter.getFront(Corner.TOP_LX));
        Assertions.assertEquals(Availability.NOT_USABLE, starter.getFront(Corner.DOWN_RX));
        Assertions.assertEquals(Availability.NOT_USABLE, starter.getFront(Corner.DOWN_LX));
    }
}