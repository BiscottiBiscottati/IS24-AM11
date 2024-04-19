package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.players.field.PositionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    static Deck<GoldCard> goldCardDeck;
    static Deck<ResourceCard> resourceCardDeck;
    static Deck<ObjectiveCard> objectiveCardDeck;
    static Set<String> players;
    GameModel model;

    @BeforeAll
    static void beforeAll() {
        goldCardDeck = GoldDeckFactory.createDeck();
        resourceCardDeck = ResourceDeckFactory.createDeck();
        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
        players = Set.of("edo", "chen", "osama");
    }

    @BeforeEach
    void setUp() {
        model = new GameLogic();
        goldCardDeck.reset();
        resourceCardDeck.reset();
        objectiveCardDeck.reset();

        try {
            model.addPlayerToTable("edo", PlayerColor.RED);
            model.addPlayerToTable("chen", PlayerColor.BLUE);
            model.addPlayerToTable("osama", PlayerColor.YELLOW);
            model.initGame();
        } catch (PlayerInitException | GameStatusException | IllegalNumOfPlayersException |
                 GameBreakingException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void testInitGame() {
        model = new GameLogic();
        // Test illegal number of players
        assertThrows(IllegalNumOfPlayersException.class, () -> model.initGame());
        // Test adding of players
        assertDoesNotThrow(() -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertDoesNotThrow(() -> model.addPlayerToTable("chen", PlayerColor.BLUE));
        assertDoesNotThrow(() -> model.addPlayerToTable("osama", PlayerColor.YELLOW));

        Set<String> players = Set.of("edo", "chen", "osama");
        // Test init game
        assertDoesNotThrow(() -> model.initGame());

        // Check if the players are the same
        assertEquals(3, model.getPlayers().size());
        assertEquals(players, model.getPlayers());

        // Check if user can't add more players
        assertThrows(GameStatusException.class,
                     () -> model.addPlayerToTable("lola", PlayerColor.GREEN));
        assertThrows(GameStatusException.class, model::initGame);

        try {
            // Check if the first player is the same as the current turn player
            assertTrue(players.contains(model.getFirstPlayer()));
            assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());

            // Check if the player can be removed
            assertThrows(GameStatusException.class, () -> model.removePlayer("lola"));

            // Check if cards have been dealt on the table
            assertEquals(2, model.getCommonObjectives().size());
            assertEquals(2,
                         model.getCommonObjectives().stream()
                              .map(objectiveCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());

            assertEquals(2, model.getExposedCards(PlayableCardType.GOLD).size());
            assertEquals(2,
                         model.getExposedCards(PlayableCardType.GOLD).stream()
                              .map(goldCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());

            assertEquals(2, model.getExposedCards(PlayableCardType.RESOURCE).size());
            assertEquals(2,
                         model.getExposedCards(PlayableCardType.RESOURCE).stream()
                              .map(resourceCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());
        } catch (GameStatusException e) {
            throw new RuntimeException(e);
        }

        try {
            assertEquals(Position.of(0, 0),
                         model.getAvailablePositions("edo").stream()
                              .findFirst()
                              .orElseThrow());
        } catch (PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }

        try {
            assertEquals(3, model.getPlayerHand("edo").size());

            assertEquals(1,
                         model.getPlayerHand("edo")
                              .stream()
                              .map(goldCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());

            assertEquals(2,
                         model.getPlayerHand("edo")
                              .stream()
                              .map(resourceCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());
        } catch (GameStatusException | PlayerInitException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDealingStarter() {
        int card;
        Map<String, Integer> starterCards = new HashMap<>(8);

        try {
            // Test if the player can pick a starter card
            starterCards.put("edo", model.getStarterCard("edo").orElseThrow());
            starterCards.put("osama", model.getStarterCard("osama").orElseThrow());
            starterCards.put("chen", model.getStarterCard("chen").orElseThrow());

        } catch (GameStatusException | PlayerInitException e) {
            throw new RuntimeException(e);
        }

        // Test if the player can't pick a starter card twice

        try {

            // pick if Starter is present
            assertTrue(model.getStarterCard("edo").isPresent());


            // Test if the player doesn't exist
            assertThrows(PlayerInitException.class, () -> model.getStarterCard("lola"));

            // Test if before placing positions the starter card is not set
            assertTrue(model.getAvailablePositions("edo")
                            .contains(Position.of(0, 0)));

            Function<String, Optional<Integer>> getStarter = player -> {
                try {
                    return model.getStarterCard(player);
                } catch (PlayerInitException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };

            // getting all starters dealt
            Set<Integer> starters = players.stream()
                                           .map(getStarter)
                                           .filter(Optional::isPresent)
                                           .map(Optional::get)
                                           .collect(Collectors.toSet());
            // check starters are the same as players
            assertEquals(3, starters.size());

            for (String player : players) {
                // check if starter is the same for each player
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));

                // place starter for each player
                model.setStarterFor(player, true);

                // check that nothing changes
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));
            }

            // get all positions around 0,0
            Set<Position> positions = Stream.of(Corner.values())
                                            .map(corner -> PositionManager.getPositionIn(
                                                    Position.of(0, 0), corner))
                                            .collect(Collectors.toSet());

            Function<String, Set<Position>> getAvailablePos = player -> {
                try {
                    return model.getAvailablePositions(player);
                } catch (PlayerInitException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };

            // check that available positions are the same as the ones around 0,0
            players.stream()
                   .map(getAvailablePos)
                   .map(positions::containsAll)
                   .forEach(Assertions::assertTrue);

        } catch (PlayerInitException | GameStatusException |
                 IllegalCardPlacingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testDealingObjective() {
        Map<String, Integer> objOfPlayer = new HashMap<>(8);
        try {
            for (String player : players) {
                // pick candidate objectives
                Set<Integer> objCards = model.getCandidateObjectives(player);

                // check size of candidate objectives
                assertEquals(2, objCards.size());
                assertEquals(2, model.getCandidateObjectives(player).size());
                assertEquals(objCards, model.getCandidateObjectives(player));


                // check that they are objective cards
                assertEquals(2,
                             objCards.stream()
                                     .map(objectiveCardDeck::getCardById)
                                     .filter(Optional::isPresent)
                                     .count());

                // check that player can't set an objective that is not in the candidate objectives
                assertThrows(IllegalPlayerSpaceActionException.class,
                             () -> model.setObjectiveFor(player, 0));

                int chosenCard = objCards.stream().findFirst().orElseThrow();
                objOfPlayer.put(player, chosenCard);

                // set objective for player
                model.setObjectiveFor(player, chosenCard);

                // check that objective has been set
                assertEquals(chosenCard,
                             model.getPlayerObjective(player).stream().findFirst().orElseThrow());
            }

            for (String player : players) {
                // check each objective has been set and are equals
                Optional<Integer> objCard = model.getPlayerObjective(player).stream().findFirst();
                assertEquals(Optional.of(objOfPlayer.get(player)), objCard);
            }
        } catch (GameStatusException |
                 PlayerInitException | IllegalPlayerSpaceActionException e) {
            throw new RuntimeException(e);
        }
    }


}