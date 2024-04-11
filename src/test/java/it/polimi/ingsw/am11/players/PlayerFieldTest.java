package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.field.PlayerField;
import it.polimi.ingsw.am11.players.field.PositionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class PlayerFieldTest {
    static Deck<ResourceCard> resourceDeck;
    static Deck<StarterCard> starterDeck;
    static Random gen;
    PlayerField playerField;

    @BeforeAll
    static void beforeAll() {
        resourceDeck = ResourceDeckFactory.createDeck();
        starterDeck = StarterDeckFactory.createDeck();
        gen = new Random();
    }

    @BeforeEach
    void setUp() {
        playerField = new PlayerField();
        resourceDeck.reset();
        starterDeck.reset();
    }

    @Test
    void getPositionIn() {
        Position position = Position.of(0, 0);
        Assertions.assertEquals(
                Position.of(1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_RX));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_LX));
        Assertions.assertEquals(
                Position.of(1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_RX));
        Assertions.assertEquals(
                Position.of(-1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_LX));

    }

    @Test
    void getMovementOfPositions() {
        Position position = Position.of(0, 0);
        Assertions.assertEquals(
                Position.of(1, 1),
                PositionManager.getMovementOfPositions(position, Collections.singletonList(Corner.TOP_RX)));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(0, 2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_RX, Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(2, -2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.DOWN_RX, Corner.DOWN_RX)));
    }

    @Test
    void getCornerFromPositions() {
        Position firstPos = Position.of(0, 0);
        Position secondPos = Position.of(-1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_LX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_RX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, -1);
        Assertions.assertEquals(Optional.of(Corner.DOWN_RX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(3, 3);
        secondPos = Position.of(2, 2);
        Assertions.assertEquals(Optional.of(Corner.DOWN_LX),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 0);
        Assertions.assertEquals(Optional.empty(),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 1);
        Assertions.assertEquals(Optional.empty(),
                                PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        for (int i = 0; i < 100; i++) {
            secondPos = Position.of(gen.nextInt(2, 100), gen.nextInt(2, 100));
            Assertions.assertEquals(Optional.empty(),
                                    PositionManager.getCornerFromPositions(firstPos, secondPos));

            secondPos = Position.of(gen.nextInt(-100, -2), gen.nextInt(-100, -2));
            Assertions.assertEquals(Optional.empty(),
                                    PositionManager.getCornerFromPositions(firstPos, secondPos));
        }
    }

    @Test
    void getCardsPositioned() {
        Map<Position, CardContainer> positionedCards = playerField.getCardsPositioned();
        Assertions.assertTrue(positionedCards.isEmpty());

        StarterCard starter = starterDeck.draw();
        Assertions.assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        positionedCards = playerField.getCardsPositioned();
        Assertions.assertEquals(1, positionedCards.size());
        Assertions.assertTrue(positionedCards.containsKey(Position.of(0, 0)));
        Assertions.assertTrue(playerField.containsCard(starter));

        ResourceCard resource = resourceDeck.draw();
        Assertions.assertDoesNotThrow(() -> playerField.place(resource, Position.of(1, 1), false));
        positionedCards = playerField.getCardsPositioned();
        Assertions.assertEquals(2, positionedCards.size());
        Assertions.assertTrue(positionedCards.containsKey(Position.of(1, 1)));
        Assertions.assertTrue(positionedCards.containsKey(Position.of(0, 0)));
        Assertions.assertTrue(playerField.containsCard(resource));
        Assertions.assertTrue(playerField.containsCard(starter));
    }

    @Test
    void getPlacedCardColours() {
        playerField.getPlacedCardColours()
                   .forEach((color, integer) -> Assertions.assertEquals(0, integer));

        StarterCard starter = starterDeck.draw();
        Assertions.assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        playerField.getPlacedCardColours()
                   .forEach((color, integer) -> Assertions.assertEquals(0, integer));

        ResourceCard resourceCard = resourceDeck.draw();
        Color colorOfCard = resourceCard.getColor();
        Assertions.assertDoesNotThrow(() -> playerField.place(resourceCard, Position.of(1, 1), false));
        Assertions.assertEquals(1, playerField.getPlacedCardColours().get(colorOfCard));
    }

    @Test
    void placeStartingCard() {
        StarterCard starterCard = starterDeck.draw();
        AtomicInteger actual = new AtomicInteger();

        // Testing placing a StarterCard on its retro
        Assertions.assertDoesNotThrow(() -> actual.set(
                playerField.placeStartingCard(starterCard, true))
        );

        // Checking that the card gives 0 points
        Assertions.assertEquals(0, actual.get());

        // Checking that the card is placed and the position is not available
        Assertions.assertFalse(playerField.isAvailable(Position.of(0, 0)));
        Assertions.assertTrue(playerField.containsCard(starterCard));

        // Testing illegal positioning of another starter
        StarterCard secondStarter = starterDeck.draw();
        Assertions.assertThrows(IllegalCardPlacingException.class,
                                () -> playerField.placeStartingCard(secondStarter, false));
    }

    @Test
    void place() {
        Set<FieldCard> placedCards = new HashSet<>(40);
        Set<Position> placedPos = new HashSet<>(40);
        ResourceCard resourceCard = resourceDeck.draw();
        AtomicInteger pointsGiven = new AtomicInteger();
        int pointsExpected;

        // Testing placing a card on the starter position
        Assertions.assertThrows(IllegalCardPlacingException.class,
                                () -> pointsGiven.set(
                                        playerField.place(resourceCard,
                                                          Position.of(0, 0),
                                                          false)));

        // Checking that no points have been given
        Assertions.assertEquals(0, pointsGiven.get());

        // Placing a StarterCard on its retro
        StarterCard starter = starterDeck.draw();
        placedCards.add(starter);
        Assertions.assertDoesNotThrow(() -> playerField.placeStartingCard(starter, true));
        placedPos.add(Position.of(0, 0));

        // Placing a ResourceCard on (1, 1)
        Assertions.assertDoesNotThrow(() -> pointsGiven.set(
                playerField.place(resourceCard, Position.of(1, 1), false))
        );
        placedCards.add(resourceCard);
        placedPos.add(Position.of(1, 1));

        // Checking that the card gives the expected points
        pointsExpected = resourceCard.getPoints();
        Assertions.assertEquals(pointsExpected, pointsGiven.get());

        // Checking that the cards are placed and the positions are not available
        placedPos.forEach(pos -> Assertions.assertFalse(playerField.isAvailable(pos)));
        placedCards.forEach(card -> Assertions.assertTrue(playerField.containsCard(card)));

        // Testing illegal positioning to same position
        Assertions.assertThrows(IllegalCardPlacingException.class,
                                () -> playerField.place(resourceCard,
                                                        Position.of(1, 1),
                                                        false));

        // Testing illegal positioning to starter position
        Assertions.assertThrows(IllegalCardPlacingException.class,
                                () -> playerField.place(resourceCard,
                                                        Position.of(0, 0),
                                                        false));

        // Checking nothing has changed from the previous state
        placedCards.forEach(card -> Assertions.assertTrue(playerField.containsCard(card)));
        placedPos.forEach(pos -> Assertions.assertFalse(playerField.isAvailable(pos)));

        // Testing random placements on its retro
        for (int i = 0; i < 4; i++) {

            // Getting a random subset of available positions
            List<Position> posToPlace = playerField.getAvailablePositions()
                                                   .stream()
                                                   .filter(pos -> gen.nextBoolean())
                                                   .toList();
            // Placing the cards
            for (Position position : posToPlace) {
                ResourceCard card = resourceDeck.draw();
                placedCards.add(card);
                Assertions.assertDoesNotThrow(() -> pointsGiven.set(
                        playerField.place(card, position, true))
                );
                placedPos.add(position);

                // Checking that the card gives the expected points
                pointsExpected = card.getPoints();
                Assertions.assertEquals(pointsExpected, pointsGiven.get());
            }

            // Checking that the cards are placed and the positions are not available
            placedCards.forEach(card -> Assertions.assertTrue(playerField.containsCard(card)));
            placedPos.forEach(pos -> Assertions.assertFalse(playerField.isAvailable(pos)));

        }
    }

    @Test
    void getAvailablePositions() {
        Set<Position> availablePos = playerField.getAvailablePositions();
        Assertions.assertTrue(availablePos.contains(Position.of(0, 0)));
        Assertions.assertEquals(1, availablePos.size());

        // Place a StarterCard on its retro as they always have all corners available
        Assertions.assertDoesNotThrow(() -> {
            playerField.placeStartingCard(starterDeck.draw(), true);
        });

        Set<Position> availablePosAfterStarter = playerField.getAvailablePositions();
        Stream.of(Corner.values())
              .map(corner -> PositionManager.getPositionIn(Position.of(0, 0), corner))
              .map(availablePosAfterStarter::contains)
              .forEach(Assertions::assertTrue);
    }

    @Test
    void isAvailable() {
        Assertions.assertTrue(playerField.isAvailable(Position.of(0, 0)));
        Assertions.assertFalse(playerField.isAvailable(Position.of(1, 1)));

        // Place a StarterCard on its retro as they always have all corners available
        Assertions.assertDoesNotThrow(() -> {
            playerField.placeStartingCard(starterDeck.draw(), true);
        });
        // Check that all corners are available
        Stream.of(Corner.values())
              .map(corner -> PositionManager.getPositionIn(Position.of(0, 0), corner))
              .forEach(position -> Assertions.assertTrue(playerField.isAvailable(position)));
    }

    @Test
    void getNumberOf() {
        Stream.concat(Stream.of(Color.values()),
                      Stream.of(Symbol.values()))
              .forEach(item -> Assertions.assertEquals(0, playerField.getNumberOf(item)));

        // Place a StarterCard on its retro
        StarterCard starterCard = starterDeck.draw();
        Assertions.assertDoesNotThrow(() -> {
            playerField.placeStartingCard(starterCard, true);
        });

        // Checking that colors have been updated and symbol are 0 because of StarterCard retro
        Stream.of(Color.values())
              .forEach(color -> Assertions.assertEquals(1, playerField.getNumberOf(color)));
        Stream.of(Symbol.values())
              .forEach(symbol -> Assertions.assertEquals(0, playerField.getNumberOf(symbol)));

        // Place a ResourceCard on (1, 1)
        ResourceCard resourceCard = resourceDeck.draw();
        Assertions.assertDoesNotThrow(() -> {
            playerField.place(resourceCard, Position.of(1, 1), false);
        });

        // TODO to complete

    }
}