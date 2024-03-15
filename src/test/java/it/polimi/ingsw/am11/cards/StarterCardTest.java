package it.polimi.ingsw.am11.cards;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.Corner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class StarterCardTest {

    private StarterCard starter;

    @BeforeEach
    void setUp() throws IllegalBuildException {
        starter = new StarterCard.Builder()
                .hasCenterColors(Set.of(Color.RED, Color.BLUE))
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
        Assertions.assertTrue(Set.of(Color.RED, Color.BLUE).containsAll(starter.getCenterColorsFront()));
        Assertions.assertTrue(starter.getCenterColorsFront().containsAll(Set.of(Color.RED, Color.BLUE)));
    }

    @Test
    void checkIllegalBuild() {

        Assertions.assertThrows(IllegalBuildException.class, () ->
                new StarterCard.Builder()
                        .hasCenterColors(Set.of(Color.RED, Color.BLUE))
                        .hasAvailableFrontCorner(Corner.DOWN_LX)
                        .hasAvailableFrontCorner(Corner.TOP_LX)
                        .hasColorBackIn(Corner.TOP_RX, Color.BLUE)
                        .build()
        );

    }
}