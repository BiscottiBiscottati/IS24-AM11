package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    }

    @Test
    void getHand() {
    }

    @Test
    void getPlayerObjective() {
    }

    @Test
    void getPlayerColor() {
    }

    @Test
    void addPlayerToTable() {
    }

    @Test
    void removePlayer() {
    }

    @Test
    void isFirstTheCurrent() {
    }

    @Test
    void goNextTurn() {
    }
}