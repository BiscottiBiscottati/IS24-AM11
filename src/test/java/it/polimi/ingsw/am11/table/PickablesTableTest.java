package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.exceptions.IllegalPickActionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType.GOLD;
import static it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType.RESOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class PickablesTableTest {
    static Deck<ResourceCard> resourceCardDeck;

    static Deck<GoldCard> goldCardDeck;
    static Deck<ObjectiveCard> objectiveCardDeck;
    static Deck<StarterCard> starterCardDeck;

    static int numOfObjectives = 2;
    static int numOfShownPerType = 2;
    PickablesTable pickablesTable;

    @BeforeAll
    static void beforeAll() {
        resourceCardDeck = ResourceDeckFactory.createDeck();

        goldCardDeck = GoldDeckFactory.createDeck();
        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
        starterCardDeck = StarterDeckFactory.createDeck();

        PickablesTable.setConstants(numOfObjectives, numOfShownPerType);

    }

    @BeforeEach
    public void setUp() {
        pickablesTable = new PickablesTable();
        pickablesTable.initialize();
        resourceCardDeck.reset();

        goldCardDeck.reset();
//        objectiveCardDeck.reset();
//        starterCardDeck.reset();

    }

    @Test
    public void getPlayableByID() {
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
    public void initialize() {
        pickablesTable.initialize();
        assertEquals(numOfObjectives, pickablesTable.getCommonObjectives().size());
        assertEquals(numOfShownPerType << 1, pickablesTable.getShownPlayable().size());
        Assertions.assertTrue(pickablesTable.getDeckTop(RESOURCE).isPresent());
    }

    @Test
    public void drawPlayableFrom() {
        // Pick cards from goldDeck until it's empty
        while (true) {
            try {
                PlayableCard goldCard = pickablesTable.drawPlayableFrom(GOLD);
                Assertions.assertNotNull(goldCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }

        // Pick cards from resourceDeck until it's empty
        while (true) {
            try {
                PlayableCard resourceCard = pickablesTable.drawPlayableFrom(RESOURCE);
                Assertions.assertNotNull(resourceCard);
            } catch (EmptyDeckException e) {
                break; // Exit the loop when the deck is empty
            }
        }
    }

    @Test
    public void pickStarterCard() {
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
    public void pickObjectiveCard() {
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

    @Test
    void getCommonObjectives() {
        assertEquals(2, pickablesTable.getCommonObjectives().size());
        pickablesTable.getCommonObjectives()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .forEach(id -> {
                          assertTrue(objectiveCardDeck.getCardById(id).isPresent());
                      });
    }

    @Test
    void getDeckTop() {
        assertTrue(pickablesTable.getDeckTop(GOLD).isPresent());
        assertTrue(pickablesTable.getDeckTop(RESOURCE).isPresent());
        pickablesTable.getDeckTop(GOLD).ifPresent(goldColor -> {
            try {
                assertEquals(pickablesTable.drawPlayableFrom(GOLD).getColor(), goldColor);
            } catch (EmptyDeckException e) {
                throw new RuntimeException(e);
            }
        });
        pickablesTable.getDeckTop(RESOURCE).ifPresent(resourceColor -> {
            try {
                assertEquals(pickablesTable.drawPlayableFrom(RESOURCE).getColor(), resourceColor);
            } catch (EmptyDeckException e) {
                throw new RuntimeException(e);
            }
        });


        int remainingCards = pickablesTable.getRemainingDeckOf(GOLD);
        for (int i = 0; i < remainingCards; i++) {
            assertTrue(pickablesTable.getDeckTop(GOLD).isPresent());
            try {
                pickablesTable.drawPlayableFrom(GOLD);
            } catch (EmptyDeckException e) {
                throw new RuntimeException(e);
            }
        }
        assertTrue(pickablesTable.getDeckTop(GOLD).isEmpty());
    }

    @Test
    void getStarterByID() {
    }

    @Test
    void getObjectiveByID() {
    }

    @Test
    void resetDecks() {
    }

    @Test
    void shuffleDecks() {
    }

    @Test
    void pickCommonObjectives() {
    }

    @Test
    void pickPlayableVisible() {
        pickablesTable.getShownPlayable().stream()
                      .map(PlayableCard::getId)
                      .forEach(id -> {
                          try {
                              assertNotNull(pickablesTable.pickPlayableVisible(id));
                          } catch (IllegalPickActionException e) {
                              throw new RuntimeException(e);
                          }
                          assertThrows(IllegalPickActionException.class,
                                       () -> pickablesTable.pickPlayableVisible(id));
                      });

    }

    @Test
    void getShownPlayable() {
    }

    @Test
    void getRemainingDeckOf() {
        int numberInDeck = goldCardDeck.getRemainingCards() - numOfShownPerType;
        assertEquals(numberInDeck,
                     pickablesTable.getRemainingDeckOf(GOLD));
        try {
            pickablesTable.drawPlayableFrom(GOLD);
        } catch (EmptyDeckException e) {
            throw new RuntimeException(e);
        }
        assertEquals(numberInDeck - 1,
                     pickablesTable.getRemainingDeckOf(GOLD));
    }
}
