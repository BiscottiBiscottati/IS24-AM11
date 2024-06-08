package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.model.players.Player;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.PlateauMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlateauTest {
    Plateau plateau;

    @BeforeEach
    void setUp() {
        plateau = new Plateau(new GameListenerSupport());
        Plateau.setArmageddonTime(20);
    }


    @Test
    void isArmageddonTime() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        plateau.setStatus(GameStatus.ONGOING);

        // Add points to the player until they reach the armageddon time
        for (int i = 0; i < 20; i++) {
            plateau.addPlayerPoints(player.nickname(), 1);
        }

        Assertions.assertTrue(plateau.isArmageddonTime());

    }

    @Test
    void reset() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        plateau.addPlayerPoints(player.nickname(), 1);
        plateau.addCounterObjective(player.nickname());
        Assertions.assertEquals(1, plateau.getPlayerPoints(player.nickname()));
        Assertions.assertEquals(1, plateau.getCounterObjective(player.nickname()));
        plateau.reset();
        Assertions.assertEquals(0, plateau.getPlayerPoints(player.nickname()));
        Assertions.assertEquals(0, plateau.getCounterObjective(player.nickname()));


    }

    @Test
    void addPlayer() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        Assertions.assertEquals(0, plateau.getPlayerPoints(player.nickname()));
        Assertions.assertEquals(0, plateau.getCounterObjective(player.nickname()));

    }

    @Test
    void addPlayerPoints() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        plateau.addPlayerPoints(player.nickname(), 3);
        Assertions.assertEquals(3, plateau.getPlayerPoints(player.nickname()));


    }

    @Test
    void getPlayerPoints() throws IllegalPlateauActionException {

        Player player = new Player("Test Player", PlayerColor.BLUE);

        plateau.addPlayer(player.nickname());

        Assertions.assertEquals(0, plateau.getPlayerPoints(player.nickname()));

    }

    @Test
    void setFinalLeaderboard() throws IllegalPlateauActionException {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1.nickname());
        plateau.addPlayerPoints(player1.nickname(), 5);
        plateau.addCounterObjective(player1.nickname());
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2.nickname());
        plateau.addPlayerPoints(player2.nickname(), 5);
        plateau.addCounterObjective(player2.nickname());
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3.nickname());
        plateau.addPlayerPoints(player3.nickname(), 4);
        plateau.addCounterObjective(player3.nickname());
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4.nickname());
        plateau.addPlayerPoints(player4.nickname(), 3);
        plateau.addCounterObjective(player4.nickname());
        plateau.addCounterObjective(player4.nickname());
        plateau.addCounterObjective(player4.nickname());

        plateau.setFinalLeaderboard();

        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player1.nickname()));
        Assertions.assertEquals(1, plateau.getPlayerFinishingPosition(player2.nickname()));
        Assertions.assertEquals(2, plateau.getPlayerFinishingPosition(player3.nickname()));
        Assertions.assertEquals(3, plateau.getPlayerFinishingPosition(player4.nickname()));

    }

    @Test
    void getPlayerFinishingPosition() {
    }

    @Test
    void getWinners() throws IllegalPlateauActionException {

        Player player1 = new Player("Test Player1",
                                    PlayerColor.BLUE);
        plateau.addPlayer(player1.nickname());
        plateau.addPlayerPoints(player1.nickname(), 5);
        plateau.addCounterObjective(player1.nickname());
        Player player2 = new Player("Test Player2",
                                    PlayerColor.RED);
        plateau.addPlayer(player2.nickname());
        plateau.addPlayerPoints(player2.nickname(), 5);
        plateau.addCounterObjective(player2.nickname());
        Player player3 = new Player("Test Player3",
                                    PlayerColor.YELLOW);
        plateau.addPlayer(player3.nickname());
        plateau.addPlayerPoints(player3.nickname(), 4);
        plateau.addCounterObjective(player3.nickname());
        Player player4 = new Player("Test Player4",
                                    PlayerColor.GREEN);
        plateau.addPlayer(player4.nickname());
        plateau.addPlayerPoints(player4.nickname(), 3);
        plateau.addCounterObjective(player4.nickname());

        plateau.setFinalLeaderboard();
        plateau.getWinners();

        Assertions.assertEquals(2, plateau.getWinners().size());
        Assertions.assertTrue(plateau.getWinners().contains(player1.nickname()));
        Assertions.assertTrue(plateau.getWinners().contains(player2.nickname()));


    }

    @Test
    void activateArmageddon() {
        plateau.activateArmageddon();
        Assertions.assertTrue(plateau.isArmageddonTime());

        plateau.setStatus(GameStatus.CHOOSING_STARTERS);
        Assertions.assertFalse(plateau.isArmageddonTime());
    }

    @Test
    void getStatus() {

        plateau.reset();
        GameStatus status = plateau.getStatus();
        Assertions.assertEquals(GameStatus.SETUP, status);

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
        plateau.addPlayer(player.nickname());
        Assertions.assertEquals(0, plateau.getCounterObjective(player.nickname()));
        plateau.addCounterObjective(player.nickname());
        plateau.addCounterObjective(player.nickname());
        Assertions.assertEquals(2, plateau.getCounterObjective(player.nickname()));
        plateau.addCounterObjective(player.nickname());
        Assertions.assertEquals(3, plateau.getCounterObjective(player.nickname()));

    }

    @Test
    void getCounterObjective() throws IllegalPlateauActionException {

        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        Assertions.assertEquals(0, plateau.getCounterObjective(player.nickname()));
    }


    @Test
    void removePlayer() {
        Player player = new Player("Test Player",
                                   PlayerColor.BLUE);
        plateau.addPlayer(player.nickname());
        plateau.removePlayer(player.nickname());
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getPlayerPoints(player.nickname()));
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getCounterObjective(player.nickname()));
        Assertions.assertThrows(IllegalPlateauActionException.class,
                                () -> plateau.getPlayerFinishingPosition(player.nickname()));
    }

    @Test
    void save() throws IllegalPlateauActionException {
        PlateauMemento memento = plateau.save();
        Assertions.assertEquals(0, memento.playerPoints().size());
        Assertions.assertEquals(0, memento.objCounter().size());
        Assertions.assertEquals(0, memento.leaderboard().size());
        Assertions.assertEquals(GameStatus.SETUP, memento.status());

        plateau.setStatus(GameStatus.ONGOING);
        Player testPlayer = new Player("Test Player", PlayerColor.BLUE);
        plateau.addPlayer(testPlayer.nickname());
        plateau.addPlayerPoints(testPlayer.nickname(), 5);
        plateau.addCounterObjective(testPlayer.nickname());

        memento = plateau.save();

        Assertions.assertEquals(1, memento.playerPoints().size());
        Assertions.assertEquals(5, memento.playerPoints().get("Test Player"));
        Assertions.assertEquals(1, memento.objCounter().size());
        Assertions.assertEquals(0, memento.leaderboard().size());
        Assertions.assertEquals(GameStatus.ONGOING, memento.status());
    }

    @Test
    void load() throws IllegalPlateauActionException {
        plateau.setStatus(GameStatus.ONGOING);
        Player testPlayer = new Player("Test Player", PlayerColor.BLUE);
        plateau.addPlayer(testPlayer.nickname());
        plateau.addPlayerPoints(testPlayer.nickname(), 5);
        plateau.addCounterObjective(testPlayer.nickname());

        PlateauMemento memento = plateau.save();

        plateau.reset();

        plateau.load(memento);

        Assertions.assertEquals(5, plateau.getPlayerPoints(testPlayer.nickname()));
        Assertions.assertEquals(1, plateau.getCounterObjective(testPlayer.nickname()));
        Assertions.assertEquals(GameStatus.ONGOING, plateau.getStatus());
    }
}