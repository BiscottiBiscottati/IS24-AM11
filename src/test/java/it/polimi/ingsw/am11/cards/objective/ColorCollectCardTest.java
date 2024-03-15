package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ColorCollectCardTest {

    private ColorCollectCard card;
    private ObjectiveCard card2;
    private ColorCollectCard card3;

    @BeforeEach
    void setUp() {
        try {
            card = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .hasColor(Color.BLUE)
                    .build();

            card2 = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE, 3)
                    .build();

            card3 = new ColorCollectCard.Builder(2)
                    .hasColor(Color.BLUE, 3)
                    .hasColor(Color.BLUE)
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
}