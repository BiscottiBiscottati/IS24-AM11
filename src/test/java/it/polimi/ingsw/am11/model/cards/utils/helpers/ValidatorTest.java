package it.polimi.ingsw.am11.model.cards.utils.helpers;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    private Map<GameColor, Integer> testMap;
    private Map<Symbol, Integer> testMap2;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        testMap = new EnumMap<>(GameColor.class);
        Arrays.stream(GameColor.values())
              .forEach(color -> testMap.put(color, random.nextInt(1, 100)));

        testMap2 = new EnumMap<>(Symbol.class);
        Arrays.stream(Symbol.values())
              .forEach(symbol -> testMap2.put(symbol, random.nextInt(- 10, 0)));
    }

    @Test
    void nonNegativeValues() {
        assertTrue(Validator.nonNegativeValues(testMap));
        assertFalse(Validator.nonNegativeValues(testMap2));
    }
}