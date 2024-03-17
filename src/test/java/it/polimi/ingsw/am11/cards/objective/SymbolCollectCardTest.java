package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class SymbolCollectCardTest {

    private static SymbolCollectCard card;
    private static SymbolCollectCard card2;

    @BeforeAll
    static void setUp() {
        try {
            card = new SymbolCollectCard.Builder(2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.GLASS, 1)
                    .hasSymbol(Symbol.PAPER, 1)
                    .build();

            card2 = new SymbolCollectCard.Builder(2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.FEATHER)
                    .build();
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        Assertions.assertTrue(
                card.getSymbolRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 1)
        );
        Assertions.assertTrue(
                Arrays.stream(Symbol.values())
                      .filter(symbol -> symbol != Symbol.FEATHER)
                      .map(symbol -> card2.getSymbolRequirements().get(symbol))
                      .allMatch(integer -> integer == 0)
        );
        Assertions.assertEquals(2, card2.getSymbolRequirements().get(Symbol.FEATHER));
    }

    @Test
    void getColorRequirements() {
        Assertions.assertTrue(
                card.getColorRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 0)
        );
        Assertions.assertTrue(
                card2.getColorRequirements()
                     .values()
                     .stream()
                     .allMatch(value -> value == 0)
        );
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.OBJECT_COLLECT, card.getType());
        Assertions.assertSame(ObjectiveCardType.OBJECT_COLLECT, card2.getType());
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card2.getPoints());
    }

    @Test
    void checkIllegalBuild() {
        Assertions.assertThrows(
                IllegalBuildException.class,
                () -> new SymbolCollectCard.Builder(2)
                        .hasSymbol(Symbol.FEATHER)
                        .hasSymbol(Symbol.GLASS, -1)
                        .build()
        );
    }
}