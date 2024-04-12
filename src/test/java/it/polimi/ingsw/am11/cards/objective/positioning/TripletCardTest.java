package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.players.field.PlayerField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripletCardTest {
    @InjectMocks
    static ObjectiveCard tripletCardForGreens;
    @InjectMocks
    static ObjectiveCard tripletCardRevForGreens;
    static PlayableCard greenCard;
    static PlayableCard blueCard;
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
            Arrays.stream(Corner.values()).forEach(corner -> builder.hasColorRetroIn(corner, Color.PURPLE));
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
        assertSame(ObjectiveCardType.TRIPLET, tripletCardForGreens.getType());
    }

    @Test
    void countPoints() {
        // Test for a simple map.
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardSmall);
        EnumMap<Color, Integer> placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 7);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);

        assertEquals(4, tripletCardForGreens.countPoints(playerField));
        assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test if color placed are less than 3.
        placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 2);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);

        assertEquals(0, tripletCardForGreens.countPoints(playerField));
        assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test a simple map but there are no matching pattern
        placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 6);
        placedCardNumber.put(Color.BLUE, 2);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardNoMatch);

        assertEquals(0, tripletCardForGreens.countPoints(playerField));
        assertEquals(0, tripletCardRevForGreens.countPoints(playerField));

        // Test for a larger map.
        placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 13);
        placedCardNumber.put(Color.BLUE, 5);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardLarge);

        assertEquals(4, tripletCardForGreens.countPoints(playerField));
        assertEquals(4, tripletCardRevForGreens.countPoints(playerField));

        // Test for an even larger map.
        placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 22);
        placedCardNumber.put(Color.BLUE, 8);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(posCardLargest);

        assertEquals(6, tripletCardForGreens.countPoints(playerField));
        assertEquals(6, tripletCardRevForGreens.countPoints(playerField));
    }

    @Test
    void isFlipped() {

        // internal check
        TripletCard tempCard = (TripletCard) tripletCardForGreens;
        assertTrue(tempCard.isFlipped());

        tempCard = (TripletCard) tripletCardRevForGreens;
        assertFalse(tempCard.isFlipped());
    }


}