package it.polimi.ingsw.am11.model.players;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.model.exceptions.MaxHandSizeException;
import it.polimi.ingsw.am11.model.exceptions.NotInHandException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.persistence.PersonalSpaceMemento;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PersonalSpaceTest {
    static Deck<StarterCard> starterDeck;
    static Deck<ResourceCard> resourceDeck;
    static Deck<ObjectiveCard> objectiveDeck;

    @BeforeAll
    static void beforeAll() {
        starterDeck = StarterDeckFactory.createDeck();
        resourceDeck = ResourceDeckFactory.createDeck();
        objectiveDeck = ObjectiveDeckFactory.createDeck();
    }

    @BeforeEach
    void setUp() {
        starterDeck.reset();
        resourceDeck.reset();
        objectiveDeck.reset();
        PersonalSpace.setMaxObjectives(1);
        PersonalSpace.setMaxSizeofHand(3);
    }

    @Test
    void getPlayerHand() throws IllegalCardBuildException, MaxHandSizeException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertNotNull(personalSpace.getPlayerHand());

        PlayableCard.Builder builder = new ResourceCard.Builder(1, 0, Color.BLUE);
        PlayableCard card = builder.build();
        personalSpace.addCardToHand(card);
        assertEquals(1, personalSpace.getPlayerHand().size());

        PlayableCard.Builder builder2 = new ResourceCard.Builder(2, 0, Color.BLUE);
        PlayableCard card2 = builder2.build();
        personalSpace.addCardToHand(card2);

        assertEquals(2, personalSpace.getPlayerHand().size());
        assertTrue(personalSpace.getPlayerHand().contains(card));
        assertTrue(personalSpace.getPlayerHand().contains(card2));
    }

    @Test
    void getPlayerObjective()
    throws IllegalArgumentException, IllegalCardBuildException, IllegalPlayerSpaceActionException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertNotNull(personalSpace.getPlayerObjective());
        assertEquals(0, personalSpace.getPlayerObjective().size());

        ColorCollectCard.Builder builder = new ColorCollectCard.Builder(1, 1);
        ObjectiveCard card = builder.build();
        personalSpace.addObjective(card);
        assertEquals(1, personalSpace.getPlayerObjective().size());
        assertTrue(personalSpace.getPlayerObjective().contains(card));
    }

    @Test
    void availableSpaceInHand() throws IllegalCardBuildException, MaxHandSizeException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertEquals(3, personalSpace.availableSpaceInHand());

        PlayableCard.Builder builder = new ResourceCard.Builder(1, 0, Color.BLUE);
        PlayableCard card = builder.build();
        personalSpace.addCardToHand(card);
        assertEquals(2, personalSpace.availableSpaceInHand());

        PlayableCard.Builder builder2 = new ResourceCard.Builder(2, 3, Color.BLUE);
        PlayableCard card2 = builder2.build();
        personalSpace.addCardToHand(card2);

        assertEquals(1, personalSpace.availableSpaceInHand());
    }

    @Test
    void addCardToHand() throws IllegalCardBuildException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertEquals(3, personalSpace.availableSpaceInHand());

        PlayableCard.Builder builder = new ResourceCard.Builder(1, 0, Color.BLUE);
        PlayableCard card = null;
        try {
            card = builder.build();
        } catch (IllegalCardBuildException e) {
            e.printStackTrace();
        }
        try {
            personalSpace.addCardToHand(card);
        } catch (MaxHandSizeException e) {
            e.printStackTrace();
        }
        assertEquals(2, personalSpace.availableSpaceInHand());
        assertEquals(card, personalSpace.getPlayerHand().iterator().next());
        assertEquals(1, personalSpace.getPlayerHand().size());

        PlayableCard.Builder builder2 = new ResourceCard.Builder(2, 0, Color.BLUE);
        PlayableCard card2 = null;
        try {
            card2 = builder2.build();
        } catch (IllegalCardBuildException e) {
            e.printStackTrace();
        }
        try {
            personalSpace.addCardToHand(card2);
        } catch (MaxHandSizeException e) {
            e.printStackTrace();
        }
        assertEquals(1, personalSpace.availableSpaceInHand());
        assertEquals(card2, personalSpace.getPlayerHand().stream().skip(1).findFirst().get());
        assertEquals(2, personalSpace.getPlayerHand().size());

    }

    @Test
    void pickCard() throws MaxHandSizeException, IllegalCardBuildException, NotInHandException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertNotNull(personalSpace.getPlayerHand());

        PlayableCard.Builder builder = new ResourceCard.Builder(1, 0, Color.BLUE);
        PlayableCard card = builder.build();
        personalSpace.addCardToHand(card);

        assertThrows(NotInHandException.class, () -> personalSpace.pickCard(2));
        personalSpace.pickCard(card.getId());
        assertEquals(0, personalSpace.getPlayerHand().size());
        assertThrows(NotInHandException.class, () -> personalSpace.pickCard(card.getId()));
    }

    @Test
    void addObjective() throws IllegalCardBuildException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        assertEquals(0, personalSpace.getPlayerObjective().size());

        ColorCollectCard.Builder builder = new ColorCollectCard.Builder(1, 1);
        ObjectiveCard card = builder.build();
        try {
            personalSpace.addObjective(card);
        } catch (IllegalPlayerSpaceActionException e) {
            e.printStackTrace();
        }
        assertEquals(1, personalSpace.getPlayerObjective().size());
        assertThrows(IllegalPlayerSpaceActionException.class,
                     () -> personalSpace.addObjective(card));
        assertEquals(card, personalSpace.getPlayerObjective().iterator().next());

    }

    @Test
    void clearAll()
    throws IllegalCardBuildException, MaxHandSizeException, IllegalPlayerSpaceActionException {

        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        PlayableCard.Builder builder = new ResourceCard.Builder(1, 0, Color.BLUE);
        PlayableCard card = builder.build();
        personalSpace.addCardToHand(card);

        ColorCollectCard.Builder builder2 = new ColorCollectCard.Builder(2, 1);
        ObjectiveCard card2 = builder2.build();
        personalSpace.addObjective(card2);

        personalSpace.clearAll();
        assertEquals(0, personalSpace.getPlayerHand().size());
        assertEquals(0, personalSpace.getPlayerObjective().size());
    }

    @Test
    void save() throws IllegalPlayerSpaceActionException, MaxHandSizeException {
        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        StarterCard starter = starterDeck.draw().orElseThrow();
        personalSpace.setStarterCard(starter);
        Set<ResourceCard> cards = IntStream.range(0, 3)
                                           .mapToObj(i -> resourceDeck.draw())
                                           .map(Optional::orElseThrow)
                                           .collect(Collectors.toSet());
        for (ResourceCard card : cards) {
            personalSpace.addCardToHand(card);
        }

        ObjectiveCard personal = objectiveDeck.draw().orElseThrow();
        personalSpace.addObjective(personal);

        ObjectiveCard candidate = objectiveDeck.draw().orElseThrow();
        personalSpace.setNewCandidateObjectives(candidate);

        PersonalSpaceMemento memento = personalSpace.save();

        assertEquals(3, memento.hand().size());
        assertEquals(starter.getId(), memento.starterCard());
        assertEquals(cards.stream().map(PlayableCard::getId).collect(Collectors.toSet()),
                     memento.hand());
        assertEquals(personal.getId(), memento.personalObjs().stream().findFirst().orElseThrow());
        assertEquals(candidate.getId(), memento.candidateObjs().stream().findFirst().orElseThrow());
    }

    @Test
    void load() throws IllegalPlayerSpaceActionException, MaxHandSizeException {
        Player player = new Player("test player", PlayerColor.BLUE);
        PersonalSpace personalSpace = player.space();
        StarterCard starter = starterDeck.draw().orElseThrow();
        personalSpace.setStarterCard(starter);
        Set<ResourceCard> cards = IntStream.range(0, 3)
                                           .mapToObj(i -> resourceDeck.draw())
                                           .map(Optional::orElseThrow)
                                           .collect(Collectors.toSet());
        for (ResourceCard card : cards) {
            personalSpace.addCardToHand(card);
        }

        ObjectiveCard personal = objectiveDeck.draw().orElseThrow();
        personalSpace.addObjective(personal);

        ObjectiveCard candidate = objectiveDeck.draw().orElseThrow();
        personalSpace.setNewCandidateObjectives(candidate);

        PersonalSpaceMemento memento = personalSpace.save();

        personalSpace.clearAll();
        personalSpace.load(memento);

        assertEquals(3, personalSpace.getPlayerHand().size());
        assertEquals(cards, personalSpace.getPlayerHand());
        assertEquals(starter, personalSpace.getStarterCard().orElseThrow());
        assertEquals(1, personalSpace.getPlayerObjective().size());
        assertEquals(personal, personalSpace.getPlayerObjective().iterator().next());
        assertEquals(1, personalSpace.getCandidateObjectives().size());
        assertEquals(candidate, personalSpace.getCandidateObjectives().iterator().next());
    }
}