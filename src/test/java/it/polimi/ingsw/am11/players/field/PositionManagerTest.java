package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class PositionManagerTest {

    static Deck<StarterCard> starterDeck;
    Random gen;
    PositionManager positionManager;

    @BeforeAll
    static void beforeAll() {
        starterDeck = StarterDeckFactory.createDeck();
    }

    @BeforeEach
    void setUp() {
        gen = new Random();
        positionManager = new PositionManager();
        starterDeck.reset();
    }

    @Test
    void getPositionIn() {
        Position position = Position.of(0, 0);
        assertEquals(
                Position.of(1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_RX));
        assertEquals(
                Position.of(-1, 1),
                PositionManager.getPositionIn(position, Corner.TOP_LX));
        assertEquals(
                Position.of(1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_RX));
        assertEquals(
                Position.of(-1, -1),
                PositionManager.getPositionIn(position, Corner.DOWN_LX));

    }

    @Test
    void getMovementOfPositions() {
        Position position = Position.of(0, 0);
        assertEquals(
                Position.of(1, 1),
                PositionManager.getMovementOfPositions(position, Collections.singletonList(Corner.TOP_RX)));
        assertEquals(
                Position.of(-1, 1),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_LX)));
        assertEquals(
                Position.of(0, 2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.TOP_RX, Corner.TOP_LX)));
        assertEquals(
                Position.of(2, -2),
                PositionManager.getMovementOfPositions(position, List.of(Corner.DOWN_RX, Corner.DOWN_RX)));
    }

    @Test
    void getCornerFromPositions() {
        Position firstPos = Position.of(0, 0);
        Position secondPos = Position.of(-1, 1);
        assertEquals(Optional.of(Corner.TOP_LX),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, 1);
        assertEquals(Optional.of(Corner.TOP_RX),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(1, -1);
        assertEquals(Optional.of(Corner.DOWN_RX),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(3, 3);
        secondPos = Position.of(2, 2);
        assertEquals(Optional.of(Corner.DOWN_LX),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 0);
        assertEquals(Optional.empty(),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        secondPos = Position.of(0, 1);
        assertEquals(Optional.empty(),
                     PositionManager.getCornerFromPositions(firstPos, secondPos));

        firstPos = Position.of(0, 0);
        for (int i = 0; i < 100; i++) {
            secondPos = Position.of(gen.nextInt(2, 100), gen.nextInt(2, 100));
            assertEquals(Optional.empty(),
                         PositionManager.getCornerFromPositions(firstPos, secondPos));

            secondPos = Position.of(gen.nextInt(-100, -2), gen.nextInt(-100, -2));
            assertEquals(Optional.empty(),
                         PositionManager.getCornerFromPositions(firstPos, secondPos));
        }
    }

    @Test
    void reset() {
        positionManager.reset();
        assertEquals(1, positionManager.getAvailablePositions().size());
        assertEquals(0, positionManager.getCardsPositioned().size());
        assertTrue(positionManager.isAvailable(Position.of(0, 0)));
    }

    @Test
    void placeCard() {
        StarterCard card = starterDeck.draw().orElseThrow();
        AtomicReference<List<Item>> itemCovered = new AtomicReference<>();

        assertDoesNotThrow(() -> itemCovered.set(
                positionManager.placeCard(card, Position.of(0, 0), true)));
        assertTrue(itemCovered.get().isEmpty());

        assertThrows(IllegalCardPlacingException.class,
                     () -> positionManager.placeCard(card, Position.of(0, 0), false));

        try {
            itemCovered.set(
                    positionManager.placeCard(card, Position.of(1, 1), true)
            );
            assertEquals(List.of(card.getItemCorner(Corner.TOP_RX, true)), itemCovered.get());

            itemCovered.set(
                    positionManager.placeCard(card, Position.of(1, -1), true)
            );
            assertEquals(List.of(card.getItemCorner(Corner.DOWN_RX, true)), itemCovered.get());

            List<CornerContainer> itemsToBeCovered = new ArrayList<>(4);
            itemsToBeCovered.add(card.getItemCorner(Corner.DOWN_RX, true));
            itemsToBeCovered.add(card.getItemCorner(Corner.TOP_RX, true));

            itemCovered.set(
                    positionManager.placeCard(card, Position.of(2, 0), true)
            );
            assertEquals(Set.copyOf(itemsToBeCovered), Set.copyOf(itemCovered.get()));

        } catch (IllegalCardPlacingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCardsPositioned() {
    }

    @Test
    void getAvailablePositions() {
    }

    @Test
    void isAvailable() {
    }

    @Test
    void getCardIfExists() {
    }

    @Test
    void containsCard() {
    }
}