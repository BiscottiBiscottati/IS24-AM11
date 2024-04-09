package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.IllegalPositioningException;
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
    PlayerField playerField;

    @BeforeAll
    static void beforeAll() {
        resourceDeck = ResourceDeckFactory.createDeck();
        starterDeck = StarterDeckFactory.createDeck();
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
                PlayerField.getPositionIn(position, Corner.TOP_RX));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PlayerField.getPositionIn(position, Corner.TOP_LX));
        Assertions.assertEquals(
                Position.of(1, -1),
                PlayerField.getPositionIn(position, Corner.DOWN_RX));
        Assertions.assertEquals(
                Position.of(-1, -1),
                PlayerField.getPositionIn(position, Corner.DOWN_LX));

    }

    @Test
    void getMovementOfPositions() {
        Position position = Position.of(0, 0);
        Assertions.assertEquals(
                Position.of(1, 1),
                PlayerField.getMovementOfPositions(position, Collections.singletonList(Corner.TOP_RX)));
        Assertions.assertEquals(
                Position.of(-1, 1),
                PlayerField.getMovementOfPositions(position, List.of(Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(0, 2),
                PlayerField.getMovementOfPositions(position, List.of(Corner.TOP_RX, Corner.TOP_LX)));
        Assertions.assertEquals(
                Position.of(2, -2),
                PlayerField.getMovementOfPositions(position, List.of(Corner.DOWN_RX, Corner.DOWN_RX)));
    }

    @Test
    void getCornerFromPositions() {
        Position firstPos = Position.of(0, 0);
        Position secondPos = Position.of(-1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_LX),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, 1);
        Assertions.assertEquals(Optional.of(Corner.TOP_RX),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, -1);
        Assertions.assertEquals(Optional.of(Corner.DOWN_RX),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(3, 3);
        secondPos = Position.of(2, 2);
        Assertions.assertEquals(Optional.of(Corner.DOWN_LX),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 0);
        Assertions.assertEquals(Optional.empty(),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 1);
        Assertions.assertEquals(Optional.empty(),
                                PlayerField.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            secondPos = Position.of(random.nextInt(2, 100), random.nextInt(2, 100));
            Assertions.assertEquals(Optional.empty(),
                                    PlayerField.getCornerFromPositions(firstPos, secondPos));

            secondPos = Position.of(random.nextInt(-100, -2), random.nextInt(-100, -2));
        }
    }

    @Test
    void getCardsPositioned() {
        Map<Position, CardContainer> positionedCards = playerField.getCardsPositioned();
        Assertions.assertTrue(positionedCards.isEmpty());
    }

    @Test
    void getPlacedCardColours() {
        EnumMap<Color, Integer> placedCardColors = playerField.getPlacedCardColours();
        placedCardColors.forEach((color, integer) -> Assertions.assertEquals(0, integer));
    }

    @Test
    void placeStartingCard() {
        StarterCard starterCard = starterDeck.draw();
        AtomicInteger actual = new AtomicInteger();
        Assertions.assertDoesNotThrow(() -> {
            actual.set(playerField.placeStartingCard(starterCard, true));
        });
        Assertions.assertEquals(0, actual.get());

        Assertions.assertFalse(playerField.isAvailable(Position.of(0, 0)));
        StarterCard secondStarter = starterDeck.draw();
        Assertions.assertThrows(IllegalPositioningException.class,
                                () -> playerField.placeStartingCard(secondStarter, false));
    }

    @Test
    void place() {
        ResourceCard resourceCard = resourceDeck.draw();
        Assertions.assertThrows(IllegalPositioningException.class,
                                () -> playerField.place(resourceCard,
                                                        Position.of(0, 0),
                                                        false));

        Assertions.assertDoesNotThrow(() -> {
            playerField.placeStartingCard(starterDeck.draw(), true);
        });
        Assertions.assertDoesNotThrow(() -> {
            playerField.place(resourceCard, Position.of(1, 1), false);
        });
        Assertions.assertFalse(playerField.isAvailable(Position.of(1, 1)));
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
              .map(corner -> PlayerField.getPositionIn(Position.of(0, 0), corner))
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
              .map(corner -> PlayerField.getPositionIn(Position.of(0, 0), corner))
              .forEach(position -> Assertions.assertTrue(playerField.isAvailable(position)));
    }

    @Test
    void getNumberOf() {
        Stream.concat(Stream.of(Color.values()),
                      Stream.of(Symbol.values()))
              .forEach(item -> Assertions.assertEquals(0, playerField.getNumberOf(item)));
    }
}