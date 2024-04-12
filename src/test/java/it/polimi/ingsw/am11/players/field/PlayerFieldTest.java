package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PlayerFieldTest {
    static Deck<ResourceCard> resourceDeck;
    static Deck<StarterCard> starterDeck;
    static Deck<GoldCard> goldDeck;
    static Random gen;
    PlayerField playerField;

    @BeforeAll
    static void beforeAll() {
        resourceDeck = ResourceDeckFactory.createDeck();
        starterDeck = StarterDeckFactory.createDeck();
        goldDeck = GoldDeckFactory.createDeck();
        gen = new Random();
    }

    @BeforeEach
    void setUp() {
        playerField = new PlayerField();
        resourceDeck.reset();
        starterDeck.reset();
    }

    @Test
    void getCardsPositioned() {
        Map<Position, CardContainer> positionedCards = playerField.getCardsPositioned();
        assertTrue(positionedCards.isEmpty());

        StarterCard starter = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        positionedCards = playerField.getCardsPositioned();
        assertEquals(1, positionedCards.size());
        assertTrue(positionedCards.containsKey(Position.of(0, 0)));
        assertTrue(playerField.containsCard(starter));

        ResourceCard resource = resourceDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.place(resource, Position.of(1, 1), false));
        positionedCards = playerField.getCardsPositioned();
        assertEquals(2, positionedCards.size());
        assertTrue(positionedCards.containsKey(Position.of(1, 1)));
        assertTrue(positionedCards.containsKey(Position.of(0, 0)));
        assertTrue(playerField.containsCard(resource));
        assertTrue(playerField.containsCard(starter));
    }

    @Test
    void getPlacedCardColours() {
        playerField.getPlacedCardColours()
                   .forEach((color, integer) -> assertEquals(0, integer));

        StarterCard starter = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        playerField.getPlacedCardColours()
                   .forEach((color, integer) -> assertEquals(0, integer));

        ResourceCard resourceCard = resourceDeck.draw().orElseThrow();
        Color colorOfCard = resourceCard.getColor();
        assertDoesNotThrow(() -> playerField.place(resourceCard, Position.of(1, 1), false));
        assertEquals(1, playerField.getPlacedCardColours().get(colorOfCard));
    }

    @Test
    void placeStartingCard() {
        StarterCard starterCard = starterDeck.draw().orElseThrow();

        // Testing placing a StarterCard on its retro
        assertDoesNotThrow(() -> playerField.placeStartingCard(starterCard, true));

        // Checking that the card is placed and the position is not available
        assertFalse(playerField.isAvailable(Position.of(0, 0)));
        assertTrue(playerField.containsCard(starterCard));

        // Testing illegal positioning of another starter
        StarterCard secondStarter = starterDeck.draw().orElseThrow();
        assertThrows(IllegalCardPlacingException.class,
                     () -> playerField.placeStartingCard(secondStarter, false));
    }

    @Test
    void place() {
        Set<FieldCard> placedCards = new HashSet<>(40);
        Set<Position> placedPos = new HashSet<>(40);
        ResourceCard resourceCard = resourceDeck.draw().orElseThrow();
        AtomicInteger pointsGiven = new AtomicInteger();
        int pointsExpected;

        // Testing placing a card on the starter position
        assertThrows(IllegalCardPlacingException.class,
                     () -> pointsGiven.set(
                             playerField.place(resourceCard,
                                               Position.of(0, 0),
                                               false)));

        // Checking that no points have been given
        assertEquals(0, pointsGiven.get());

        // Placing a StarterCard on its retro
        StarterCard starter = starterDeck.draw().orElseThrow();
        placedCards.add(starter);
        assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        placedPos.add(Position.of(0, 0));

        // Placing a ResourceCard on (1, 1)
        assertDoesNotThrow(() -> pointsGiven.set(
                playerField.place(resourceCard, Position.of(1, 1), false))
        );
        placedCards.add(resourceCard);
        placedPos.add(Position.of(1, 1));

        // Checking that the card gives the expected points
        pointsExpected = resourceCard.getPoints();
        assertEquals(pointsExpected, pointsGiven.get());

        // Checking that the cards are placed and the positions are not available
        placedPos.forEach(pos -> assertFalse(playerField.isAvailable(pos)));
        placedCards.forEach(card -> assertTrue(playerField.containsCard(card)));

        // Testing illegal positioning to same position
        assertThrows(IllegalCardPlacingException.class,
                     () -> playerField.place(resourceCard,
                                             Position.of(1, 1),
                                             false));

        // Testing illegal positioning to starter position
        assertThrows(IllegalCardPlacingException.class,
                     () -> playerField.place(resourceCard,
                                             Position.of(0, 0),
                                             false));

        // Checking nothing has changed from the previous state
        placedCards.forEach(card -> assertTrue(playerField.containsCard(card)));
        placedPos.forEach(pos -> assertFalse(playerField.isAvailable(pos)));

        // Testing random placements on its retro
        for (int i = 0; i < 4; i++) {

            // Getting a random subset of available positions
            List<Position> posToPlace = playerField.getAvailablePositions()
                                                   .stream()
                                                   .filter(pos -> gen.nextBoolean())
                                                   .toList();
            // Placing the cards
            for (Position position : posToPlace) {
                ResourceCard card = resourceDeck.draw().orElseThrow();
                placedCards.add(card);
                assertDoesNotThrow(() -> pointsGiven.set(
                        playerField.place(card, position, true))
                );
                placedPos.add(position);

                // Checking that the card gives the expected points
                pointsExpected = card.getPoints();
                assertEquals(pointsExpected, pointsGiven.get());
            }

            // Checking that the cards are placed and the positions are not available
            placedCards.forEach(card -> assertTrue(playerField.containsCard(card)));
            placedPos.forEach(pos -> assertFalse(playerField.isAvailable(pos)));

        }
    }

    @Test
    void getAvailablePositions() {
        Set<Position> availablePos = playerField.getAvailablePositions();
        assertTrue(availablePos.contains(Position.of(0, 0)));
        assertEquals(1, availablePos.size());

        // Place a StarterCard on its retro as they always have all corners available
        assertDoesNotThrow(() -> playerField.placeStartingCard(starterDeck.draw().orElseThrow(), true));

        Set<Position> availablePosAfterStarter = playerField.getAvailablePositions();
        Stream.of(Corner.values())
              .map(corner -> PositionManager.getPositionIn(Position.of(0, 0), corner))
              .map(availablePosAfterStarter::contains)
              .forEach(Assertions::assertTrue);
    }

    @Test
    void isAvailable() {
        assertTrue(playerField.isAvailable(Position.of(0, 0)));
        assertFalse(playerField.isAvailable(Position.of(1, 1)));

        // Place a StarterCard on its retro as they always have all corners available
        assertDoesNotThrow(() -> playerField.placeStartingCard(starterDeck.draw().orElseThrow(), true));
        // Check that all corners are available
        Stream.of(Corner.values())
              .map(corner -> PositionManager.getPositionIn(Position.of(0, 0), corner))
              .forEach(position -> assertTrue(playerField.isAvailable(position)));
    }

    @Test
    void getNumberOf() {
        Map<Item, Integer> itemCount = new HashMap<>(16);
        Stream.concat(Stream.of(Color.values()),
                      Stream.of(Symbol.values()))
              .forEach(item -> {
                  assertEquals(0, playerField.getNumberOf(item));
                  itemCount.put(item, 0);
              });

        // Place a StarterCard on its retro
        StarterCard starterCard = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(starterCard, true));

        // Checking that colors have been updated and symbol are 0 because of StarterCard retro
        Stream.of(Color.values())
              .forEach(color -> {
                  assertEquals(1, playerField.getNumberOf(color));
                  itemCount.put(color, 1);
              });
        Stream.of(Symbol.values())
              .forEach(symbol -> assertEquals(0, playerField.getNumberOf(symbol)));

        // Place a ResourceCard on (1, 1)
        ResourceCard resourceCard = resourceDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> {
            playerField.place(resourceCard, Position.of(1, 1), false);
        });

        starterCard.getItemCorner(Corner.TOP_RX, true)
                   .getItem()
                   .ifPresent(item -> itemCount.merge(item, -1, Integer::sum));
        Stream.of(Corner.values())
              .map(corner -> resourceCard.getItemCorner(corner, false))
              .map(CornerContainer::getItem)
              .filter(Optional::isPresent)
              .map(Optional::get)
              .forEach(item -> itemCount.merge(item, 1, Integer::sum));

        Stream.concat(Stream.of(Color.values()), Stream.of(Symbol.values()))
              .forEach(item -> assertEquals(itemCount.get(item), playerField.getNumberOf(item)));

    }

    @Test
    void clearAll() {
        StarterCard card = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(card, true));
        playerField.clearAll();
        assertTrue(playerField.isAvailable(Position.of(0, 0)));
        assertFalse(playerField.containsCard(card));

        Stream.concat(Stream.of(Color.values()), Stream.of(Symbol.values()))
              .forEach(item -> assertEquals(0, playerField.getNumberOf(item)));
        Stream.of(Color.values())
              .forEach(color -> assertEquals(0, playerField.getNumberOfPositionedColor(color)));
    }

    @Test
    void containsCard() {
        StarterCard card = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(card, true));
        assertTrue(playerField.containsCard(card));

        ResourceCard resourceCard = resourceDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.place(resourceCard, Position.of(1, 1), true));
        assertTrue(playerField.containsCard(resourceCard));
        assertFalse(playerField.containsCard(resourceDeck.draw().orElseThrow()));
    }

    @Test
    void getNumberOfPositionedColor() {
        StarterCard card = starterDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.placeStartingCard(card, true));
        Stream.of(Color.values())
              .forEach(color -> assertEquals(0, playerField.getNumberOfPositionedColor(color)));

        ResourceCard resourceCard = resourceDeck.draw().orElseThrow();
        assertDoesNotThrow(() -> playerField.place(resourceCard,
                                                   Position.of(1, 1),
                                                   true));
        assertEquals(1, playerField.getNumberOfPositionedColor(resourceCard.getColor()));
    }

    @Test
    void isRequirementMet() {
        StarterCard card = starterDeck.draw().orElseThrow();
        assertTrue(playerField.isRequirementMet(card, false));
        assertTrue(playerField.isRequirementMet(resourceDeck.draw().orElseThrow(), false));
        assertFalse(playerField.isRequirementMet(goldDeck.draw().orElseThrow(), false));
    }
}