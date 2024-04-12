package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlateauTest {
    Plateau plateau;

    @BeforeEach
    void setUp() {
        plateau = new Plateau(20);
    }

    @Test
    void isArmageddonTime() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);

        // Add points to the player until they reach the armageddon time
        for (int i = 0; i < 20; i++) {
            plateau.addPlayerPoints(player, 1);

        }

        Assertions.assertTrue(plateau.isArmageddonTime());

        // Add points to the player so they exceed the armageddon time
        plateau.addPlayerPoints(player, 1);

        // Assert that isArmageddonTime still returns true
        Assertions.assertTrue(plateau.isArmageddonTime());

        // Reset the plateau
        plateau.reset();

        // Assert that isArmageddonTime now returns false
        Assertions.assertFalse(plateau.isArmageddonTime());
    }

    @Test
    void reset() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void addPlayerPoints() {
    }

    @Test
    void getPlayerPoints() {
    }

    @Test
    void setFinalLeaderboard() {
    }

    @Test
    void getPlayerFinihingPosition() {
    }

    @Test
    void getWinners() {
    }

    @Test
    void activateArmageddon() {
    }
}