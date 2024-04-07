package it.polimi.ingsw.am11.cards.playable;

import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class PlayableCardTest {

    @Test
    void isRetroAvailable() {
        Arrays.stream(Corner.values())
              .forEach(
                      corner -> Assertions.assertTrue(PlayableCard.isRetroAvailable(corner))
              );
    }
}