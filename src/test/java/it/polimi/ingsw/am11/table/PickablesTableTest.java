package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.playable.ResourceDeckFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class PickablesTableTest {
    static Deck<ResourceCard> resourceCardDeck;
    PickablesTable pickablesTable;

    @BeforeAll
    static void beforeAll() {
        resourceCardDeck = ResourceDeckFactory.createDeck();
    }

    @BeforeEach
    public void setUp() {
        pickablesTable = new PickablesTable(2, 2);
        pickablesTable.initialize();
        resourceCardDeck.reset();
        // Initialize the pickablesTable with some data if necessary
    }

    @Test
    public void getCommonObjectives() {
        List<ObjectiveCard> commonObjectives = pickablesTable.getCommonObjectives();
        Assertions.assertEquals(2, commonObjectives.size());
    }

    @Test
    public void getShownGold() {
        List<PlayableCard> shownGold = pickablesTable.getShownGold();
        Assertions.assertEquals(2, shownGold.size());
    }

    @Test
    public void getShownResources() {
        List<PlayableCard> shownResources = pickablesTable.getShownResources();
        Assertions.assertEquals(2, shownResources.size());
    }

    @Test
    public void getResourceDeckTop() {
        Optional<Color> resourceDeckTop = pickablesTable.getResourceDeckTop();
        Assertions.assertTrue(resourceDeckTop.isPresent());
    }

    @Test
    public void getGoldDeckTop() {
        Optional<Color> goldDeckTop = pickablesTable.getGoldDeckTop();
        Assertions.assertTrue(goldDeckTop.isPresent());
    }

    @Test
    public void getPlayableByID() {
        int resourceId = resourceCardDeck.draw().get().getId();
        Optional<PlayableCard> playableCard = pickablesTable.getPlayableByID(resourceId);

        Assertions.assertNotNull(playableCard);
        Assertions.assertTrue(playableCard.isPresent());

        PlayableCard card = playableCard.get();
        Assertions.assertEquals(resourceId, card.getId());
    }


}
