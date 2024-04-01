package it.polimi.ingsw.am11.cards.objective.collecting;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
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

@ExtendWith(MockitoExtension.class)
class ColorCollectCardTest {

    private static final Map<Color, Integer> colors = new EnumMap<>(Color.class);
    private static final Map<Symbol, Integer> symbols = new EnumMap<>(Symbol.class);
    @InjectMocks
    private static ColorCollectCard card;
    private static ObjectiveCard card2;
    private static ColorCollectCard card3;
    @InjectMocks
    private static ColorCollectCard cardWithMixedColors;
    @Mock
    PlayerField playerField;

    @BeforeAll
    static void setUp() {

        try {
            card = new ColorCollectCard.Builder(1, 2)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .build();

            card2 = new ColorCollectCard.Builder(2, 2)
                    .hasColor(Color.BLUE, 3)
                    .build();

            card3 = new ColorCollectCard.Builder(3, 2)
                    .hasColor(Color.BLUE, 3)
                    .hasColor(Color.BLUE)
                    .build();

            cardWithMixedColors = new ColorCollectCard.Builder(4, 2)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.GREEN)
                    .hasColor(Color.RED)
                    .hasColor(Color.PURPLE)
                    .build();
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        Assertions.assertTrue(card.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        Assertions.assertTrue(card2.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        Assertions.assertTrue(card3.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
    }

    @Test
    void getColorRequirements() {
        Assertions.assertEquals(3, card.getColorRequirements().get(Color.BLUE));
        Assertions.assertEquals(3, card2.getColorRequirements().get(Color.BLUE));
        Assertions.assertEquals(4, card3.getColorRequirements().get(Color.BLUE));

        for (Color color : Arrays.stream(Color.values())
                                 .filter(key -> key != Color.BLUE)
                                 .toList()) {
            Assertions.assertEquals(0, card.getColorRequirements().get(color));
            Assertions.assertEquals(0, card2.getColorRequirements().get(color));
            Assertions.assertEquals(0, card3.getColorRequirements().get(color));
        }
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card.getType());
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card2.getType());
        Assertions.assertSame(ObjectiveCardType.COLOR_COLLECT, card3.getType());
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card.getPoints());
        Assertions.assertEquals(2, card.getPoints());
    }

    @Test
    void countPoints() {

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(4);
        Assertions.assertEquals(2, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(8);
        Assertions.assertEquals(4, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(12);
        Assertions.assertEquals(8, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(0);
        Assertions.assertEquals(0, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(-1);
        Assertions.assertEquals(0, card.countPoints(playerField));

        Arrays.stream(Color.values()).forEach(
                color -> Mockito.when(playerField.getNumberOf(color)).thenReturn(1)
        );
        Assertions.assertEquals(2, cardWithMixedColors.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(30);
        Assertions.assertEquals(2, cardWithMixedColors.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(-1);
        Assertions.assertEquals(0, cardWithMixedColors.countPoints(playerField));
    }

}