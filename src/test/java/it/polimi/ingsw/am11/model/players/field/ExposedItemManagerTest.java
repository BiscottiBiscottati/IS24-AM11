package it.polimi.ingsw.am11.model.players.field;

import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.utils.memento.ItemManagerMemento;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        itemManager.addToExposed(GameColor.RED);
        itemManager.addToExposed(Symbol.FEATHER);
        itemManager.addCardColor(GameColor.RED);
        itemManager.reset();
        Stream.concat(Stream.of(GameColor.values()), Stream.of(Symbol.values()))
              .forEach(item -> assertEquals(0, itemManager.getExposedItem(item)));
        Stream.of(GameColor.values())
              .forEach(color -> assertEquals(0, itemManager.getPlacedCardOf(color)));
    }

    @Test
    void addToExposed() {
        itemManager.addToExposed(GameColor.RED);
        itemManager.reset();
        assertEquals(0, itemManager.getExposedItem(GameColor.RED));
        itemManager.addToExposed(GameColor.RED);
        assertEquals(1, itemManager.getExposedItem(GameColor.RED));
        itemManager.addToExposed(Symbol.FEATHER);
        assertEquals(1, itemManager.getExposedItem(Symbol.FEATHER));

        Stream.concat(Stream.of(GameColor.values()), Stream.of(Symbol.values()))
              .filter(item -> item != GameColor.RED && item != Symbol.FEATHER)
              .forEach(item -> assertEquals(0, itemManager.getExposedItem(item)));

        itemManager.addToExposed(GameColor.RED);
        assertEquals(2, itemManager.getExposedItem(GameColor.RED));
    }

    @Test
    void subToExposed() {
        itemManager.addToExposed(GameColor.RED);
        itemManager.addToExposed(GameColor.RED);
        itemManager.addToExposed(Symbol.FEATHER);
        itemManager.subToExposed(GameColor.RED);
        assertEquals(1, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(GameColor.RED);
        assertEquals(0, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(GameColor.RED);
        assertEquals(0, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(Symbol.FEATHER);
        assertEquals(0, itemManager.getExposedItem(Symbol.FEATHER));
    }

    @Test
    void addCardColor() {
        itemManager.addCardColor(GameColor.RED);
        assertEquals(1, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(2, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(3, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(4, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(5, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.reset();
        assertEquals(0, itemManager.getPlacedCardOf(GameColor.RED));
    }

    @Test
    void addExposedItemOn() {
        Map<Item, Integer> itemCount = new HashMap<>(20);
        Stream.concat(Stream.of(GameColor.values()), Stream.of(Symbol.values()))
              .forEach(item -> itemCount.put(item, 0));
        for (int i = 0; i < 40; i++) {
            ResourceCard card = resourceDeck.draw().orElseThrow();

            Stream.of(Corner.values())
                  .map(corner -> card.getItemCorner(corner, false).getItem())
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .forEach(item -> itemCount.merge(item, 1, Integer::sum));

            itemManager.addExposedItemOn(card, false);
            itemCount.forEach(
                    (item, count) -> assertEquals(count, itemManager.getExposedItem(item)));
        }

        resourceDeck.reset();

        for (int i = 0; i < 40; i++) {
            ResourceCard card = resourceDeck.draw().orElseThrow();

            Stream.of(Corner.values())
                  .map(corner -> card.getItemCorner(corner, true).getItem())
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .forEach(item -> itemCount.merge(item, 1, Integer::sum));

            card.getCenter(true)
                .forEach(color -> itemCount.merge(color, 1, Integer::sum));

            itemManager.addExposedItemOn(card, true);
            itemCount.forEach(
                    (item, count) -> assertEquals(count, itemManager.getExposedItem(item)));
        }
    }

    @Test
    void getExposedItem() {
        itemManager.addToExposed(GameColor.RED);
        assertEquals(1, itemManager.getExposedItem(GameColor.RED));
        itemManager.addToExposed(GameColor.RED);
        assertEquals(2, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(GameColor.RED);
        assertEquals(1, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(GameColor.RED);
        assertEquals(0, itemManager.getExposedItem(GameColor.RED));
        itemManager.subToExposed(GameColor.RED);
        assertEquals(0, itemManager.getExposedItem(GameColor.RED));
    }

    @Test
    void getPlacedCardOf() {
        itemManager.addCardColor(GameColor.RED);
        assertEquals(1, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(2, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(3, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(4, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.addCardColor(GameColor.RED);
        assertEquals(5, itemManager.getPlacedCardOf(GameColor.RED));
        itemManager.reset();
        assertEquals(0, itemManager.getPlacedCardOf(GameColor.RED));
    }

    @Test
    void getPlacedCardColors() {
        itemManager.addCardColor(GameColor.RED);
        itemManager.addCardColor(GameColor.RED);
        itemManager.addCardColor(GameColor.RED);
        itemManager.addCardColor(GameColor.RED);

        Map<GameColor, Integer> placedCardColors = itemManager.getPlacedCardColors();

        assertEquals(4, placedCardColors.get(GameColor.RED));

        Stream.of(GameColor.values())
              .filter(color -> color != GameColor.RED)
              .forEach(color -> assertEquals(0, placedCardColors.get(color)));
    }

    @Test
    void isRequirementsMet() {
        GoldCard card = goldDeck.draw().orElseThrow();
        assertFalse(itemManager.isRequirementsMet(card, false));
        assertTrue(itemManager.isRequirementsMet(card, true));

        Map<GameColor, Integer> requirements = card.getPlacingRequirements();
        requirements.forEach((color, count) -> {
            for (int i = 0; i < count; i++) {
                itemManager.addToExposed(color);
            }
        });

        assertTrue(itemManager.isRequirementsMet(card, false));
        assertTrue(itemManager.isRequirementsMet(card, true));
    }

    @Test
    void save() {
        itemManager.addToExposed(GameColor.RED);
        itemManager.addToExposed(GameColor.RED);
        itemManager.addToExposed(GameColor.BLUE);
        itemManager.addToExposed(Symbol.FEATHER);
        itemManager.addCardColor(GameColor.RED);

        ItemManagerMemento memento = itemManager.save();

        assertEquals(2, memento.exposedColors().get(GameColor.RED));
        assertEquals(1, memento.exposedColors().get(GameColor.BLUE));
        assertEquals(1, memento.exposedSymbols().get(Symbol.FEATHER));
        assertEquals(1, memento.placedCardColors().get(GameColor.RED));
    }

    @Test
    void load() {
        ItemManagerMemento memento = new ItemManagerMemento(
                Map.of(GameColor.RED, 2, GameColor.BLUE, 1),
                Map.of(Symbol.FEATHER, 1),
                Map.of(GameColor.RED, 1));
        itemManager.load(memento);

        assertEquals(2, itemManager.getExposedItem(GameColor.RED));
        assertEquals(1, itemManager.getExposedItem(GameColor.BLUE));
        assertEquals(1, itemManager.getExposedItem(Symbol.FEATHER));
        assertEquals(1, itemManager.getPlacedCardOf(GameColor.RED));
    }
}