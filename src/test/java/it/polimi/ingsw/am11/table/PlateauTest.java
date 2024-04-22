package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.utils.PlayerColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlateauTest {
    Plateau plateau;

    @BeforeEach
    void setUp() {
        plateau = new Plateau();
    }


    @Test
    void isArmageddonTime() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        plateau.setStatus(GameStatus.ONGOING);

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
        plateau.addCounterObjective(player);
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
        plateau.addCounterObjective(player1);
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2);
        plateau.addPlayerPoints(player2, 5);
        plateau.addCounterObjective(player2);
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3);
        plateau.addPlayerPoints(player3, 4);
        plateau.addCounterObjective(player3);
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4);
        plateau.addPlayerPoints(player4, 3);
        plateau.addCounterObjective(player4);
        plateau.addCounterObjective(player4);
        plateau.addCounterObjective(player4);

        plateau.setFinalLeaderboard();

        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player1));
        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player2));
        Assertions.assertEquals(2, plateau.getPlayerFinishingPosition(player3));
        Assertions.assertEquals(3, plateau.getPlayerFinishingPosition(player4));

    }

    @Test
    void getPlayerFinishingPosition() {
    }

    @Test
    void getWinners() throws IllegalPlateauActionException {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1);
        plateau.addPlayerPoints(player1, 5);
        plateau.addCounterObjective(player1);
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2);
        plateau.addPlayerPoints(player2, 5);
        plateau.addCounterObjective(player2);
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3);
        plateau.addPlayerPoints(player3, 4);
        plateau.addCounterObjective(player3);
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4);
        plateau.addPlayerPoints(player4, 3);
        plateau.addCounterObjective(player4);

        plateau.setFinalLeaderboard();
        plateau.getWinners();

        Assertions.assertEquals(2, plateau.getWinners().size());
        Assertions.assertTrue(plateau.getWinners().contains(player1));
        Assertions.assertTrue(plateau.getWinners().contains(player2));


    }

    @Test
    void activateArmageddon() {
        plateau.activateArmageddon();
        Assertions.assertTrue(plateau.isArmageddonTime());

        plateau.reset();
        Assertions.assertFalse(plateau.isArmageddonTime());
    }

    @Test
    void getStatus() {

        plateau.reset();
        GameStatus status = plateau.getStatus();
        Assertions.assertEquals(GameStatus.STARTING, status);

        plateau.activateArmageddon();
        status = plateau.getStatus();
        Assertions.assertEquals(GameStatus.ARMAGEDDON, status);

    }

    @Test
    void setStatus() {
        plateau.setStatus(GameStatus.ARMAGEDDON);
        Assertions.assertEquals(GameStatus.ARMAGEDDON, plateau.getStatus());

        plateau.setStatus(GameStatus.ONGOING);
        Assertions.assertEquals(GameStatus.ONGOING, plateau.getStatus());


    }

    @Test
    void addCounterObjective() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        Assertions.assertEquals(0, plateau.getCounterObjective(player));
        plateau.addCounterObjective(player);
        plateau.addCounterObjective(player);
        Assertions.assertEquals(2, plateau.getCounterObjective(player));
        plateau.addCounterObjective(player);
        Assertions.assertEquals(3, plateau.getCounterObjective(player));

    }

    @Test
    void getCounterObjective() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        Assertions.assertEquals(0, plateau.getCounterObjective(player));
    }


    @Test
    void removePlayer() {
        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player);
        plateau.removePlayer(player);
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getPlayerPoints(player));
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getCounterObjective(player));
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getPlayerFinishingPosition(player));
    }
}