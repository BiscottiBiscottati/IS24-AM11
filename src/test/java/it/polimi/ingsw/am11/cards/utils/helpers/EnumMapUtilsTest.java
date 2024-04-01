package it.polimi.ingsw.am11.cards.utils.helpers;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

class EnumMapUtilsTest {

    private static EnumMap<Symbol, Integer> toTest;
    private static EnumMap<Color, Symbol> toTest2;

    @BeforeAll
    static void beforeAll() {
        toTest = EnumMapUtils.Init(Symbol.class, 0);
        toTest2 = EnumMapUtils.Init(Color.class, Symbol.FEATHER);
    }

    @Test
    void defaultInit() {
        for (Symbol symbol : Symbol.values()) {
            Assertions.assertEquals(0, toTest.get(symbol));
        }
        for (Color color : Color.values()) {
            Assertions.assertSame(Symbol.FEATHER, toTest2.get(color));
        }
    }
}