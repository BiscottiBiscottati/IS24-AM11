package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
                PlayerField.getPositionIn(Corner.TOP_RX, position));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PlayerField.getPositionIn(Corner.TOP_LX, position));
        Assertions.assertEquals(
                Position.of(1, -1),
                PlayerField.getPositionIn(Corner.DOWN_RX, position));
        Assertions.assertEquals(
                Position.of(-1, -1),
                PlayerField.getPositionIn(Corner.DOWN_LX, position));

    }
}