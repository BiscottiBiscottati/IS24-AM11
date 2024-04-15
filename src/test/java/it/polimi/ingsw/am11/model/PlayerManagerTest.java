package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagerTest {

    static RuleSet ruleSet;
    private static PlayerManager manager;

    @BeforeAll
    static void beforeAll() {
        ruleSet = new BasicRuleset();

    }

    @BeforeEach
    void setUp() {
        manager = new PlayerManager();
        PlayerManager.setMaxNumberOfPlayers(ruleSet.getMaxPlayers());
    }

    @Test
    void setMaxNumberOfPlayers() {
        int val = 4;
        PlayerManager.setMaxNumberOfPlayers(val);
        assertEquals(val, PlayerManager.getMaxNumberOfPlayer());
    }

    @Test
    void getMaxNumberOfPlayer() {
        int val = 4;
        PlayerManager.setMaxNumberOfPlayers(val);
        assertEquals(val, PlayerManager.getMaxNumberOfPlayer());
    }

    @Test
    void getPlayers() {
        Set<String> modelPlayers;
        //Case with no players
        modelPlayers = manager.getPlayers();
        assertTrue(modelPlayers.isEmpty());

        //Normal example
        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        modelPlayers = manager.getPlayers();

        assertEquals(3, modelPlayers.size());
        assertEquals(players, modelPlayers);
    }

    @Test
    void getNumberOfPlayers() {
        assertEquals(0, manager.getNumberOfPlayers());

        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, manager.getNumberOfPlayers());
        try {
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, manager.getNumberOfPlayers());
        try {
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, manager.getNumberOfPlayers());
        try {
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        assertEquals(4, manager.getNumberOfPlayers());
        manager.removePlayer("Giorgio");
        assertEquals(4, manager.getNumberOfPlayers());
        manager.removePlayer("player1");
        assertEquals(3, manager.getNumberOfPlayers());
        manager.removePlayer("player2");
        assertEquals(2, manager.getNumberOfPlayers());
        manager.removePlayer("player3");
        assertEquals(1, manager.getNumberOfPlayers());
        manager.removePlayer("player4");
        assertEquals(0, manager.getNumberOfPlayers());

    }

    @Test
    void getCurrentTurnPlayer() {
        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        manager.startingTheGame();

        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        assertEquals(manager.getFirstPlayer(), manager.getCurrentTurnPlayer());

        for (int i = 0; i < numOfPlayers; i++) {
            orderOfPlayers.add(i, manager.getCurrentTurnPlayer());
            manager.goNextTurn();
        }
        for (int j = 0; j < numOfPlayers; j++) {
            assertEquals(orderOfPlayers.get(j), manager.getCurrentTurnPlayer());
            manager.goNextTurn();
        }


    }

    @Test
    void getFirstPlayer() {

        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        manager.startingTheGame();

        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        assertEquals(manager.getFirstPlayer(), manager.getCurrentTurnPlayer());
    }

    @Test
    void getPlayer() {
        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        for (String nickname : players) {
            assertEquals(nickname, manager.getPlayer(nickname).nickname());
        }

        assertNull(manager.getPlayer("Giorgio"));
    }

    @Test
    void getHand() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        //TODO
    }

    @Test
    void getPlayerObjective() {
        //TODO
    }

    @Test
    void getPlayerColor() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        assertEquals(manager.getPlayerColor("player1"), PlayerColor.BLUE);
        assertEquals(manager.getPlayerColor("player2"), PlayerColor.GREEN);
        assertEquals(manager.getPlayerColor("player3"), PlayerColor.RED);
        assertEquals(manager.getPlayerColor("player4"), PlayerColor.YELLOW);
    }

    @Test
    void addPlayerToTable() {
        //reducing the limit to test exceptions
        PlayerManager.setMaxNumberOfPlayers(3);
        //adding 1 player
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        //adding a player with same name or color
        assertThrows(PlayerInitException.class,
                     () -> manager.addPlayerToTable("player1", PlayerColor.YELLOW));
        assertThrows(PlayerInitException.class,
                     () -> manager.addPlayerToTable("player2", PlayerColor.BLUE));

        //reaching the limit
        try {
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        //overcoming the limit
        assertThrows(PlayerInitException.class,
                     () -> manager.addPlayerToTable("player4", PlayerColor.YELLOW));


    }

    @Test
    void removePlayer() {
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        //removing a player
        manager.removePlayer("player1");
        assertNull(manager.getPlayer("player1"));
        Set<String> players = Set.of("player2", "player3", "player4");
        assertEquals(players, manager.getPlayers());

        //removing a fake player
        manager.removePlayer("Giorgio");
        assertEquals(players, manager.getPlayers());
    }

    @Test
    void isFirstTheCurrent() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
        manager.startingTheGame();

        assertTrue(manager.isFirstTheCurrent());

        for (int i = 0; i < 4; i++) {
            manager.goNextTurn();
        }
        assertTrue(manager.isFirstTheCurrent());
    }

    @Test
    void goNextTurn() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        int numOfPlayers = players.size();
        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        assertEquals(manager.getFirstPlayer(), manager.getCurrentTurnPlayer());

        for (int i = 0; i < numOfPlayers; i++) {
            orderOfPlayers.add(i, manager.getCurrentTurnPlayer());
            manager.goNextTurn();
        }
        for (int j = 0; j < numOfPlayers; j++) {
            assertEquals(orderOfPlayers.get(j), manager.getCurrentTurnPlayer());
            manager.goNextTurn();
        }


    }

    @Test
    void startingTheGame() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            manager.addPlayerToTable("player1", PlayerColor.BLUE);
            manager.addPlayerToTable("player2", PlayerColor.GREEN);
            manager.addPlayerToTable("player3", PlayerColor.RED);
            manager.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }

        assertNull(manager.getFirstPlayer());
        assertNull(manager.getCurrentTurnPlayer());

        manager.startingTheGame();

        assertNotNull(manager.getFirstPlayer());
        assertNotNull(manager.getCurrentTurnPlayer());
        assertTrue(manager.isFirstTheCurrent());
    }

    @Test
    void resetAll() {
        //TODO
    }
}