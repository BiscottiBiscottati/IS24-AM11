package it.polimi.ingsw.am11.model.decks;

import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import org.junit.jupiter.api.BeforeAll;
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
}