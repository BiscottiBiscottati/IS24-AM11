package it.polimi.ingsw.am11.model.cards.objective.collecting;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SymbolCollectCardTest {

    @Mock
    private static PlayerField playerField;
    @InjectMocks
    private static ObjectiveCard card;
    private static ObjectiveCard card2;

    @BeforeAll
    static void setUp() {
        try {
            card = new SymbolCollectCard.Builder(1, 2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.GLASS, 1)
                    .hasSymbol(Symbol.PAPER, 1)
                    .build();

            card2 = new SymbolCollectCard.Builder(2, 2)
                    .hasSymbol(Symbol.FEATHER)
                    .hasSymbol(Symbol.FEATHER)
                    .build();
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        assertTrue(
                card.getSymbolRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 1)
        );
        assertTrue(
                Arrays.stream(Symbol.values())
                      .filter(symbol -> symbol != Symbol.FEATHER)
                      .map(symbol -> card2.getSymbolRequirements().get(symbol))
                      .allMatch(integer -> Objects.requireNonNull(integer) == 0)
        );
        assertEquals(2, card2.getSymbolRequirements().get(Symbol.FEATHER));
    }

    @Test
    void getColorRequirements() {
        assertTrue(
                card.getColorRequirements()
                    .values()
                    .stream()
                    .allMatch(value -> value == 0)
        );
        assertTrue(
                card2.getColorRequirements()
                     .values()
                     .stream()
                     .allMatch(value -> value == 0)
        );
    }

    @Test
    void getType() {
        assertSame(ObjectiveCardType.SYMBOL_COLLECT, card.getType());
        assertSame(ObjectiveCardType.SYMBOL_COLLECT, card2.getType());
    }

    @Test
    void getPoints() {
        assertEquals(2, card.getPoints());
        assertEquals(2, card2.getPoints());
    }

    @Test
    void checkIllegalBuild() {
        assertThrows(
                IllegalCardBuildException.class,
                () -> new SymbolCollectCard.Builder(3, 2)
                        .hasSymbol(Symbol.FEATHER)
                        .hasSymbol(Symbol.GLASS, - 1)
                        .build()
        );
    }

    @Test
    void countPoints() {
        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(1)
        );
        assertEquals(2, card.countPoints(playerField));

        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(2)
        );
        assertEquals(4, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Symbol.GLASS)).thenReturn(3);
        Mockito.when(playerField.getNumberOf(Symbol.FEATHER)).thenReturn(30);
        Mockito.when(playerField.getNumberOf(Symbol.PAPER)).thenReturn(4);
        assertEquals(6, card.countPoints(playerField));


        Mockito.when(playerField.getNumberOf(Symbol.PAPER)).thenReturn(- 1);
        assertEquals(0, card.countPoints(playerField));

        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(- 1)
        );
        assertEquals(0, card.countPoints(playerField));
    }
}