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

        assertThrows(GameStatusException.class, () -> model.pickStarterFor("edo"));

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

            // Check if cards have been dealt
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
            card = model.pickStarterFor("edo");
            starterCards.put("edo", card);
        } catch (EmptyDeckException | GameStatusException | PlayerInitException |
                 IllegalPlayerSpaceActionException e) {
            throw new RuntimeException(e);
        }

        assertThrows(IllegalPlayerSpaceActionException.class, () -> model.pickStarterFor("edo"));

        try {
            assertTrue(model.getStarterCard("edo").isPresent());
            card = model.pickStarterFor("chen");
            starterCards.put("chen", card);
            card = model.pickStarterFor("osama");
            starterCards.put("osama", card);

            assertThrows(PlayerInitException.class,
                         () -> model.pickStarterFor("lola"));
            assertThrows(PlayerInitException.class, () -> model.getStarterCard("lola"));

            assertTrue(model.getAvailablePositions("edo")
                            .contains(Position.of(0, 0)));

            Function<String, Optional<Integer>> getStarter = player -> {
                try {
                    return model.getStarterCard(player);
                } catch (PlayerInitException e) {
                    throw new RuntimeException(e);
                }
            };

            Set<Integer> starters = players.stream()
                                           .map(getStarter)
                                           .filter(Optional::isPresent)
                                           .map(Optional::get)
                                           .collect(Collectors.toSet());
            assertEquals(3, starters.size());

            for (String player : players) {
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));
                model.setStarterFor(player, true);
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));
            }

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

            players.stream()
                   .map(getAvailablePos)
                   .map(positions::containsAll)
                   .forEach(Assertions::assertTrue);

            model.pickCandidateObjectives("edo"); //TODO


        } catch (PlayerInitException | GameStatusException | EmptyDeckException |
                 IllegalCardPlacingException | IllegalPlayerSpaceActionException e) {
            throw new RuntimeException(e);
        } catch (IllegalPickActionException e) {
            throw new RuntimeException(e);
        }

    }
}