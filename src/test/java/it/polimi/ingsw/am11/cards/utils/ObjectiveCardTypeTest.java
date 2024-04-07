package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ObjectiveCardTypeTest {

    static Set<Position> standardTriplet = Set.of(
            Position.of(0, 2),
            Position.of(1, 1),
            Position.of(2, 0)
    );

    static Set<Position> flippedTriplet = Set.of(
            Position.of(0, 0),
            Position.of(1, 1),
            Position.of(2, 2)
    );

    static Set<Position> LShape = Set.of(
            Position.of(1, 3),
            Position.of(1, 1),
            Position.of(2, 0)
    );

    static Set<Position> flippedL = Set.of(
            Position.of(1, 3),
            Position.of(1, 1),
            Position.of(0, 0)
    );

    static Set<Position> rotatedL = Set.of(
            Position.of(1, -1),
            Position.of(1, 1),
            Position.of(0, 2)
    );

    static Set<Position> rotatedFlippedL = Set.of(
            Position.of(1, -1),
            Position.of(1, 1),
            Position.of(2, 2)
    );


    @Test
    void getPositions() {

        Assertions.assertEquals(
                standardTriplet,
                ObjectiveCardType.TRIPLET.getPositions(false, false).orElse(Set.of())
        );
        Assertions.assertEquals(
                standardTriplet,
                ObjectiveCardType.TRIPLET.getPositions(false, true).orElse(Set.of())
        );
        Assertions.assertEquals(
                flippedTriplet,
                ObjectiveCardType.TRIPLET.getPositions(true, false).orElse(Set.of())
        );
        Assertions.assertEquals(
                flippedTriplet,
                ObjectiveCardType.TRIPLET.getPositions(true, true).orElse(Set.of())
        );
        Assertions.assertEquals(
                LShape,
                ObjectiveCardType.L_SHAPE.getPositions(false, false).orElse(Set.of())
        );
        Assertions.assertEquals(
                flippedL,
                ObjectiveCardType.L_SHAPE.getPositions(true, false).orElse(Set.of())
        );
        Assertions.assertEquals(
                rotatedL,
                ObjectiveCardType.L_SHAPE.getPositions(false, true).orElse(Set.of())
        );
        Assertions.assertEquals(
                rotatedFlippedL,
                ObjectiveCardType.L_SHAPE.getPositions(true, true).orElse(Set.of())
        );

    }
}