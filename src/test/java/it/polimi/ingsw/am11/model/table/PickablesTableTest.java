package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPickActionException;
import it.polimi.ingsw.am11.model.utils.memento.PickablesTableMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType.GOLD;
import static it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType.RESOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class PickablesTableTest {
    static final int numOfObjectives = 2;
    static final int numOfShownPerType = 2;
    static Deck<ResourceCard> resourceCardDeck;
    static Deck<GoldCard> goldCardDeck;
    static Deck<ObjectiveCard> objectiveCardDeck;
    static Deck<StarterCard> starterCardDeck;
    PickablesTable pickablesTable;

    @BeforeAll
    static void beforeAll() {
        resourceCardDeck = ResourceDeckFactory.createDeck();

        goldCardDeck = GoldDeckFactory.createDeck();
        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
        starterCardDeck = StarterDeckFactory.createDeck();

        PickablesTable.setNumOfCommonObjectives(numOfObjectives);
        PickablesTable.setNumOfShownPerType(numOfShownPerType);

    }

    @BeforeEach
    public void setUp() {
        pickablesTable = new PickablesTable(new GameListenerSupport());
        pickablesTable.initialize();
        resourceCardDeck.reset();

        goldCardDeck.reset();
        objectiveCardDeck.reset();
        starterCardDeck.reset();

    }

    @Test
    public void getPlayableByID() {
        ResourceCard card = resourceCardDeck.draw().orElseThrow();
        int resourceId = card.getId();
        Optional<PlayableCard> playableCard = CardDecoder.decodePlayableCard(resourceId);

        Assertions.assertNotNull(playableCard);
        Assertions.assertTrue(playableCard.isPresent());

        PlayableCard playable = playableCard.get();
        assertEquals(resourceId, playable.getId());
        assertEquals(card, playable);
        assertEquals(Optional.of(card), playableCard);
        assertEquals(ResourceCard.class, playable.getClass());

        GoldCard goldCard = goldCardDeck.draw().orElseThrow();
        playableCard = CardDecoder.decodePlayableCard(goldCard.getId());

        Assertions.assertNotNull(playableCard);
        Assertions.assertTrue(playableCard.isPresent());

        assertEquals(Optional.of(goldCard), playableCard);

        StarterCard starterCard = starterCardDeck.draw().orElseThrow();
        playableCard = CardDecoder.decodePlayableCard(starterCard.getId());

        assertNotNull(playableCard);
        assertTrue(playableCard.isEmpty());
    }

    @Test
    public void initialize() {
        pickablesTable.initialize();
        assertEquals(numOfObjectives, pickablesTable.getCommonObjectives().size());
        assertEquals(numOfShownPerType, pickablesTable.getShownPlayable(GOLD).size());
        assertEquals(numOfShownPerType, pickablesTable.getShownPlayable(RESOURCE).size());
        Assertions.assertTrue(pickablesTable.getDeckTop(GOLD).isPresent());
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
            } catch (NoSuchElementException e) {
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
                      .forEach(id -> assertTrue(objectiveCardDeck.getCardById(id).isPresent()));
    }

    @Test
    void getDeckTop() {
        assertTrue(pickablesTable.getDeckTop(GOLD).isPresent());
        assertTrue(pickablesTable.getDeckTop(RESOURCE).isPresent());
        pickablesTable.getDeckTop(GOLD).ifPresent(goldColor -> {
            try {
                assertEquals(pickablesTable.drawPlayableFrom(GOLD).getColor(), goldColor);
            } catch (EmptyDeckException e) {
                fail(e);
            }
        });
        pickablesTable.getDeckTop(RESOURCE).ifPresent(resourceColor -> {
            try {
                assertEquals(pickablesTable.drawPlayableFrom(RESOURCE).getColor(), resourceColor);
            } catch (EmptyDeckException e) {
                fail(e);
            }
        });


        int remainingCards = pickablesTable.getRemainingDeckOf(GOLD);
        for (int i = 0; i < remainingCards; i++) {
            assertTrue(pickablesTable.getDeckTop(GOLD).isPresent());
            try {
                pickablesTable.drawPlayableFrom(GOLD);
            } catch (EmptyDeckException e) {
                fail(e);
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

        Stream.concat(pickablesTable.getShownPlayable(GOLD).stream(),
                      pickablesTable.getShownPlayable(RESOURCE).stream())
              .map(PlayableCard::getId)
              .forEach(id -> {
                  try {
                      assertNotNull(pickablesTable.pickPlayableVisible(id));
                  } catch (IllegalPickActionException e) {
                      fail(e);
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
            fail(e);
        }
        assertEquals(numberInDeck - 1,
                     pickablesTable.getRemainingDeckOf(GOLD));
    }

    @Test
    void save() {
        Set<ObjectiveCard> objCards = pickablesTable.getCommonObjectives();
        Map<PlayableCardType, Set<Integer>> shownCards =
                Stream.of(PlayableCardType.values())
                      .collect(Collectors.toMap(
                              Function.identity(),
                              type -> pickablesTable.getShownPlayable(type)
                                                    .stream()
                                                    .map(PlayableCard::getId)
                                                    .collect(Collectors.toSet())));

        PickablesTableMemento memento = pickablesTable.save();

        assertEquals(objCards.size(), memento.commonObjs().size());
        assertEquals(objCards.stream().map(ObjectiveCard::getId).collect(Collectors.toSet()),
                     memento.commonObjs());
        assertEquals(shownCards.size(), memento.shownPlayable().size());
        assertEquals(shownCards, memento.shownPlayable());
    }

    @Test
    void load() {
        Set<ObjectiveCard> objCards = pickablesTable.getCommonObjectives();
        Map<PlayableCardType, Set<PlayableCard>> shownCards =
                Stream.of(PlayableCardType.values())
                      .collect(Collectors.toMap(
                              Function.identity(),
                              pickablesTable::getShownPlayable));

        PickablesTableMemento memento = pickablesTable.save();

        pickablesTable.hardReset();
        pickablesTable.initialize();

        pickablesTable.load(memento);

        assertEquals(objCards.size(), pickablesTable.getCommonObjectives().size());
        assertEquals(objCards, pickablesTable.getCommonObjectives());
        assertEquals(shownCards.size(), pickablesTable.getShownPlayable(GOLD).size());
        assertEquals(shownCards.get(GOLD),
                     pickablesTable.getShownPlayable(GOLD));
        assertEquals(shownCards.get(RESOURCE),
                     pickablesTable.getShownPlayable(RESOURCE));
    }
}
