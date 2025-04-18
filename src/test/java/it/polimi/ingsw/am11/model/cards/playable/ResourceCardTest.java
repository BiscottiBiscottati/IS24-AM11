package it.polimi.ingsw.am11.model.cards.playable;

import it.polimi.ingsw.am11.model.cards.utils.enums.*;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class ResourceCardTest {

    private static PlayableCard playable;
    private static ResourceCard resource;
    private static PlayableCard playable2;


    @BeforeAll
    static void setUp() {
        try {
            playable = new ResourceCard.Builder(1, 1, GameColor.RED)
                    .hasIn(Corner.TOP_LX, GameColor.RED)
                    .hasIn(Corner.TOP_RX, GameColor.RED)
                    .build();

            resource = new ResourceCard.Builder(2, 1, GameColor.RED)
                    .hasIn(Corner.TOP_LX, GameColor.RED)
                    .hasIn(Corner.TOP_RX, GameColor.RED)
                    .build();

            playable2 = new ResourceCard.Builder(3, 0, GameColor.GREEN)
                    .hasIn(Corner.DOWN_LX, GameColor.PURPLE)
                    .build();
        } catch (IllegalCardBuildException e) {
            fail(e);
        }
    }


    @Test
    void getType() {
        assertSame(playable.getType(), PlayableCardType.RESOURCE);
        assertSame(resource.getType(), PlayableCardType.RESOURCE);
    }

    @Test
    void isCornerAvail() {
        for (Corner corner : Corner.values()) {
            switch (corner) {
                case TOP_LX -> {
                    assertTrue(playable.isFrontAvailable(corner));
                    assertTrue(resource.isFrontAvailable(corner));
                }
                case TOP_RX -> {
                    assertTrue(playable.isFrontAvailable(corner));
                    assertTrue(playable.isFrontAvailable(corner));
                }
                default -> assertFalse(playable.isFrontAvailable(corner));
            }
        }
    }

    @Test
    void getPlacingRequirements() {
        for (GameColor color : GameColor.values()) {
            assertEquals(playable.getPlacingRequirements().get(color), 0);
            assertEquals(resource.getPlacingRequirements().get(color), 0);
        }
    }

    @Test
    void getPointsRequirements() {
        assertSame(playable.getPointsRequirements(), PointsRequirementsType.CLASSIC);
        assertSame(resource.getPointsRequirements(), PointsRequirementsType.CLASSIC);
        assertSame(playable2.getPointsRequirements(), PointsRequirementsType.CLASSIC);
    }

    @Test
    void getItemCorner() {
        for (Corner corner : Corner.values()) {
            switch (corner) {
                case TOP_LX, TOP_RX -> {
                    assertSame(playable.getItemCorner(corner), GameColor.RED);
                    assertSame(resource.getItemCorner(corner), GameColor.RED);
                }
                default -> {
                    assertSame(Availability.NOT_USABLE, playable.getItemCorner(corner));
                    assertSame(Availability.NOT_USABLE, resource.getItemCorner(corner));
                }
            }
            if (corner == Corner.DOWN_LX) {
                assertSame(playable2.getItemCorner(corner), GameColor.PURPLE);
            } else {
                assertSame(Availability.NOT_USABLE, playable2.getItemCorner(corner));

            }
        }
    }

    @Test
    void getSymbolToCollect() {
        assertTrue(playable.getSymbolToCollect().isEmpty());
        assertTrue(resource.getSymbolToCollect().isEmpty());
    }

    @Test
    void getColor() {
        assertSame(playable.getColor(), GameColor.RED);
        assertSame(resource.getColor(), GameColor.RED);
        assertSame(playable2.getColor(), GameColor.GREEN);
    }

    @Test
    void getPoints() {
        assertEquals(playable.getPoints(), 1);
        assertEquals(resource.getPoints(), 1);
        assertEquals(playable2.getPoints(), 0);
    }

    @Disabled("Disabled because it depends on IDE settings")
    @Test
    void checkNulls() {
        assertThrows(RuntimeException.class, () -> playable.getItemCorner(null));
        assertThrows(RuntimeException.class, () -> playable.isFrontAvailable(null));
        assertThrows(RuntimeException.class, () -> resource.getItemCorner(null));
        assertThrows(RuntimeException.class, () -> resource.isFrontAvailable(null));
    }

    @Disabled("Disabled because it depends on IDE settings")
    @Test
    void checkBuilderNulls() {
        assertThrows(RuntimeException.class,
                     () -> new ResourceCard.Builder(5, 10, null));
        assertThrows(RuntimeException.class,
                     () -> new ResourceCard.Builder(4, 10, GameColor.RED)
                             .hasIn(null, GameColor.RED)
        );
    }
}