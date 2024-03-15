package it.polimi.ingsw.am11.cards;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.util.PlayableCardType;
import it.polimi.ingsw.am11.cards.util.PointsRequirementsType;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.util.Color;
import it.polimi.ingsw.am11.cards.util.Corner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceCardTest {

    private PlayableCard playable;
    private ResourceCard resource;
    private PlayableCard playable2;


    @BeforeEach
    void setUp() {

        playable = new ResourceCard.Builder(1, Color.RED)
                .hasItemIn(Corner.TOP_LX, Color.RED)
                .hasItemIn(Corner.TOP_RX, Color.RED)
                .build();

        resource = new ResourceCard.Builder(1, Color.RED)
                .hasItemIn(Corner.TOP_LX, Color.RED)
                .hasItemIn(Corner.TOP_RX, Color.RED)
                .build();

        playable2 = new ResourceCard.Builder(0, Color.GREEN)
                .hasItemIn(Corner.DOWN_LX, Color.PURPLE)
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
                default -> Assertions.assertFalse(playable.isCornerAvail(corner));
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
        Assertions.assertSame(playable2.getPointsRequirements(), PointsRequirementsType.CLASSIC);
    }

    @Test
    void checkItemCorner() {
        for (Corner corner : Corner.values()) {
            switch (corner) {
                case TOP_LX, TOP_RX -> {
                    Assertions.assertSame(playable.checkItemCorner(corner), Color.RED);
                    Assertions.assertSame(resource.checkItemCorner(corner), Color.RED);
                }
                default -> {
                    Assertions.assertFalse(playable.checkItemCorner(corner).isAvailable());
                    Assertions.assertFalse(resource.checkItemCorner(corner).isAvailable());
                }
            }
            if (corner == Corner.DOWN_LX) {
                Assertions.assertSame(playable2.checkItemCorner(corner), Color.PURPLE);
            } else {
                Assertions.assertFalse(playable2.checkItemCorner(corner).isAvailable());
            }
        }
    }

    @Test
    void getSymbolToCollect() {
        Assertions.assertTrue(playable.getSymbolToCollect().isEmpty());
        Assertions.assertTrue(resource.getSymbolToCollect().isEmpty());
    }

    @Test
    void getColor() {
        Assertions.assertSame(playable.getColor(), Color.RED);
        Assertions.assertSame(resource.getColor(), Color.RED);
        Assertions.assertSame(playable2.getColor(), Color.GREEN);
    }

    @Test
    void getPoints() {
        Assertions.assertEquals(playable.getPoints(), 1);
        Assertions.assertEquals(resource.getPoints(), 1);
        Assertions.assertEquals(playable2.getPoints(), 0);
    }

    @Test
    void checkNulls() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> playable.checkItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> playable.isCornerAvail(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> resource.checkItemCorner(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> resource.isCornerAvail(null));
    }

    @Test
    void checkBuilderNulls() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ResourceCard.Builder(10, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ResourceCard.Builder(10, Color.RED).hasItemIn(null, Color.RED));
    }
}