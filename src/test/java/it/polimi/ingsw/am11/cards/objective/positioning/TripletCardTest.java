package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CardPattern;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerField;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.Assertions;
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

@SuppressWarnings("ClassWithTooManyFields")
@ExtendWith(MockitoExtension.class)
class TripletCardTest {

    static CardPattern standardTripletMatrix = new CardPattern(
            List.of(
                    Arrays.asList(null, null, Color.GREEN),
                    Arrays.asList(null, Color.GREEN, null),
                    Arrays.asList(Color.GREEN, null, null)
            )
    );
    static CardPattern reversedTripletMatrix = new CardPattern(
            // the matrix is flipped on its y-axis for the cartesian reference
            List.of(
                    Arrays.asList(Color.GREEN, null, null),
                    Arrays.asList(null, Color.GREEN, null),
                    Arrays.asList(null, null, Color.GREEN)
            )
    );
    @InjectMocks
    static TripletCard tripletCardForGreens;
    @InjectMocks
    static TripletCard tripletCardRevForGreens;
    static ResourceCard greenCard;
    static ResourceCard blueCard;
    static StarterCard starterCard;
    @Mock
    PlayerField playerField;
    Map<Position, CardContainer> posCardSmall;
    Map<Position, CardContainer> posCardLargest;

    Map<Position, CardContainer> posCardNoMatch;

    Map<Position, CardContainer> posCardLarge;


    @BeforeAll
    static void beforeAll() {
        try {
            tripletCardForGreens = new TripletCard.Builder(4, 2)
                    .hasColor(Color.GREEN)
                    .isFlipped(true)
                    .build();
            tripletCardRevForGreens = new TripletCard.Builder(3, 2)
                    .hasColor(Color.GREEN)
                    .isFlipped(false)
                    .build();
            StarterCard.Builder builder = new StarterCard.Builder(1);
            Arrays.stream(Corner.values()).forEach(corner -> builder.hasColorBackIn(corner, Color.PURPLE));
            starterCard = builder.build();
            greenCard = new ResourceCard.Builder(1, 0, Color.GREEN).build();
            blueCard = new ResourceCard.Builder(2, 0, Color.BLUE).build();
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        posCardSmall = Map.of(
                Position.of(0, 0), new CardContainer(starterCard),
                Position.of(1, 1), new CardContainer(greenCard),
                Position.of(2, 0), new CardContainer(greenCard),
                Position.of(3, 1), new CardContainer(greenCard),
                Position.of(1, -1), new CardContainer(greenCard),
                Position.of(-1, -1), new CardContainer(greenCard),
                Position.of(0, -2), new CardContainer(greenCard),
                Position.of(-1, -3), new CardContainer(greenCard),
                Position.of(-2, -4), new CardContainer(greenCard)
        );
        posCardLargest = new HashMap<>(64);
        posCardLargest.put(Position.of(0, 0), new CardContainer(starterCard));
        List<Position> testCardPositions = List.of(
                Position.of(1, 1),
                Position.of(2, 2),
                Position.of(1, 3),
                Position.of(3, 1),
                Position.of(4, 0),
                Position.of(2, 0),
                Position.of(5, -1),
                Position.of(1, -1),
                Position.of(3, -3),
                Position.of(2, -4),
                Position.of(1, -3),
                Position.of(-2, 2),
                Position.of(-3, 1),
                Position.of(-4, 0),
                Position.of(-3, -1),
                Position.of(-4, -2),
                Position.of(-5, -3),
                Position.of(-4, -4),
                Position.of(-3, -5),
                Position.of(0, -4),
                Position.of(-1, -1),
                Position.of(-2, -2)
        );
        testCardPositions.forEach(
                position -> posCardLargest.put(position, new CardContainer(greenCard))
        );
        List<Position> testCardPositionsFalse = List.of(
                Position.of(0, 2),
                Position.of(2, -2),
                Position.of(4, -2),
                Position.of(-1, 3),
                Position.of(-1, -5),
                Position.of(-2, -4),
                Position.of(-4, 2),
                Position.of(-3, 3)
        );
        testCardPositionsFalse.forEach(
                position -> posCardLargest.put(position, new CardContainer(blueCard))
        );

        posCardNoMatch = Map.of(
                Position.of(0, 0), new CardContainer(starterCard),
                Position.of(1, 1), new CardContainer(greenCard),
                Position.of(2, 0), new CardContainer(blueCard),
                Position.of(3, 1), new CardContainer(greenCard),
                Position.of(1, -1), new CardContainer(greenCard),
                Position.of(-1, -1), new CardContainer(greenCard),
                Position.of(0, -2), new CardContainer(blueCard),
                Position.of(-1, -3), new CardContainer(greenCard),
                Position.of(-2, -4), new CardContainer(greenCard)
        );

        posCardLarge = new HashMap<>(32);
        posCardLarge.put(Position.of(0, 0), new CardContainer(starterCard));
        testCardPositions = List.of(
                Position.of(1, 1),
                Position.of(2, 2),
                Position.of(1, 3),
                Position.of(-2, 2),
                Position.of(-3, 1),
                Position.of(-4, 0),
                Position.of(-1, -1),
                Position.of(-2, -2),
                Position.of(-3, -1),
                Position.of(-4, -2),
                Position.of(-3, -5),
                Position.of(-4, -4),
                Position.of(-5, -3)
        );
        testCardPositions.forEach(
                position -> posCardLarge.put(position, new CardContainer(greenCard))
        );
        testCardPositionsFalse = List.of(
                Position.of(0, 2),
                Position.of(-1, 3),
                Position.of(-3, 3),
                Position.of(-4, 2),
                Position.of(-2, -4)
        );
        testCardPositionsFalse.forEach(
                position -> posCardLarge.put(position, new CardContainer(blueCard))
        );

    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.TRIPLET, tripletCardForGreens.getType());
    }

