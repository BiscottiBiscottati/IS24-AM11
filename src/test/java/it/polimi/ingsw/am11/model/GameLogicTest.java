package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.exceptions.GameBreakingException;
import it.polimi.ingsw.am11.exceptions.GameStatusException;
import it.polimi.ingsw.am11.exceptions.IllegalNumOfPlayersException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.table.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    GameModel model;

    @BeforeEach
    void setUp() {
        model = new GameLogic();
    }

    @Test
    void getPlayers() {
        Set<String> players = Set.of("player1", "player2", "player3");
        assertTrue(model.getPlayers().isEmpty());
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        Set<String> modelPlayers = model.getPlayers();

        assertEquals(3, modelPlayers.size());
        assertEquals(players, modelPlayers);
    }

    @Test
    void getCurrentTurnPlayer() {

        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());
        for (int i = 0; i < numOfPlayers; i++) {
            orderOfPlayers.add(i, model.getCurrentTurnPlayer());
            try {
                model.goNextTurn();
            } catch (GameBreakingException | GameStatusException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < numOfPlayers; j++) {
                assertEquals(orderOfPlayers.get(j), model.getCurrentTurnPlayer());
                try {
                    model.goNextTurn();
                } catch (GameBreakingException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    void getFirstPlayer() {
        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());
        String firstPlayer = model.getFirstPlayer();
        try {
            model.goNextTurn();
        } catch (GameBreakingException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals(firstPlayer, model.getFirstPlayer());
    }

    @Test
    void getPlayerHand() {
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
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        assertEquals(model.getPlayerColor("player1"), PlayerColor.BLUE);
        assertEquals(model.getPlayerColor("player2"), PlayerColor.GREEN);
        assertEquals(model.getPlayerColor("player3"), PlayerColor.RED);
        assertEquals(model.getPlayerColor("player4"), PlayerColor.YELLOW);
    }

    @Test
    void getPositionedCard() {
        //TODO
    }

    @Test
    void getAvailablePositions() {
        //TODO
    }


    @Test
    void getCommonObjectives() {
        //TODO
    }

    @Test
    void getPlayerPoints() {
        //TODO
    }

    @Test
    void isArmageddonTime() {
        //TODO
    }

    @Test
    void getPlayerFinishingPosition() {
        //TODO
    }

    @Test
    void getWinner() {
        //TODO
    }

    @Test
    void initGame() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        assertThrows(IllegalNumOfPlayersException.class, () -> model.initGame());
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(IllegalNumOfPlayersException.class, () -> model.initGame());
        try {
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        assertThrows(GameStatusException.class, () -> model.initGame());

        assertEquals(model.getStatus(), GameStatus.ONGOING);


    }

    @Test
    void addPlayerToTable() {
        assertDoesNotThrow(() -> model.addPlayerToTable("player1", PlayerColor.BLUE));
        assertDoesNotThrow(() -> model.addPlayerToTable("player2", PlayerColor.GREEN));

        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("player1", PlayerColor.RED));

        assertDoesNotThrow(() -> model.addPlayerToTable("player3", PlayerColor.RED));

        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("player4", PlayerColor.RED));
        assertDoesNotThrow(() -> model.addPlayerToTable("player4", PlayerColor.YELLOW));

        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("player5", PlayerColor.YELLOW));

        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        assertThrows(GameStatusException.class,
                     () -> model.addPlayerToTable("player5", PlayerColor.YELLOW));

    }

    @Test
    void removePlayer() {
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        //Normal case
        try {
            model.removePlayer("player1");
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
        Set<String> players = Set.of("player2", "player3");
        Set<String> modelPlayers = model.getPlayers();
        assertEquals(modelPlayers, players);

        //Remove a player that is not there
        try {
            model.removePlayer("Giorgio");
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
        players = Set.of("player2", "player3");
        modelPlayers = model.getPlayers();
        assertEquals(modelPlayers, players);

        //Remove all
        try {
            model.removePlayer("player2");
            model.removePlayer("player3");
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertTrue(model.getPlayers().isEmpty());

        //Remove a player that is not there in an empty playerSet
        try {
            model.removePlayer("Giorgio");
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertTrue(model.getPlayers().isEmpty());

        //Adding new players
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        //Test GameStatusException
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.removePlayer("player1"));

    }

    @Test
    void pickStarter() {
    }

    @Test
    void pickObjective() {
    }

    @Test
    void setStarterFor() {
    }

    @Test
    void setObjectiveFor() {
    }

    @Test
    void goNextTurn() {
    }

    @Test
    void placeCard() {
    }

    @Test
    void countObjectivesPoints() {
    }

    @Test
    void endGame() {
    }
}