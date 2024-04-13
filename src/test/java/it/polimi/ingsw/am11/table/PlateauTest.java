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

        plateau.reset();

        Assertions.assertFalse(plateau.isArmageddonTime());

    }

    @Test
    void reset() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        plateau.addPlayerPoints(player, 1);
        plateau.addCounterObjective(player, 1);
        Assertions.assertEquals(1, plateau.getPlayerPoints(player));
        Assertions.assertEquals(1, plateau.getCounterObjective(player));
        plateau.reset();
        Assertions.assertEquals(0, plateau.getPlayerPoints(player));
        Assertions.assertEquals(0, plateau.getCounterObjective(player));


    }

    @Test
    void addPlayer() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        Assertions.assertEquals(0, plateau.getPlayerPoints(player));
        Assertions.assertEquals(0, plateau.getCounterObjective(player));

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
    void getPlayerPoints() throws IllegalPlateauActionException {

        Player player = new Player("Test Player", PlayerColor.BLUE);

        plateau.addPlayer(player);

        Assertions.assertEquals(0, plateau.getPlayerPoints(player));

    }

    @Test
    void setFinalLeaderboard() throws IllegalPlateauActionException {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1);
        plateau.addPlayerPoints(player1, 5);
        plateau.addCounterObjective(player1, 2);
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2);
        plateau.addPlayerPoints(player2, 5);
        plateau.addCounterObjective(player2, 1);
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3);
        plateau.addPlayerPoints(player3, 4);
        plateau.addCounterObjective(player3, 1);
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4);
        plateau.addPlayerPoints(player4, 3);
        plateau.addCounterObjective(player4, 0);

        plateau.setFinalLeaderboard();

        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player1));
        Assertions.assertEquals(2, plateau.getPlayerFinishingPosition(player2));
        Assertions.assertEquals(3, plateau.getPlayerFinishingPosition(player3));
        Assertions.assertEquals(4, plateau.getPlayerFinishingPosition(player4));

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

        //FIXME should be a method to add points to a player to represent how it would be used


        plateau.setFinalLeaderboard();

        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player1));
        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player2));
        Assertions.assertEquals(2, plateau.getPlayerFinishingPosition(player3));
        Assertions.assertEquals(3, plateau.getPlayerFinishingPosition(player4));

        Assertions.assertEquals(2, plateau.getWinners().size());
        Assertions.assertEquals(player1, plateau.getWinners().get(0));
        Assertions.assertEquals(player2, plateau.getWinners().get(1));
    }

    @Test
    void activateArmageddon() {
        plateau.activateArmageddon();
    }

    @Test
    void getStatus() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void addCounterObjective() {
    }

    @Test
    void getCountObjective() {
    }
}