    @Test
    void countPoints() {
        // Test for a simple map.
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardSmall);
        Mockito.when(playerField.getNumberOf(Color.GREEN))
               .thenReturn(7);

        Assertions.assertEquals(4, tripletCardForGreens.countPoints(playerField));
        Assertions.assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test if color placed are less than 3.
        Mockito.when(playerField.getNumberOf(Color.GREEN))
               .thenReturn(2);

        Assertions.assertEquals(0, tripletCardForGreens.countPoints(playerField));
        Assertions.assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test a simple map but there are no matching pattern
        Mockito.when(playerField.getNumberOf(Color.GREEN))
               .thenReturn(6);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardNoMatch);

        Assertions.assertEquals(0, tripletCardForGreens.countPoints(playerField));
        Assertions.assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test for a larger map.
        Mockito.when(playerField.getNumberOf(Color.GREEN))
               .thenReturn(13);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardLarge);

        Assertions.assertEquals(4, tripletCardForGreens.countPoints(playerField));
        Assertions.assertEquals(4, tripletCardRevForGreens.countPoints(playerField));

        // Test for an even larger map.
        Mockito.when(playerField.getNumberOf(Color.GREEN))
               .thenReturn(22);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardLargest);

        Assertions.assertEquals(6, tripletCardForGreens.countPoints(playerField));
        Assertions.assertEquals(6, tripletCardRevForGreens.countPoints(playerField));
    }

    @Test
    void isFlipped() {
        Assertions.assertTrue(tripletCardForGreens.isFlipped());
        Assertions.assertFalse(tripletCardRevForGreens.isFlipped());
    }

    @Test
    void getPattern() {

        assert tripletCardForGreens.getPattern() != null;
        Assertions.assertEquals(
                reversedTripletMatrix.pattern(),
                tripletCardForGreens.getPattern().pattern()
        );


        assert tripletCardRevForGreens.getPattern() != null;
        Assertions.assertEquals(
                standardTripletMatrix.pattern(),
                tripletCardRevForGreens.getPattern().pattern()
        );
    }

}