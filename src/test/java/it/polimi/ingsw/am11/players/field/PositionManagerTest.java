package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

class PositionManagerTest {

    Random gen;
    PositionManager positionManager;

    @BeforeEach
    void setUp() {
        gen = new Random();
        positionManager = new PositionManager();
    }

    @Test
    void getPositionIn() {
        Position position = Position.of(0, 0);
        Assertions.assertEquals(
                Position.of(1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_RX));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_LX));
        Assertions.assertEquals(
                Position.of(1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_RX));
        Assertions.assertEquals(
                Position.of(-1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_LX));

    }

    @Test
    void getMovementOfPositions() {
        Position position = Position.of(0, 0);
        Assertions.assertEquals(
                Position.of(1, 1),
                PositionManager.getMovementOfPositions(position, Collections.singletonList(Corner.TOP_RX)));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(0, 2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_RX, Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(2, -2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.DOWN_RX, Corner.DOWN_RX)));
    }

    @Test
    void getCornerFromPositions() {
        Position firstPos = Position.of(0, 0);
        Position secondPos = Position.of(-1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_LX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_RX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, -1);
        Assertions.assertEquals(Optional.of(Corner.DOWN_RX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(3, 3);
        secondPos = Position.of(2, 2);
        Assertions.assertEquals(Optional.of(Corner.DOWN_LX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 0);
        Assertions.assertEquals(Optional.empty(),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 1);
        Assertions.assertEquals(Optional.empty(),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        for (int i = 0; i < 100; i++) {
            secondPos = Position.of(gen.nextInt(2, 100), gen.nextInt(2, 100));
            Assertions.assertEquals(Optional.empty(),
                                    PositionManager.getCornerFromPositions(firstPos, secondPos));

            secondPos = Position.of(gen.nextInt(-100, -2), gen.nextInt(-100, -2));
            Assertions.assertEquals(Optional.empty(),
                                    PositionManager.getCornerFromPositions(firstPos, secondPos));
        }
    }

    @Test
    void reset() {
        positionManager.reset();
        Assertions.assertEquals(1, positionManager.getAvailablePositions().size());
        Assertions.assertEquals(0, positionManager.getCardsPositioned().size());
        Assertions.assertTrue(positionManager.isAvailable(Position.of(0, 0)));
    }
}