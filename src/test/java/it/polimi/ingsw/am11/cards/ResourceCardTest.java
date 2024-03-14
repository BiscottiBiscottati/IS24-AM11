package it.polimi.ingsw.am11.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ResourceCardTest {

    static private PlayableCard playable;
    static private ResourceCard resource;

    @BeforeAll
    static void setUp() {
        playable = new ResourceCard.Builder(1, Color.RED)
                .hasItemIn(Corner.TOP_LX, Color.RED)
                .hasItemIn(Corner.TOP_RX, Color.RED)
                .build();

        resource = new ResourceCard.Builder(1, Color.RED)
                .hasItemIn(Corner.TOP_LX, Color.RED)
                .hasItemIn(Corner.TOP_RX, Color.RED)
                .build();

    }


    @Test
    void getType() {
        Assertions.assertSame(playable.getType(), PlayableCardType.RESOURCE);
        Assertions.assertSame(resource.getType(), PlayableCardType.RESOURCE);
    }

    @Test
    void isCornerAvail() {
        for (Corner corner : Corner.values()) {
            switch (corner) {
                case TOP_LX -> {
                    Assertions.assertTrue(playable.isCornerAvail(corner));
                    Assertions.assertTrue(resource.isCornerAvail(corner));
                }
                case TOP_RX -> {
                    Assertions.assertTrue(playable.isCornerAvail(corner));
                    Assertions.assertTrue(playable.isCornerAvail(corner));
                }
                case null, default -> Assertions.assertFalse(playable.isCornerAvail(corner));
            }
        }
    }

    @Test
    void getPlacingRequirements() {
        for (Color color : Color.values()) {
            Assertions.assertEquals(playable.getPlacingRequirements().get(color), 0);
            Assertions.assertEquals(resource.getPlacingRequirements().get(color), 0);
        }
    }

    @Test
    void getPointsRequirements() {
        Assertions.assertSame(playable.getPointsRequirements(), PointsRequirementsType.CLASSIC);
        Assertions.assertSame(resource.getPointsRequirements(), PointsRequirementsType.CLASSIC);
    }

    @Test
    void checkItemCorner() {
        for (Corner corner : Corner.values()) {
            switch (corner) {
                case TOP_LX -> {
                    Assertions.assertSame(playable.checkItemCorner(corner), Color.RED);
                    Assertions.assertSame(resource.checkItemCorner(corner), Color.RED);
                }
                case TOP_RX -> {
                    Assertions.assertSame(playable.checkItemCorner(corner), Color.RED);
                    Assertions.assertSame(playable.checkItemCorner(corner), Color.RED);
                }
                case null, default -> Assertions.assertNotSame(playable.checkItemCorner(corner), Color.RED);
            }
        }
    }

    @Test
    void getSymbolToCollect() {
        Assertions.assertTrue(playable.getSymbolToCollect().isEmpty());
        Assertions.assertTrue(resource.getSymbolToCollect().isEmpty());
    }
}