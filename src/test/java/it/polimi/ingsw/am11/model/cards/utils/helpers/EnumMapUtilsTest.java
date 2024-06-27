package it.polimi.ingsw.am11.model.cards.utils.helpers;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class EnumMapUtilsTest {

    private static EnumMap<Symbol, Integer> toTest;
    private static EnumMap<GameColor, Symbol> toTest2;

    @BeforeAll
    static void beforeAll() {
        toTest = EnumMapUtils.init(Symbol.class, 0);
        toTest2 = EnumMapUtils.init(GameColor.class, Symbol.FEATHER);
    }

    @Test
    void defaultInit() {
        for (Symbol symbol : Symbol.values()) {
            assertEquals(0, toTest.get(symbol));
        }
        for (GameColor color : GameColor.values()) {
            assertSame(Symbol.FEATHER, toTest2.get(color));
        }
    }
}