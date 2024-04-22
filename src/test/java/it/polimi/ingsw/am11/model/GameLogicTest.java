package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.utils.PlayerColor;
import it.polimi.ingsw.am11.table.GameStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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

        List<String> players = List.of("player1", "player2", "player3");
        Deque<String> playerQueue = new ArrayDeque<>(3);
        players.forEach(playerQueue::addLast);
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getCurrentTurnPlayer());
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        assertDoesNotThrow(() -> model.getCurrentTurnPlayer());
        //setting objectives and starter
        for (String nickname : playerQueue) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            assertEquals(model.getCurrentTurnPlayer(), model.getFirstPlayer());
            while (! Objects.equals(playerQueue.peek(), model.getFirstPlayer())) {
                playerQueue.addLast(playerQueue.removeFirst());
            }
            playerQueue.addLast(playerQueue.removeFirst());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }


        assertEquals(GameStatus.ONGOING, model.getStatus());
        try {
            model.placeCard(model.getCurrentTurnPlayer(),
                            model.getPlayerHand(model.getCurrentTurnPlayer())
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(),
                            model.getAvailablePositions(model.getCurrentTurnPlayer())
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(),
                            true);
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }

        try {
            assertEquals(model.getCurrentTurnPlayer(), playerQueue.removeFirst());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        try {
            model.placeCard(model.getCurrentTurnPlayer(),
                            model.getPlayerHand(model.getCurrentTurnPlayer())
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(),
                            model.getAvailablePositions(model.getCurrentTurnPlayer())
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(),
                            true);
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }

        try {
            assertEquals(model.getCurrentTurnPlayer(), playerQueue.removeFirst());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        boolean gameEnded = false;
        while (true) {
            try {
                if (model.getStatus() == GameStatus.ENDED) {
                    gameEnded = true;
                    break;
                }
                model.placeCard(model.getCurrentTurnPlayer(),
                                model.getPlayerHand(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                model.getAvailablePositions(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                true);
                if (model.getDeckTop(PlayableCardType.GOLD).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.GOLD,
                                         model.getCurrentTurnPlayer());
                } else if (model.getDeckTop(PlayableCardType.RESOURCE).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                         model.getCurrentTurnPlayer());
                } else if (model.getExposedCards(PlayableCardType.RESOURCE).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.RESOURCE)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else if (model.getExposedCards(PlayableCardType.GOLD).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.GOLD)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else {
                    model.goNextTurn();
                }
            } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                     IllegalCardPlacingException | PlayerInitException |
                     IllegalPickActionException | NotInHandException | EmptyDeckException |
                     IllegalPlateauActionException | GameBreakingException | MaxHandSizeException |
                     GameStatusException e) {
                throw new RuntimeException(e);
            }
        }
        assertThrows(GameStatusException.class, () -> model.getCurrentTurnPlayer());
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
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
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
    void getPositionedCard() throws PlayerInitException, GameStatusException {
        Set<String> players = Set.of("player1", "player2", "player3");
        int numOfPlayers = players.size();

        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);

        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getPositionedCard("player1"));
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            assertNotNull(model.getPositionedCard(model.getCurrentTurnPlayer()));
            assertEquals(model.getPositionedCard(model.getCurrentTurnPlayer()).size(), 2);
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }

        try {
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            assertNotNull(model.getPositionedCard(model.getCurrentTurnPlayer()));
            assertEquals(model.getPositionedCard(model.getCurrentTurnPlayer()).size(), 2);
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (TurnsOrderException | IllegalCardPlacingException | NotInHandException |
                 IllegalPlateauActionException | IllegalPlayerSpaceActionException |
                 EmptyDeckException | GameBreakingException | MaxHandSizeException |
                 IllegalPickActionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAvailablePositions() throws PlayerInitException, GameStatusException {

        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getAvailablePositions("player1"));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(() -> model.getAvailablePositions("player1"));

        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, true);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        assertEquals(model.getAvailablePositions(model.getCurrentTurnPlayer()).size(), 4);
        try {
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            assertEquals(model.getAvailablePositions(model.getCurrentTurnPlayer()).size(), 6);
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(model.getAvailablePositions(model.getCurrentTurnPlayer()).size(), 4);
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

        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getExposedCards(PlayableCardType.GOLD));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        try {
            assertDoesNotThrow(() -> model.getExposedCards(PlayableCardType.GOLD));
            assertEquals(model.getExposedCards(PlayableCardType.GOLD).size(), 2);
            assertDoesNotThrow(() -> model.getExposedCards(PlayableCardType.RESOURCE));
            assertEquals(model.getExposedCards(PlayableCardType.RESOURCE).size(), 2);
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                model.getExposedCards(
                                        PlayableCardType.GOLD).stream().findFirst().orElseThrow());
            assertEquals(model.getExposedCards(PlayableCardType.GOLD).size(), 2);
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | GameBreakingException |
                 IllegalPickActionException e) {
            throw new RuntimeException(e);
        }

        try {
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                model.getExposedCards(
                                        PlayableCardType.RESOURCE).stream().findFirst().orElseThrow());
            assertEquals(model.getExposedCards(PlayableCardType.RESOURCE).size(), 2);
        } catch (TurnsOrderException | IllegalCardPlacingException | NotInHandException |
                 IllegalPlateauActionException | IllegalPlayerSpaceActionException |
                 GameBreakingException | IllegalPickActionException | PlayerInitException |
                 GameStatusException e) {
            throw new RuntimeException(e);
        }
        model.forceEnd();
        assertThrows(GameStatusException.class, () -> model.getExposedCards(PlayableCardType.GOLD));
        assertThrows(GameStatusException.class,
                     () -> model.getExposedCards(PlayableCardType.RESOURCE));
    }

    @Test
    void getPlayerPoints() {

        Set<String> players = Set.of("player1", "player2", "player3", "player4");

        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        assertThrows(GameStatusException.class, () -> model.getPlayerPoints("player1"));


        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        assertThrows(PlayerInitException.class, () -> model.getPlayerPoints("playerX"));

        try {
            for (String nickname : players) {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            }
        } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                 IllegalCardPlacingException e) {
            throw new RuntimeException(e);
        }

        try {
            assertEquals(model.getPlayerPoints("player1"), 0);
            assertEquals(model.getPlayerPoints("player2"), 0);
            assertEquals(model.getPlayerPoints("player3"), 0);
            assertEquals(model.getPlayerPoints("player4"), 0);
        } catch (PlayerInitException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        model.forceEnd();
        assertThrows(GameStatusException.class, () -> model.getPlayerPoints("player1"));
    }


    @Test
    void getPlayerFinishingPosition()
    throws PlayerInitException, GameBreakingException, GameStatusException,
           IllegalCardBuildException {

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
        try {
            for (String nickname : players) {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            }
        } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                 IllegalCardPlacingException e) {
            throw new RuntimeException(e);
        }

        assertThrows(GameStatusException.class, () -> model.getPlayerFinishingPosition("player1"));
        PlayableCard.Builder builder = new ResourceCard.Builder(1, 1, Color.BLUE);

        boolean gameEnded = false;
        while (true) {
            try {
                if (model.getStatus() == GameStatus.ENDED) {
                    gameEnded = true;
                    break;
                }
                model.placeCard(model.getCurrentTurnPlayer(),
                                model.getPlayerHand(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                model.getAvailablePositions(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                true);
                if (model.getDeckTop(PlayableCardType.GOLD).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.GOLD,
                                         model.getCurrentTurnPlayer());
                } else if (model.getDeckTop(PlayableCardType.RESOURCE).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                         model.getCurrentTurnPlayer());
                } else if (model.getExposedCards(PlayableCardType.RESOURCE).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.RESOURCE)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else if (model.getExposedCards(PlayableCardType.GOLD).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.GOLD)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else {
                    model.goNextTurn();
                }
            } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                     IllegalCardPlacingException | NotInHandException | EmptyDeckException |
                     IllegalPlateauActionException | MaxHandSizeException |
                     IllegalPickActionException e) {
                throw new RuntimeException(e);
            }

        }

        for (String nickname : players) {
            assertDoesNotThrow(() -> model.getPlayerFinishingPosition(nickname));
        }
        assertThrows(PlayerInitException.class, () -> model.getPlayerFinishingPosition("playerX"));
    }

    @Test
    void getWinner() {
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
        try {
            for (String nickname : players) {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            }
        } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                 IllegalCardPlacingException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getWinner());

        while (true) {
            try {
                if (model.getStatus() == GameStatus.ENDED) {
                    break;
                }
                model.placeCard(model.getCurrentTurnPlayer(),
                                model.getPlayerHand(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                model.getAvailablePositions(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                true);
                if (model.getDeckTop(PlayableCardType.GOLD).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.GOLD,
                                         model.getCurrentTurnPlayer());
                } else if (model.getDeckTop(PlayableCardType.RESOURCE).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                         model.getCurrentTurnPlayer());
                } else if (model.getExposedCards(PlayableCardType.RESOURCE).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.RESOURCE)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else if (model.getExposedCards(PlayableCardType.GOLD).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.GOLD)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else {
                    model.goNextTurn();
                }
            } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                     IllegalCardPlacingException | NotInHandException | EmptyDeckException |
                     IllegalPlateauActionException | MaxHandSizeException |
                     IllegalPickActionException | GameStatusException | PlayerInitException |
                     GameBreakingException e) {
                throw new RuntimeException(e);
            }

        }

        assertDoesNotThrow(() -> model.getWinner());
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
            model.removePlayer("player3");
            model.removePlayer("player2");
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
            model.addPlayerToTable("cacca", PlayerColor.BLUE);
            model.addPlayerToTable("farfalla", PlayerColor.GREEN);
            model.addPlayerToTable("geese", PlayerColor.RED);
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
                     () -> model.removePlayer("cacca"));

    }

    @Test
    void setStarterFor() {

        Set<String> players = Set.of("player1", "player2", "player3");

        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.setStarterFor("player1", false));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertThrows(PlayerInitException.class, () -> model.setStarterFor("playerX", false));
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        assertDoesNotThrow(() -> model.getStarterCard("player1"));
        assertDoesNotThrow(() -> model.getStarterCard("player2"));
        assertDoesNotThrow(() -> model.getStarterCard("player3"));
    }

    @Test
    void setObjectiveFor() {
        Set<String> players = Set.of("player1", "player2", "player3");
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
        assertThrows(PlayerInitException.class,
                     () -> model.setObjectiveFor("playerX", model.getCandidateObjectives(
                             "playerX").stream().findFirst().orElseThrow()));
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        for (String nickname : players) {
            assertDoesNotThrow(() -> model.getPlayerObjective(nickname));
        }
    }

    @Test
    void placeCard() {

        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.placeCard("player1", model.getPlayerHand(
                                                   "player1").stream().findFirst().orElseThrow(),
                                           model.getAvailablePositions(
                                                   "player1").stream().findFirst().orElseThrow(),
                                           false));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.placeCard("player1", model.getPlayerHand(
                                                   "player1").stream().findFirst().orElseThrow(),
                                           model.getAvailablePositions(
                                                   "player1").stream().findFirst().orElseThrow(),
                                           false));
        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        String previousPlayer;
        try {
            assertThrows(IllegalCardPlacingException.class,
                         () -> model.placeCard(model.getCurrentTurnPlayer(),
                                               100,
                                               model.getAvailablePositions(
                                                            model.getCurrentTurnPlayer())
                                                    .stream()
                                                    .findFirst()
                                                    .orElseThrow(),
                                               true));
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            previousPlayer = model.getCurrentTurnPlayer();
            assertThrows(TurnsOrderException.class,
                         () -> model.placeCard(model.getCurrentTurnPlayer(),
                                               model.getPlayerHand(model.getCurrentTurnPlayer())
                                                    .stream()
                                                    .findFirst().orElseThrow(),
                                               model.getAvailablePositions(
                                                            model.getCurrentTurnPlayer())
                                                    .stream()
                                                    .findFirst().orElseThrow(),
                                               true));
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
            assertThrows(TurnsOrderException.class,
                         () -> model.placeCard(previousPlayer,
                                               model.getPlayerHand(previousPlayer)
                                                    .stream()
                                                    .findFirst()
                                                    .orElseThrow(),
                                               model.getAvailablePositions(previousPlayer)
                                                    .stream()
                                                    .findFirst()
                                                    .orElseThrow(),
                                               true));
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void drawFromDeckOf() {
        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.drawFromDeckOf(PlayableCardType.GOLD, "player1"));
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
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        assertThrows(PlayerInitException.class,
                     () -> model.drawFromDeckOf(PlayableCardType.GOLD, "playerX"));
        String previousPlayer;
        try {
            assertThrows(IllegalPickActionException.class,
                         () -> model.drawFromDeckOf(PlayableCardType.GOLD,
                                                    model.getCurrentTurnPlayer()));
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            previousPlayer = model.getCurrentTurnPlayer();
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }
        assertThrows(TurnsOrderException.class,
                     () -> model.drawFromDeckOf(PlayableCardType.GOLD, previousPlayer));

        while (true) {
            try {
                if (model.getStatus() == GameStatus.ENDED) {
                    break;
                }
                model.placeCard(model.getCurrentTurnPlayer(),
                                model.getPlayerHand(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                model.getAvailablePositions(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                true);
                if (model.getDeckTop(PlayableCardType.GOLD).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.GOLD,
                                         model.getCurrentTurnPlayer());
                } else if (model.getDeckTop(PlayableCardType.RESOURCE).isPresent()) {
                    assertThrows(EmptyDeckException.class,
                                 () -> model.drawFromDeckOf(PlayableCardType.GOLD,
                                                            model.getCurrentTurnPlayer()));
                    model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                         model.getCurrentTurnPlayer());
                } else if (model.getExposedCards(PlayableCardType.RESOURCE).isEmpty()) {
                    assertThrows(EmptyDeckException.class,
                                 () -> model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                                            model.getCurrentTurnPlayer()));
                    model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.RESOURCE)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else if (model.getExposedCards(PlayableCardType.GOLD).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.GOLD)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else {
                    model.goNextTurn();
                }
            } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                     IllegalCardPlacingException | NotInHandException | EmptyDeckException |
                     IllegalPlateauActionException | MaxHandSizeException |
                     IllegalPickActionException | GameStatusException | PlayerInitException |
                     GameBreakingException e) {
                throw new RuntimeException(e);
            }

        }
        assertThrows(GameStatusException.class,
                     () -> model.drawFromDeckOf(PlayableCardType.GOLD, "player1"));
    }

    @Test
    void drawVisibleOf() throws PlayerInitException, GameStatusException {

        Set<String> players = Set.of("player1", "player2", "player3");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class,
                     () -> model.drawVisibleOf(PlayableCardType.GOLD, "player1", 1));
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
            } catch (IllegalPlayerSpaceActionException | GameStatusException | PlayerInitException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        assertThrows(PlayerInitException.class,
                     () -> model.drawVisibleOf(PlayableCardType.GOLD, "playerX", 1));
        String previousPlayer;
        try {
            assertThrows(IllegalPickActionException.class,
                         () -> model.drawVisibleOf(PlayableCardType.GOLD,
                                                   model.getCurrentTurnPlayer(),
                                                   model.getExposedCards(PlayableCardType.GOLD)
                                                        .stream()
                                                        .findFirst()
                                                        .orElseThrow()));
            model.placeCard(model.getCurrentTurnPlayer(), model.getPlayerHand(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            model.getAvailablePositions(
                                    model.getCurrentTurnPlayer()).stream().findFirst().orElseThrow(),
                            true);
            previousPlayer = model.getCurrentTurnPlayer();
            model.drawFromDeckOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer());
        } catch (GameStatusException | TurnsOrderException | IllegalCardPlacingException |
                 PlayerInitException | NotInHandException | IllegalPlateauActionException |
                 IllegalPlayerSpaceActionException | EmptyDeckException | GameBreakingException |
                 MaxHandSizeException | IllegalPickActionException e) {
            throw new RuntimeException(e);
        }
        assertThrows(TurnsOrderException.class,
                     () -> model.drawVisibleOf(PlayableCardType.GOLD, previousPlayer, 2));

        while (true) {
            try {
                if (model.getStatus() == GameStatus.ENDED) {
                    break;
                }
                model.placeCard(model.getCurrentTurnPlayer(),
                                model.getPlayerHand(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                model.getAvailablePositions(model.getCurrentTurnPlayer())
                                     .stream()
                                     .findFirst().orElseThrow(),
                                true);
                if (model.getDeckTop(PlayableCardType.GOLD).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.GOLD,
                                         model.getCurrentTurnPlayer());
                } else if (model.getDeckTop(PlayableCardType.RESOURCE).isPresent()) {
                    model.drawFromDeckOf(PlayableCardType.RESOURCE,
                                         model.getCurrentTurnPlayer());
                } else if (model.getExposedCards(PlayableCardType.RESOURCE).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.RESOURCE, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.RESOURCE)
                                             .stream()
                                             .findFirst().orElseThrow());
                } else if (model.getExposedCards(PlayableCardType.GOLD).isEmpty()) {
                    model.drawVisibleOf(PlayableCardType.GOLD, model.getCurrentTurnPlayer(),
                                        model.getExposedCards(PlayableCardType.GOLD)
                                             .stream()
                                             .findFirst().orElseThrow());

                    assertThrows(EmptyDeckException.class,
                                 () -> model.drawVisibleOf(PlayableCardType.RESOURCE,
                                                           model.getCurrentTurnPlayer(),
                                                           model.getExposedCards(
                                                                        PlayableCardType.RESOURCE)
                                                                .stream()
                                                                .findFirst()
                                                                .orElseThrow()));
                } else {
                    model.goNextTurn();
                }
            } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                     IllegalCardPlacingException | NotInHandException | EmptyDeckException |
                     IllegalPlateauActionException | MaxHandSizeException |
                     IllegalPickActionException | GameStatusException | PlayerInitException |
                     GameBreakingException e) {
                throw new RuntimeException(e);
            }
        }
        assertThrows(GameStatusException.class,
                     () -> model.drawVisibleOf(PlayableCardType.GOLD, "player1", 1));
    }

    @Test
    void forceEnd() {

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

        assertEquals(model.getStatus(), GameStatus.STARTING);
        model.forceEnd();
        assertEquals(model.getStatus(), GameStatus.SETUP);
        assertDoesNotThrow(() -> model.removePlayer("player1"));

        assertFalse(model.getPlayers().contains("player1"));
    }


    @Test
    void getStatus() {

        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        assertEquals(model.getStatus(), GameStatus.SETUP);
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(model.getStatus(), GameStatus.STARTING);
        model.forceEnd();
        assertEquals(model.getStatus(), GameStatus.SETUP);
    }

    @Test
    void getDeckTop() throws GameStatusException {

        assertThrows(GameStatusException.class, () -> model.getDeckTop(PlayableCardType.GOLD));
        assertThrows(GameStatusException.class, () -> model.getDeckTop(PlayableCardType.RESOURCE));

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
        assertNotNull(model.getDeckTop(PlayableCardType.GOLD));
        assertNotNull(model.getDeckTop(PlayableCardType.RESOURCE));
    }

    @Test
    void getCandidateObjectives() {

        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getCandidateObjectives("player1"));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        for (String nickname : players) {
            try {
                assertNotNull(model.getCandidateObjectives(nickname));
                assertEquals(model.getCandidateObjectives(nickname).size(), 2);
            } catch (GameStatusException | PlayerInitException e) {
                throw new RuntimeException(e);
            }
        }

        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (GameStatusException | PlayerInitException | IllegalPlayerSpaceActionException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        for (String nickname : players) {
            assertThrows(GameStatusException.class, () -> model.getCandidateObjectives(nickname));
        }

    }

    @Test
    void getStarterCard() {

        Set<String> players = Set.of("player1", "player2", "player3", "player4");
        try {
            model.addPlayerToTable("player1", PlayerColor.BLUE);
            model.addPlayerToTable("player2", PlayerColor.GREEN);
            model.addPlayerToTable("player3", PlayerColor.RED);
            model.addPlayerToTable("player4", PlayerColor.YELLOW);
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
        assertThrows(GameStatusException.class, () -> model.getStarterCard("player1"));
        try {
            model.initGame();
        } catch (IllegalNumOfPlayersException | GameStatusException | GameBreakingException e) {
            throw new RuntimeException(e);
        }

        for (String nickname : players) {
            try {
                assertNotNull(model.getStarterCard(nickname));
            } catch (GameStatusException | PlayerInitException e) {
                throw new RuntimeException(e);
            }
        }

        for (String nickname : players) {
            try {
                model.setObjectiveFor(nickname, model.getCandidateObjectives(
                        nickname).stream().findFirst().orElseThrow());
                model.setStarterFor(nickname, false);
            } catch (GameStatusException | PlayerInitException | IllegalPlayerSpaceActionException |
                     IllegalCardPlacingException e) {
                throw new RuntimeException(e);
            }
        }
        for (String nickname : players) {
            assertDoesNotThrow(() -> model.getStarterCard(nickname));
        }
    }
}

