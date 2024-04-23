package it.polimi.ingsw.am11.model.cards.objective.positioning;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SuppressWarnings("ClassWithTooManyFields")
@ExtendWith(MockitoExtension.class)
class LCardTest {

    @InjectMocks
    static ObjectiveCard lCardStandard;
    @InjectMocks
    static ObjectiveCard lCardRotated;
    @InjectMocks
    static ObjectiveCard lCardFlipped;
    static LCard lCard;
    static StarterCard starterCard;
    static ResourceCard redCard;
    static ResourceCard blueCard;
    static ResourceCard purpleCard;
    @Mock
    PlayerField playerField;

    Map<Position, CardContainer> field;

    Map<Position, CardContainer> largeField;

    Map<Position, CardContainer> noField;

    @BeforeAll
    static void beforeAll() {
        try {
            lCardStandard = new LCard.Builder(1, 2)
                    .hasPrimaryColor(Color.RED)
                    .hasSecondaryColor(Color.BLUE)
                    .isFlipped(false)
                    .isRotated(false)
                    .build();
            lCardRotated = new LCard.Builder(2, 2)
                    .hasPrimaryColor(Color.RED)
                    .hasSecondaryColor(Color.BLUE)
                    .isRotated(true)
                    .isFlipped(false)
                    .build();
            lCardFlipped = new LCard.Builder(3, 2)
                    .hasPrimaryColor(Color.RED)
                    .hasSecondaryColor(Color.BLUE)
                    .isFlipped(true)
                    .isRotated(false)
                    .build();

            lCard = (LCard) lCardStandard;

            StarterCard.Builder builder = new StarterCard.Builder(1);
            Arrays.stream(Corner.values()).forEach(
                    corner -> builder.hasColorRetroIn(corner, Color.PURPLE));
            starterCard = builder.build();
            redCard = new ResourceCard.Builder(1, 0, Color.RED).build();
            blueCard = new ResourceCard.Builder(2, 0, Color.BLUE).build();
            purpleCard = new ResourceCard.Builder(3, 0, Color.PURPLE).build();

        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        field = Map.of(
                new Position(0, 0), new CardContainer(starterCard),
                new Position(1, 1), new CardContainer(redCard),
                new Position(1, - 1), new CardContainer(redCard),
                new Position(0, 2), new CardContainer(blueCard),
                new Position(2, - 2), new CardContainer(blueCard),
                new Position(0, - 2), new CardContainer(blueCard)
        );

        noField = Map.of(
                new Position(0, 0), new CardContainer(starterCard),
                new Position(1, 1), new CardContainer(purpleCard),
                new Position(1, - 1), new CardContainer(redCard),
                new Position(2, - 2), new CardContainer(blueCard)
        );

        largeField = new HashMap<>(16);
        largeField.put(Position.of(0, 0), new CardContainer(starterCard));

        List<Position> redPositions = List.of(
                Position.of(1, 5),
                Position.of(1, 3),
                Position.of(1, 1),
                Position.of(1, - 1),
                Position.of(- 1, 1),
                Position.of(- 1, - 1)
        );
        redPositions.forEach(position -> largeField.put(position, new CardContainer(redCard)));

        List<Position> bluesPositions = List.of(
                Position.of(2, 2),
                Position.of(2, 0),
                Position.of(2, - 2),
                Position.of(0, - 2),
                Position.of(0, 2),
                Position.of(0, 4)
        );
        bluesPositions.forEach(position -> largeField.put(position, new CardContainer(blueCard)));

    }

    @Test
    void getType() {
        assertEquals(ObjectiveCardType.L_SHAPE, lCardStandard.getType());
    }

    @Test
    void countPoints() {

        // Test for a simple field
        Mockito.when(playerField.getCardsPositioned()).thenReturn(field);
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(2);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(1);
        assertEquals(2, lCardStandard.countPoints(playerField));
        assertEquals(2, lCardRotated.countPoints(playerField));
        assertEquals(2, lCardFlipped.countPoints(playerField));

        // Test on a larger field
        Mockito.when(playerField.getCardsPositioned()).thenReturn(largeField);
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(6);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(3);
        assertEquals(6, lCardStandard.countPoints(playerField));
        assertEquals(2, lCardRotated.countPoints(playerField));
        assertEquals(4, lCardFlipped.countPoints(playerField));

        // Test on a field with no matching cards
        Mockito.when(playerField.getCardsPositioned()).thenReturn(noField);
        assertEquals(0, lCardStandard.countPoints(playerField));
        assertEquals(0, lCardRotated.countPoints(playerField));

        // Test on not enough cards of primary color
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(1);
        assertEquals(0, lCardStandard.countPoints(playerField));
        assertEquals(0, lCardRotated.countPoints(playerField));

        // Test on not enough cards of secondary color
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(3);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(0);
        assertEquals(0, lCardStandard.countPoints(playerField));
        assertEquals(0, lCardRotated.countPoints(playerField));
    }

    @Test
    void isFlipped() {
        assertFalse(lCard.isFlipped());
    }

    @Test
    void isRotated() {
        assertFalse(lCard.isRotated());
    }
}