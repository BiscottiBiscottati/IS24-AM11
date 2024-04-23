package it.polimi.ingsw.am11.cards.objective.collecting;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ColorCollectCardTest {

    @InjectMocks
    private static ObjectiveCard card;
    private static ObjectiveCard card2;
    private static ColorCollectCard card3;
    @InjectMocks
    private static ObjectiveCard cardWithMixedColors;
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
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getSymbolRequirements() {
        assertTrue(card.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        assertTrue(card2.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
        assertTrue(card3.getSymbolRequirements().values().stream().allMatch(value -> value == 0));
    }

    @Test
    void getColorRequirements() {
        assertEquals(3, card.getColorRequirements().get(Color.BLUE));
        assertEquals(3, card2.getColorRequirements().get(Color.BLUE));
        assertEquals(4, card3.getColorRequirements().get(Color.BLUE));

        for (Color color : Arrays.stream(Color.values())
                                 .filter(key -> key != Color.BLUE)
                                 .toList()) {
            assertEquals(0, card.getColorRequirements().get(color));
            assertEquals(0, card2.getColorRequirements().get(color));
            assertEquals(0, card3.getColorRequirements().get(color));
        }
    }

    @Test
    void getType() {
        assertSame(ObjectiveCardType.COLOR_COLLECT, card.getType());
        assertSame(ObjectiveCardType.COLOR_COLLECT, card2.getType());
        assertSame(ObjectiveCardType.COLOR_COLLECT, card3.getType());
    }

    @Test
    void getPoints() {
        assertEquals(2, card.getPoints());
        assertEquals(2, card.getPoints());
        assertEquals(2, card.getPoints());
    }

    @Test
    void countPoints() {

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(4);
        assertEquals(2, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(8);
        assertEquals(4, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(12);
        assertEquals(8, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(0);
        assertEquals(0, card.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(- 1);
        assertEquals(0, card.countPoints(playerField));

        Arrays.stream(Color.values()).forEach(
                color -> Mockito.when(playerField.getNumberOf(color)).thenReturn(1)
        );
        assertEquals(2, cardWithMixedColors.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(30);
        assertEquals(2, cardWithMixedColors.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(- 1);
        assertEquals(0, cardWithMixedColors.countPoints(playerField));
    }

}