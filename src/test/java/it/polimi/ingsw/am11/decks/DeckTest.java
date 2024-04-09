package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(resourceDeck, resourceDeck.shuffle());
        Assertions.assertEquals(goldDeck, goldDeck.shuffle());
    }

    @Test
    void draw() {
        int remainingCards = resourceDeck.getRemainingCards();
        Assertions.assertNotNull(resourceDeck.draw());
        Assertions.assertEquals(remainingCards - 1, resourceDeck.getRemainingCards());

        remainingCards = goldDeck.getRemainingCards();
        Assertions.assertNotNull(goldDeck.draw());
        Assertions.assertEquals(remainingCards - 1, goldDeck.getRemainingCards());
    }

    @Test
    void reset() {
        resourceDeck.draw();
        resourceDeck.reset();
        Assertions.assertEquals(40, resourceDeck.getRemainingCards());

        goldDeck.draw();
        goldDeck.reset();
        Assertions.assertEquals(40, goldDeck.getRemainingCards());
    }
}