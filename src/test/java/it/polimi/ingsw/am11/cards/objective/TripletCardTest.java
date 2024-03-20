package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
class TripletCardTest {

    static CardPattern standardTripletMatrix = CardPattern.of(
            // the matrix is flipped on its y-axis for our cartesian reference
            List.of(
                    Arrays.asList(Color.GREEN, null, null),
                    Arrays.asList(null, Color.GREEN, null),
                    Arrays.asList(null, null, Color.GREEN)
            )
    );
    @InjectMocks
    static TripletCard tripletCard;
    static ResourceCard testFieldCardRes;
    static StarterCard testFieldCardStart;
    @Mock
    PlayerField playerField;
    Map<Position, CardContainer> positionedCardTest;


    @BeforeAll
    static void beforeAll() {
        try {
            tripletCard = new TripletCard.Builder(2)
                    .hasColor(Color.GREEN)
                    .isFlipped(true)
                    .build();
            StarterCard.Builder builder = new StarterCard.Builder();
            Arrays.stream(Corner.values()).forEach(corner -> builder.hasColorBackIn(corner, Color.PURPLE));
            testFieldCardStart = builder.build();
            testFieldCardRes = new ResourceCard.Builder(0, Color.GREEN).build();
        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        positionedCardTest = Map.of(
                Position.of(0, 0), new CardContainer(testFieldCardStart),
                Position.of(1, 1), new CardContainer(testFieldCardRes),
                Position.of(2, 0), new CardContainer(testFieldCardRes),
                Position.of(3, 1), new CardContainer(testFieldCardRes),
                Position.of(1, -1), new CardContainer(testFieldCardRes),
                Position.of(-1, -1), new CardContainer(testFieldCardRes),
                Position.of(0, -2), new CardContainer(testFieldCardRes),
                Position.of(-1, -3), new CardContainer(testFieldCardRes),
                Position.of(-2, -4), new CardContainer(testFieldCardRes)
        );
    }

    @Test
    void getType() {
        Assertions.assertSame(ObjectiveCardType.TRIPLET, tripletCard.getType());
    }

    @Test
    void countPoints() {
        Mockito.when(playerField.getCardsPositioned())
               .thenReturn(positionedCardTest);
        EnumMap<Color, Integer> placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 7);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);

        Assertions.assertEquals(4, tripletCard.countPoints(playerField));
        placedCardNumber = EnumMapUtils.Init(Color.class, 0);
        placedCardNumber.put(Color.GREEN, 2);
        Mockito.when(playerField.getPlacedCardColours())
               .thenReturn(placedCardNumber);

        Assertions.assertEquals(0, tripletCard.countPoints(playerField));
    }

    @Test
    void isFlipped() {
        Assertions.assertTrue(tripletCard.isFlipped());
    }

    @Test
    void getPattern() {
        IntStream.range(0, 3)
                 .forEach(value ->
                                  Assertions.assertArrayEquals(
                                          standardTripletMatrix.pattern()[value],
                                          tripletCard.getPattern().pattern()[value]
                                  )
                 );
    }
}