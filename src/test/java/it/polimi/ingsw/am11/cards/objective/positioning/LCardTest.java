package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
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
    }

    @Test
    void getType() {
    }

    @Test
    void countPoints() {
        Mockito.when(playerField.getCardsPositioned()).thenReturn(field);
        Mockito.when(playerField.getNumberOf(Color.RED)).thenReturn(2);
        Mockito.when(playerField.getNumberOf(Color.BLUE)).thenReturn(1);

        Assertions.assertEquals(2, lCardStandard.countPoints(playerField));
    }
}