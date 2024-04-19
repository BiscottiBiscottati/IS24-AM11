package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.table.GameStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    static Deck<ObjectiveCard> dObjective;
    GameModel model;

    @BeforeAll
    static void beforeAll() {
        dObjective = ObjectiveDeckFactory.createDeck();
    }


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
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        //setting objectives and starter
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException e) {
                throw new RuntimeException(e);
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            } catch (PlayerInitException e) {
                throw new RuntimeException(e);
            } catch (IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }


        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        try {
            assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
        //TODO

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
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException e) {
                throw new RuntimeException(e);
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            } catch (PlayerInitException e) {
                throw new RuntimeException(e);
            } catch (IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayList<String> orderOfPlayers = new ArrayList<>(numOfPlayers);
        try {
            assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getPlayerHand() {
        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getPlayerHand("player1"));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException e) {
                throw new RuntimeException(e);
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            } catch (PlayerInitException e) {
                throw new RuntimeException(e);
            } catch (IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            assertEquals(model.getPlayerHand("player1").size(), 3);
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void getPlayerObjective() {
        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getPlayerObjective("player1"));

        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        try {
            assertTrue(model.getPlayerObjective("player1").isEmpty());
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException e) {
                throw new RuntimeException(e);
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            } catch (PlayerInitException e) {
                throw new RuntimeException(e);
            } catch (IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            assertEquals(model.getPlayerObjective("player1").size(), 1);
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void getPlayerColor() {
        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
            assertEquals(model.getPlayerColor("player1"), PlayerColor.BLUE);
            assertEquals(model.getPlayerColor("player2"), PlayerColor.GREEN);
            assertEquals(model.getPlayerColor("player3"), PlayerColor.RED);
            assertEquals(model.getPlayerColor("player4"), PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

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
        {


            Set<String> players = Set.of("player1", "player2", "player3", "player4");
            try {
                model.addPlayerToTable("player1", PlayerColor.BLUE);
                model.addPlayerToTable("player2", PlayerColor.GREEN);
                model.addPlayerToTable("player3", PlayerColor.RED);
                model.addPlayerToTable("player4", PlayerColor.YELLOW);
            } catch (PlayerInitException | GameStatusException e) {
                throw new RuntimeException(e);
            }

            try {
                model.initGame();
            } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
                throw new RuntimeException(e);
            }

            Set<Integer> listObj;

            try {
                assertNotNull(model.getCommonObjectives());
                assertEquals(model.getCommonObjectives().size(), 2);
                listObj = model.getCommonObjectives();
            } catch (GameStatusException e) {
                throw new RuntimeException(e);
            }


            for (Integer id : listObj) {
                assertNotNull(dObjective.getCardById(id));
            }

        }
    }

    @Test
    void getExposedCards() {
        //TODO
    }

    @Test
    void getPlayerPoints() {
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
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        assertThrows(GameStatusException.class, () -> model.initGame());

        assertEquals(model.getStatus(), GameStatus.STARTING);


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
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
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
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.removePlayer("player1"));

    }

    @Test
    void setStarterFor() {
        //TODO
    }

    @Test
    void setObjectiveFor() {
        //TODO
    }

    @Test
    void placeCard() {
        //TODO, need to simulate game
    }

    @Test
    void drawFromDeckOf() {
        //TODO
    }

    @Test
    void drawVisibleOf() {
        //TODO
    }

    @Test
    void countObjectivesPoints() {
        //TODO, need to simulate game

    }

    @Test
    void endGame() {

        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        model.endGame();
        assertEquals(model.getStatus(), GameStatus.SETUP);
    }


    @Test
    void getStatus() {
    }

    @Test
    void getDeckTop() {
    }

    @Test
    void getCandidateObjectives() {
    }

    @Test
    void getStarterCard() {
    }
}

