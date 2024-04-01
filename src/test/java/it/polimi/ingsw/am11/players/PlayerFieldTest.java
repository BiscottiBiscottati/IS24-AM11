package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class PlayerFieldTest {

    Position position;

    @BeforeEach
    void setUp() {
        position = Position.of(0, 0);
    }

    @Test
    void getPositionIn() {
        Assertions.assertEquals(
                Position.of(1, 1),
                PlayerField.getPositionIn(position, Corner.TOP_RX));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PlayerField.getPositionIn(position, Corner.TOP_LX));
        Assertions.assertEquals(
                Position.of(1, -1),
                PlayerField.getPositionIn(position, Corner.DOWN_RX));
        Assertions.assertEquals(
                Position.of(-1, -1),
                PlayerField.getPositionIn(position, Corner.DOWN_LX));

    }

    @Test
    void getMovementOfPositions() {
        Assertions.assertEquals(
                Position.of(1, 1),
                PlayerField.getMovementOfPositions(position, Collections.singletonList(Corner.TOP_RX)));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PlayerField.getMovementOfPositions(position, List.of(Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(0, 2),
                PlayerField.getMovementOfPositions(position, List.of(Corner.TOP_RX, Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(2, -2),
                PlayerField.getMovementOfPositions(position, List.of(Corner.DOWN_RX, Corner.DOWN_RX)));
    }
}