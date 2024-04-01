package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class LCardTest {

    @InjectMocks
    static LCard lCardStandard;
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

            StarterCard.Builder builder = new StarterCard.Builder(1);
            Arrays.stream(Corner.values()).forEach(corner -> builder.hasColorBackIn(corner, Color.PURPLE));
            starterCard = builder.build();
            redCard = new ResourceCard.Builder(1, 0, Color.RED).build();
            blueCard = new ResourceCard.Builder(2, 0, Color.BLUE).build();
            purpleCard = new ResourceCard.Builder(3, 0, Color.PURPLE).build();

        } catch (IllegalBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        field = Map.of(
                new Position(0, 0), new CardContainer(starterCard),
                new Position(1, 1), new CardContainer(redCard),
                new Position(1, -1), new CardContainer(redCard),
                new Position(2, -2), new CardContainer(blueCard)
        );

        noField = Map.of(
                new Position(0, 0), new CardContainer(starterCard),
                new Position(1, 1), new CardContainer(purpleCard),
                new Position(1, -1), new CardContainer(redCard),
                new Position(2, -2), new CardContainer(blueCard)
        );

        largeField = new HashMap<>(16);
        largeField.put(Position.of(0, 0), new CardContainer(starterCard));

        List<Position> redsPositions = List.of(
                Position.of(1, 1),
                Position.of(1, -1),
                Position.of(-1, 1),
                Position.of(-1, -1),
                Position.of(1, 3),
                Position.of(1, 5)
        );
        redsPositions.forEach(position -> largeField.put(position, new CardContainer(redCard)));

        List<Position> bluesPositions = List.of(
                Position.of(0, -2),
                Position.of(2, -2),
                Position.of(2, 2)
        );
        bluesPositions.forEach(position -> largeField.put(position, new CardContainer(blueCard)));

    }

    @Test
    void getType() {
        Assertions.assertEquals(ObjectiveCardType.L_SHAPE, lCardStandard.getType());
    }

    @Test
    void countPoints() {
        Mockito.when(playerField.getCardsPositioned()).thenReturn(field);
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(2);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(1);
        Assertions.assertEquals(2, lCardStandard.countPoints(playerField));

        Mockito.when(playerField.getCardsPositioned()).thenReturn(largeField);
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(6);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(3);
        Assertions.assertEquals(6, lCardStandard.countPoints(playerField));

        Mockito.when(playerField.getCardsPositioned()).thenReturn(noField);
        Assertions.assertEquals(0, lCardStandard.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(1);
        Assertions.assertEquals(0, lCardStandard.countPoints(playerField));

        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(3);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(0);
        Assertions.assertEquals(0, lCardStandard.countPoints(playerField));
    }

    @Test
    void isFlipped() {
        Assertions.assertFalse(lCardStandard.isFlipped());
    }

    @Test
    void isRotated() {
        Assertions.assertFalse(lCardStandard.isRotated());
    }
}