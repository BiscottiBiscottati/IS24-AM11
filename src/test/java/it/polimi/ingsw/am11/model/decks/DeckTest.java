package it.polimi.ingsw.am11.model.decks;

import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.persistence.DeckMemento;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeckTest {

    static Deck<ResourceCard> resourceDeck;
    static Deck<GoldCard> goldDeck;

    @BeforeAll
    static void beforeAll() {
        resourceDeck = ResourceDeckFactory.createDeck();
        goldDeck = GoldDeckFactory.createDeck();
    }

    @BeforeEach
    void setUp() {
        resourceDeck.reset();
        goldDeck.reset();
    }

    @Test
    void shuffle() {
        assertEquals(resourceDeck, resourceDeck.shuffle());
        assertEquals(goldDeck, goldDeck.shuffle());
    }

    @Test
    void draw() {
        int remainingCards = resourceDeck.getRemainingCards();
        assertNotNull(resourceDeck.draw());
        assertEquals(remainingCards - 1, resourceDeck.getRemainingCards());

        remainingCards = goldDeck.getRemainingCards();
        assertNotNull(goldDeck.draw());
        assertEquals(remainingCards - 1, goldDeck.getRemainingCards());
    }

    @Test
    void reset() {
        resourceDeck.draw();
        resourceDeck.reset();
        assertEquals(40, resourceDeck.getRemainingCards());

        goldDeck.draw();
        goldDeck.reset();
        assertEquals(40, goldDeck.getRemainingCards());
    }

    @Test
    void save() {
        int numOfCards = resourceDeck.getRemainingCards();
        DeckMemento memento = resourceDeck.save();
        assertEquals(numOfCards, memento.cards().size());

        for (int i = 0; i < numOfCards; i++) {
            assertEquals(resourceDeck.draw().orElseThrow().getId(),
                         memento.cards().get(i));
        }
    }

    @Test
    void load() {
        DeckMemento memento = resourceDeck.save();

        for (int i = 0; i < 6; i++) {
            resourceDeck.draw();
        }

        resourceDeck.load(memento);
        assertEquals(40, resourceDeck.getRemainingCards());

        for (int i = 0; i < 40; i++) {
            assertEquals(memento.cards().get(i),
                         resourceDeck.draw().orElseThrow().getId());
        }
    }
}