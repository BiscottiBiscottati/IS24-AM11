package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType.GOLD;
import static it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType.RESOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class PickablesTableTest {
    static Deck<ResourceCard> resourceCardDeck;

    static Deck<GoldCard> goldCardDeck;
    //    static Deck<ObjectiveCard> objectiveCardDeck;
    static Deck<StarterCard> starterCardDeck;

    int numOfObjectives = 2;
    int numOfShownPerType = 2;
    PickablesTable pickablesTable;

    @BeforeAll
    static void beforeAll() {
        resourceCardDeck = ResourceDeckFactory.createDeck();

        goldCardDeck = GoldDeckFactory.createDeck();
//        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
        starterCardDeck = StarterDeckFactory.createDeck();

    }

    @BeforeEach
    public void setUp() {
        pickablesTable = new PickablesTable(numOfObjectives, numOfShownPerType);
        pickablesTable.initialize();
        resourceCardDeck.reset();

        goldCardDeck.reset();
//        objectiveCardDeck.reset();
//        starterCardDeck.reset();

    }

    @Test
    public void testGetPlayableByID() {
        ResourceCard card = resourceCardDeck.draw().orElseThrow();
        int resourceId = card.getId();
        Optional<PlayableCard> playableCard = pickablesTable.getPlayableByID(resourceId);

        Assertions.assertNotNull(playableCard);
        Assertions.assertTrue(playableCard.isPresent());

        PlayableCard playable = playableCard.get();
        assertEquals(resourceId, playable.getId());
        assertEquals(card, playable);
        assertEquals(Optional.of(card), playableCard);
        assertEquals(ResourceCard.class, playable.getClass());

        GoldCard goldCard = goldCardDeck.draw().orElseThrow();
        playableCard = pickablesTable.getPlayableByID(goldCard.getId());

        Assertions.assertNotNull(playableCard);
        Assertions.assertTrue(playableCard.isPresent());

        assertEquals(Optional.of(goldCard), playableCard);

        StarterCard starterCard = starterCardDeck.draw().orElseThrow();
        playableCard = pickablesTable.getPlayableByID(starterCard.getId());

        assertNotNull(playableCard);
        assertTrue(playableCard.isEmpty());
    }

    @Test
    public void testInitialize() {
        pickablesTable.initialize();
        assertEquals(numOfObjectives, pickablesTable.getCommonObjectives().size());
        assertEquals(numOfShownPerType, pickablesTable.getShownGold().size());
        assertEquals(numOfShownPerType, pickablesTable.getShownResources().size());
        Assertions.assertTrue(pickablesTable.getResourceDeckTop().isPresent());
        Assertions.assertTrue(pickablesTable.getGoldDeckTop().isPresent());
    }

    @Test
    public void testPickGoldVisible() throws IllegalPickActionException {
        // Get the list of visible gold cards
        List<PlayableCard> shownGold = pickablesTable.getShownGold();
        // Check that the list is not empty
        Assertions.assertFalse(shownGold.isEmpty());
        // Get the ID of the first card in the list
        int cardId = shownGold.getFirst().getId();
        PlayableCard card = pickablesTable.pickGoldVisible(cardId);
        assertEquals(numOfShownPerType, pickablesTable.getShownGold().size());
    }

    @Test
    public void testPickResourceVisible() throws IllegalPickActionException {
        // Get the list of visible resource cards
        List<PlayableCard> shownResources = pickablesTable.getShownResources();
        // Check that the list is not empty
        Assertions.assertFalse(shownResources.isEmpty());
        // Get the ID of the first card in the list
        int cardId = shownResources.getFirst().getId();
        PlayableCard card = pickablesTable.pickResourceVisible(cardId);
        assertEquals(numOfShownPerType, pickablesTable.getShownResources().size());
    }

    @Test
    public void testPickPlayableCardFrom() {
        // Pick cards from goldDeck until it's empty
        while (true) {
            try {
                PlayableCard goldCard = pickablesTable.pickPlayableCardFrom(GOLD);
                Assertions.assertNotNull(goldCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }

        // Pick cards from resourceDeck until it's empty
        while (true) {
            try {
                PlayableCard resourceCard = pickablesTable.pickPlayableCardFrom(RESOURCE);
                Assertions.assertNotNull(resourceCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }
    }

    @Test
    public void testPickStarterCard() {
        // Pick cards from starterCardDeck until it's empty
        while (true) {
            try {
                StarterCard starterCard = pickablesTable.pickStarterCard();
                Assertions.assertNotNull(starterCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }
    }

    @Test
    public void testPickObjectiveCard() {
        // Pick cards from objectiveCardDeck until it's empty
        while (true) {
            try {
                ObjectiveCard objectiveCard = pickablesTable.pickObjectiveCard();
                Assertions.assertNotNull(objectiveCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }
    }
}
