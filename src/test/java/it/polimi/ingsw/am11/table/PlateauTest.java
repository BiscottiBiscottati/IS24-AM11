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

        // Add points to the player, so they exceed the armageddon time
        plateau.addPlayerPoints(player, 1);

        // Assert that isArmageddonTime still returns true
        Assertions.assertTrue(plateau.isArmageddonTime());

        // Reset the plateau
        plateau.reset();

        // Assert that isArmageddonTime now returns false
        Assertions.assertFalse(plateau.isArmageddonTime());
    }

    @Test
    void reset() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        plateau.addPlayerPoints(player, 1);
        Assertions.assertEquals(1, plateau.getPlayerPoints(player));
        plateau.reset();
        Assertions.assertFalse(plateau.isArmageddonTime());
        Assertions.assertEquals(0, plateau.getPlayerPoints(player));


    }

    @Test
    void addPlayer() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        Assertions.assertEquals(0, plateau.getPlayerPoints(player));

    }

    @Test
    void addPlayerPoints() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        plateau.addPlayerPoints(player, 3);
        Assertions.assertEquals(3, plateau.getPlayerPoints(player));


    }

    @Test
    void getPlayerPoints() {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        // Directly put null as the player's points
        plateau.playerPoints.put(player, null);

        // Assert that getPlayerPoints throws an IllegalPlateauActionException
        Assertions.assertThrows(IllegalPlateauActionException.class, () -> plateau.getPlayerPoints(player));
    }

    @Test
    void setFinalLeaderboard() {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1);
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2);
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3);
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4);

        plateau.playerPoints.put(player1, 5);
        plateau.playerPoints.put(player2, 5);
        plateau.playerPoints.put(player3, 3);
        plateau.playerPoints.put(player4, 2);

        plateau.setFinalLeaderboard(plateau.playerPoints);

        Assertions.assertEquals(1, plateau.finalLeaderboard.get(player2));

    }

    @Test
    void getPlayerFinishingPosition() {
    }

    @Test
    void getWinners() {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1);
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2);
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3);
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4);

        plateau.playerPoints.put(player1, 5);
        plateau.playerPoints.put(player2, 5);
        plateau.playerPoints.put(player3, 3);
        plateau.playerPoints.put(player4, 2);

        plateau.setFinalLeaderboard(plateau.playerPoints);

        Assertions.assertEquals(1, plateau.getPlayerFinihingPosition(player1));
        Assertions.assertEquals(1, plateau.getPlayerFinihingPosition(player2));
        Assertions.assertEquals(2, plateau.getPlayerFinihingPosition(player3));
        Assertions.assertEquals(3, plateau.getPlayerFinihingPosition(player4));

        Assertions.assertEquals(2, plateau.getWinners().size());
        Assertions.assertEquals(player1, plateau.getWinners().get(0));
        Assertions.assertEquals(player2, plateau.getWinners().get(1));
    }

    @Test
    void activateArmageddon() {
        plateau.activateArmageddon();
        Assertions.assertTrue(plateau.isArmageddonTime());
    }
}