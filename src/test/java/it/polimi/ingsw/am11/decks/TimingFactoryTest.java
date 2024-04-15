package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.util.Locale;

@Disabled("TimingFactoryTest is used to measure the time needed to create a deck, not needed for " +
          "normal testing")
public class TimingFactoryTest {

    NumberFormat formatter;

    @BeforeEach
    void setUp() {
        formatter = NumberFormat.getNumberInstance(Locale.ITALY);
        formatter.setMinimumFractionDigits(4);
    }

    @Test
    void testTiming() {

        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ResourceDeckFactory.createDeck();
        }
        long end = System.nanoTime();
        System.out.println(
                "average timing Resource = " + formatter.format(getNumber(end, start)) + " ms");

        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            StarterDeckFactory.createDeck();
        }
        end = System.nanoTime();
        System.out.println(
                "average timing Starter = " + formatter.format(getNumber(end, start)) + " ms");

        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            GoldDeckFactory.createDeck();
        }
        end = System.nanoTime();
        System.out.println(
                "average timing Gold = " + formatter.format(getNumber(end, start)) + " ms");

        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ObjectiveDeckFactory.createDeck();
        }
        end = System.nanoTime();
        System.out.println(
                "average timing Objective = " + formatter.format(getNumber(end, start)) + " ms");

    }

    private static double getNumber(long end, long start) {
        return (double) ((end - start) / 1000) / 1000000;
    }
}
