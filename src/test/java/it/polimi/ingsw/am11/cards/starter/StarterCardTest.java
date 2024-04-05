package it.polimi.ingsw.am11.cards.starter;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
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
                .hasAvailableFrontCorner(Corner.TOP_LX)
                .hasAvailableFrontCorner(Corner.TOP_RX)
                .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                .hasColorBackIn(Corner.TOP_LX, Color.GREEN)
                .hasColorBackIn(Corner.DOWN_RX, Color.RED)
                .hasColorBackIn(Corner.DOWN_LX, Color.PURPLE)
                .build();

    }

    @Test
    void isFrontCornerAvail() {
        Assertions.assertTrue(starter.isFrontCornerAvail(Corner.TOP_RX));
        Assertions.assertTrue(starter.isFrontCornerAvail(Corner.TOP_LX));
        Assertions.assertFalse(starter.isFrontCornerAvail(Corner.DOWN_RX));
        Assertions.assertFalse(starter.isFrontCornerAvail(Corner.DOWN_LX));
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
                        .hasAvailableFrontCorner(Corner.DOWN_LX)
                        .hasAvailableFrontCorner(Corner.TOP_LX)
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
                        .hasAvailableFrontCorner(null)
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
                () -> starter.isFrontCornerAvail(null)
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
}