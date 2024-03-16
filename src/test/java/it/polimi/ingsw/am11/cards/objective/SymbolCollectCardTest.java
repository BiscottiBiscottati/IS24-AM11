package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.Symbol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SymbolCollectCardTest {

    private SymbolCollectCard card;
    private SymbolCollectCard card2;

    @BeforeEach
    void setUp() {
        try {
            card = new SymbolCollectCard.Builder(2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.GLASS, 1)
                    .hasSymbol(Symbol.PAPER, 1)
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
    }

    @Test
    void getColorRequirements() {
        Assertions.assertTrue(
                card.getColorRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 0)
        );
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.OBJECT_COLLECT, card.getType());
    }
}