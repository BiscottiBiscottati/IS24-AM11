package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.exceptions.GameStatusException;
import it.polimi.ingsw.am11.exceptions.IllegalNumOfPlayersException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    static Deck<GoldCard> goldCardDeck;
    static Deck<ResourceCard> resourceCardDeck;
    static Deck<ObjectiveCard> objectiveCardDeck;
    GameModel model;

    @BeforeAll
    static void beforeAll() {
        goldCardDeck = GoldDeckFactory.createDeck();
        resourceCardDeck = ResourceDeckFactory.createDeck();
        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
    }

    @BeforeEach
    void setUp() {
        model = new GameLogic();
        goldCardDeck.reset();
        resourceCardDeck.reset();
        objectiveCardDeck.reset();

    }


    @Test
    void testInitGame() {
        assertThrows(IllegalNumOfPlayersException.class, () -> model.initGame());
        assertDoesNotThrow(() -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertDoesNotThrow(() -> model.addPlayerToTable("chen", PlayerColor.BLUE));
        assertDoesNotThrow(() -> model.addPlayerToTable("osama", PlayerColor.YELLOW));

        Set<String> players = Set.of("edo", "chen", "osama");
        assertDoesNotThrow(() -> model.initGame());

        assertEquals(3, model.getPlayers().size());
        assertEquals(players, model.getPlayers());

        assertThrows(GameStatusException.class,
                     () -> model.addPlayerToTable("lola", PlayerColor.GREEN));
        assertThrows(GameStatusException.class, model::initGame);

        try {
            assertTrue(players.contains(model.getFirstPlayer()));
            assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());

            assertThrows(GameStatusException.class, () -> model.removePlayer("lola"));

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

            assertEquals(1,
                         model.getPlayerHand("edo")
                              .stream()
                              .map(resourceCardDeck::getCardById)
                              .filter(Optional::isPresent)
                              .count());
        } catch (GameStatusException | PlayerInitException e) {
            throw new RuntimeException(e);
        }
    }
}