package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.players.PlayerField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
class SymbolCollectCardTest {

    private static final Map<Color, Integer> colors = new EnumMap<>(Color.class);
    private static final Map<Symbol, Integer> symbols = new EnumMap<>(Symbol.class);
    private static final Map<Color, Integer> cardColors = EnumMapUtils.Init(Color.class, 0);
    @Mock
    private static PlayerField playerField;
    @InjectMocks
    private static SymbolCollectCard card;
    private static SymbolCollectCard card2;

    @BeforeAll
    static void setUp() {

        Random generator = new Random();

        Arrays.stream(Color.values()).forEach(color -> colors.put(color, generator.nextInt(100)));
        Arrays.stream(Symbol.values()).forEach(symbol -> symbols.put(symbol, generator.nextInt(100)));


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
        Assertions.assertSame(ObjectiveCardType.SYMBOL_COLLECT, card.getType());
        Assertions.assertSame(ObjectiveCardType.SYMBOL_COLLECT, card2.getType());
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
                () -> new SymbolCollectCard.Builder(3, 2)
                        .hasSymbol(Symbol.FEATHER)
                        .hasSymbol(Symbol.GLASS, -1)
                        .build()
        );
    }

    @Test
    void countPoints() {
        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(1)
        );
        Assertions.assertEquals(2, card.countPoints(playerField));

        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(2)
        );
        Assertions.assertEquals(4, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Symbol.GLASS)).thenReturn(3);
        Mockito.when(playerField.getNumberOf(Symbol.FEATHER)).thenReturn(30);
        Mockito.when(playerField.getNumberOf(Symbol.PAPER)).thenReturn(4);
        Assertions.assertEquals(6, card.countPoints(playerField));


        Mockito.when(playerField.getNumberOf(Symbol.PAPER)).thenReturn(-1);
        Assertions.assertEquals(0, card.countPoints(playerField));

        Arrays.stream(Symbol.values()).forEach(
                symbol -> Mockito.when(playerField.getNumberOf(symbol)).thenReturn(-1)
        );
        Assertions.assertEquals(0, card.countPoints(playerField));
    }
}