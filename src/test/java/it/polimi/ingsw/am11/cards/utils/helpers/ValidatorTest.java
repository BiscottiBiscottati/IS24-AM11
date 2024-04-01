package it.polimi.ingsw.am11.cards.utils.helpers;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.cards.utils.helpers.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

class ValidatorTest {

    private Map<Color, Integer> testMap;
    private Map<Symbol, Integer> testMap2;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        testMap = new EnumMap<>(Color.class);
        Arrays.stream(Color.values())
              .forEach(color -> testMap.put(color, random.nextInt(1, 100)));

        testMap2 = new EnumMap<>(Symbol.class);
        Arrays.stream(Symbol.values())
              .forEach(symbol -> testMap2.put(symbol, random.nextInt(-10, 0)));
    }

    @Test
    void nonNegativeValues() {
        Assertions.assertTrue(Validator.nonNegativeValues(testMap));
        Assertions.assertFalse(Validator.nonNegativeValues(testMap2));
    }
}