package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExposedItemManagerTest {

    static Deck<ResourceCard> resourceDeck;
    static Deck<GoldCard> goldDeck;
    ExposedItemManager itemManager;

    @BeforeAll
    static void beforeAll() {
        resourceDeck = ResourceDeckFactory.createDeck();
        goldDeck = GoldDeckFactory.createDeck();
    }

    @BeforeEach
    void setUp() {
        itemManager = new ExposedItemManager();
        resourceDeck.reset();
        goldDeck.reset();
    }

    @Test
    void reset() {
        itemManager.addToExposed(Color.RED);
        itemManager.addToExposed(Symbol.FEATHER);
        itemManager.addCardColor(Color.RED);
        itemManager.reset();
        Stream.concat(Stream.of(Color.values()), Stream.of(Symbol.values()))
              .forEach(item -> assertEquals(0, itemManager.getExposedItem(item)));
        Stream.of(Color.values())
              .forEach(color -> assertEquals(0, itemManager.getPlacedCardOf(color)));
    }

    @Test
    void addToExposed() {
        itemManager.addToExposed(Color.RED);
        itemManager.reset();
        assertEquals(0, itemManager.getExposedItem(Color.RED));
        itemManager.addToExposed(Color.RED);
        assertEquals(1, itemManager.getExposedItem(Color.RED));
        itemManager.addToExposed(Symbol.FEATHER);
        assertEquals(1, itemManager.getExposedItem(Symbol.FEATHER));

        Stream.concat(Stream.of(Color.values()), Stream.of(Symbol.values()))
              .filter(item -> item != Color.RED && item != Symbol.FEATHER)
              .forEach(item -> assertEquals(0, itemManager.getExposedItem(item)));

        itemManager.addToExposed(Color.RED);
        assertEquals(2, itemManager.getExposedItem(Color.RED));
    }

    @Test
    void subToExposed() {
        itemManager.addToExposed(Color.RED);
        itemManager.addToExposed(Color.RED);
        itemManager.addToExposed(Symbol.FEATHER);
        itemManager.subToExposed(Color.RED);
        assertEquals(1, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Color.RED);
        assertEquals(0, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Color.RED);
        assertEquals(0, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Symbol.FEATHER);
        assertEquals(0, itemManager.getExposedItem(Symbol.FEATHER));
    }

    @Test
    void addCardColor() {
        itemManager.addCardColor(Color.RED);
        assertEquals(1, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(2, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(3, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(4, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(5, itemManager.getPlacedCardOf(Color.RED));
        itemManager.reset();
        assertEquals(0, itemManager.getPlacedCardOf(Color.RED));
    }

    @Test
    void addExposedItemOn() {
        Map<Item, Integer> itemCount = new HashMap<>(20);
        Stream.concat(Stream.of(Color.values()), Stream.of(Symbol.values()))
              .forEach(item -> itemCount.put(item, 0));
        Map<Color, Integer> cardColorCount = new EnumMap<>(Color.class);
        Stream.of(Color.values())
              .forEach(color -> cardColorCount.put(color, 0));
        for (int i = 0; i < 40; i++) {
            ResourceCard card = resourceDeck.draw().orElseThrow();

            Stream.of(Corner.values())
                  .map(corner -> card.getItemCorner(corner, false).getItem())
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .forEach(item -> itemCount.merge(item, 1, Integer::sum));

            itemManager.addExposedItemOn(card, false);
            itemCount.forEach((item, count) -> assertEquals(count, itemManager.getExposedItem(item)));

            cardColorCount.merge(card.getColor(), 1, Integer::sum);

            itemManager.addCardColor(card.getColor());
            cardColorCount.forEach((color, count) -> assertEquals(count, itemManager.getPlacedCardOf(color)));
        }
    }

    @Test
    void getExposedItem() {
        itemManager.addToExposed(Color.RED);
        assertEquals(1, itemManager.getExposedItem(Color.RED));
        itemManager.addToExposed(Color.RED);
        assertEquals(2, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Color.RED);
        assertEquals(1, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Color.RED);
        assertEquals(0, itemManager.getExposedItem(Color.RED));
        itemManager.subToExposed(Color.RED);
        assertEquals(0, itemManager.getExposedItem(Color.RED));
    }

    @Test
    void getPlacedCardOf() {
        itemManager.addCardColor(Color.RED);
        assertEquals(1, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(2, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(3, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(4, itemManager.getPlacedCardOf(Color.RED));
        itemManager.addCardColor(Color.RED);
        assertEquals(5, itemManager.getPlacedCardOf(Color.RED));
        itemManager.reset();
        assertEquals(0, itemManager.getPlacedCardOf(Color.RED));
    }

    @Test
    void getPlacedCardColors() {

    }

    @Test
    void isRequirementsMet() {
        GoldCard card = goldDeck.draw().orElseThrow();
        assertFalse(itemManager.isRequirementsMet(card, false));
        assertTrue(itemManager.isRequirementsMet(card, true));

        Map<Color, Integer> requirements = card.getPlacingRequirements();
        requirements.forEach((color, count) -> {
            for (int i = 0; i < count; i++) {
                itemManager.addToExposed(color);
            }
        });

        assertTrue(itemManager.isRequirementsMet(card, false));
        assertTrue(itemManager.isRequirementsMet(card, true));
    }
}