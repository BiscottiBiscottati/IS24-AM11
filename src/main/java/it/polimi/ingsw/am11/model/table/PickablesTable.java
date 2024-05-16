package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPickActionException;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.table.CommonObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.DeckTopChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.ShownPlayableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PickablesTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PickablesTable.class);

    private static int numOfCommonObjectives;
    private static int numOfShownPerType;
    private static int numOfCandidatesObjectives;
    private final Deck<GoldCard> goldDeck;
    private final Deck<ResourceCard> resourceDeck;
    private final Deck<ObjectiveCard> objectiveDeck;
    private final Deck<StarterCard> starterDeck;
    private final Set<ObjectiveCard> commonObjectives;
    private final Set<GoldCard> shownGold;
    private final Set<ResourceCard> shownResources;
    private final GameListenerSupport pcs;

    public PickablesTable() {
        this.goldDeck = GoldDeckFactory.createDeck();
        this.resourceDeck = ResourceDeckFactory.createDeck();
        this.objectiveDeck = ObjectiveDeckFactory.createDeck();
        this.starterDeck = StarterDeckFactory.createDeck();

        this.commonObjectives = new HashSet<>(numOfCommonObjectives << 1);
        this.shownGold = new HashSet<>(numOfShownPerType << 1);
        this.shownResources = new HashSet<>(numOfShownPerType << 1);

        this.pcs = new GameListenerSupport();
    }

    public static void setNumOfCommonObjectives(int numOfCommonObjectives) {
        PickablesTable.numOfCommonObjectives = numOfCommonObjectives;
    }

    public static void setNumOfShownPerType(int numOfShownPerType) {
        PickablesTable.numOfShownPerType = numOfShownPerType;
    }

    public static void setNumOfCandidatesObjectives(int numOfCandidatesObjectives) {
        PickablesTable.numOfCandidatesObjectives = numOfCandidatesObjectives;
    }

    //region Getters

    public Set<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableSet(commonObjectives);
    }

    public Optional<PlayableCard> getPlayableByID(int id) {
        return resourceDeck.getCardById(id)
                           .map(PlayableCard.class::cast)
                           .or(() -> goldDeck.getCardById(id));
    }

    public Optional<StarterCard> getStarterByID(int id) {
        return starterDeck.getCardById(id);
    }

    public Optional<ObjectiveCard> getObjectiveByID(int id) {
        return objectiveDeck.getCardById(id);
    }

    public @NotNull PlayableCard drawPlayableFrom(@NotNull PlayableCardType type)
    throws EmptyDeckException {
        PlayableCard card = null;
        DeckTopChangeEvent event = null;
        switch (type) {
            case GOLD -> {
                card = goldDeck.draw().orElseThrow(
                        () -> new EmptyDeckException("Gold deck is empty!"));
                event = new DeckTopChangeEvent(
                        PlayableCardType.GOLD,
                        card.getColor(),
                        getDeckTop(PlayableCardType.GOLD).orElse(null));
            }
            case RESOURCE -> {
                card = resourceDeck.draw().orElseThrow(
                        () -> new EmptyDeckException("Resource deck is empty!"));
                event = new DeckTopChangeEvent(
                        PlayableCardType.RESOURCE,
                        card.getColor(),
                        getDeckTop(PlayableCardType.RESOURCE).orElse(null));
            }
        }

        LOGGER.info("Card color on top of the {} deck changed to: {}",
                    event.getCardType(),
                    event.getNewValue());

        pcs.fireEvent(event);
        return card;
    }

    public Optional<Color> getDeckTop(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.peekTop()
                                 .map(PlayableCard::getColor);
            case RESOURCE -> resourceDeck.peekTop()
                                         .map(PlayableCard::getColor);
        };
    }

    public StarterCard pickStarterCard()
    throws EmptyDeckException {
        return starterDeck.draw()
                          .orElseThrow(() -> new EmptyDeckException("Starter deck is empty!"));
    }

    public Set<ObjectiveCard> pickObjectiveCandidates() throws EmptyDeckException {
        Set<ObjectiveCard> objs = new HashSet<>(numOfCandidatesObjectives << 1);
        for (int i = 0; i < numOfCandidatesObjectives; i++) {
            objs.add(pickObjectiveCard());
        }
        return Set.copyOf(objs);
    }

    public ObjectiveCard pickObjectiveCard() throws EmptyDeckException {
        return objectiveDeck.draw()
                            .orElseThrow(() -> new EmptyDeckException("Objective deck is empty!"));

    }

    public void initialize() {
        commonObjectives.clear();
        shownGold.clear();
        shownResources.clear();
        resetDecks();
        shuffleDecks();
        try {
            pickCommonObjectives();
        } catch (EmptyDeckException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < numOfShownPerType; i++) {
            goldDeck.draw()
                    .ifPresent(goldCard -> {
                        shownGold.add(goldCard);

                        LOGGER.info("Gold card with ID {} is shown on the table",
                                    goldCard.getId());

                        pcs.fireEvent(new ShownPlayableEvent(
                                PlayableCardType.GOLD,
                                null,
                                goldCard.getId()
                        ));
                    });
            resourceDeck.draw()
                        .ifPresent(resourceCard -> {
                            shownResources.add(resourceCard);

                            LOGGER.info("Resource card with ID {} is shown on the table",
                                        resourceCard.getId());

                            pcs.fireEvent(new ShownPlayableEvent(
                                    PlayableCardType.RESOURCE,
                                    null,
                                    resourceCard.getId()
                            ));
                        });
        }
    }

    private void resetDecks() {
        goldDeck.reset();
        resourceDeck.reset();
        objectiveDeck.reset();
        starterDeck.reset();

        Stream.of(PlayableCardType.values())
              .forEach(type -> pcs.fireEvent(new DeckTopChangeEvent(
                      type,
                      null,
                      getDeckTop(type).orElse(null)
              )));
    }

    private void shuffleDecks() {

        LOGGER.debug("Shuffling decks...");

        goldDeck.shuffle();
        resourceDeck.shuffle();
        objectiveDeck.shuffle();
        starterDeck.shuffle();

        Stream.of(PlayableCardType.values())
              .forEach(type -> pcs.fireEvent(new DeckTopChangeEvent(
                      type,
                      null,
                      getDeckTop(type).orElse(null)
              )));
    }

    public void pickCommonObjectives() throws EmptyDeckException {
        for (int i = 0; i < numOfCommonObjectives; i++) {
            commonObjectives.add(pickObjectiveCard());
        }

        Set<Integer> objsID = commonObjectives.stream()
                                              .map(CardIdentity::getId)
                                              .collect(Collectors.toSet());

        LOGGER.info("Common objectives are picked: {}", objsID);

        pcs.fireEvent(new CommonObjectiveChangeEvent(null,
                                                     objsID));
    }

    public PlayableCard pickPlayableVisible(int cardID) throws IllegalPickActionException {
        return Stream.concat(shownGold.stream(), shownResources.stream())
                     .filter(card -> card.getId() == cardID)
                     .findFirst()
                     .map(this::removePlayable)
                     .orElseThrow(() -> new IllegalPickActionException(
                             "Card with ID " + cardID + " is not visible!"));
    }

    private @Nullable PlayableCard removePlayable(@NotNull PlayableCard playableCard) {
        switch (playableCard) {
            case GoldCard goldCard -> {

                if (! shownGold.remove(goldCard)) return null;

                Optional<GoldCard> gold = goldDeck.draw();
                gold.ifPresent(shownGold::add);

                LOGGER.info("Shown gold card with ID {} is substituted with {}",
                            goldCard.getId(),
                            gold.map(PlayableCard::getId).orElse(null));

                pcs.fireEvent(new ShownPlayableEvent(
                        PlayableCardType.GOLD,
                        goldCard.getId(),
                        gold.map(PlayableCard::getId).orElse(null)
                ));

                LOGGER.info("Gold deck top color changed to: {}",
                            getDeckTop(PlayableCardType.GOLD).orElse(null));

                pcs.fireEvent(new DeckTopChangeEvent(
                        PlayableCardType.GOLD,
                        gold.map(PlayableCard::getColor).orElse(null),
                        getDeckTop(PlayableCardType.GOLD).orElse(null)
                ));
            }
            case ResourceCard resourceCard -> {

                if (! shownResources.remove(resourceCard)) return null;

                Optional<ResourceCard> resource = resourceDeck.draw();
                resource.ifPresent(shownResources::add);

                LOGGER.info("Shown resource card with ID {} is substituted with {}",
                            resourceCard.getId(),
                            resource.map(PlayableCard::getId).orElse(null));

                pcs.fireEvent(new ShownPlayableEvent(
                        PlayableCardType.RESOURCE,
                        resourceCard.getId(),
                        resource.map(PlayableCard::getId).orElse(null)
                ));

                LOGGER.info("Resource deck top color changed to: {}",
                            getDeckTop(PlayableCardType.RESOURCE).orElse(null));

                pcs.fireEvent(new DeckTopChangeEvent(
                        PlayableCardType.RESOURCE,
                        resource.map(PlayableCard::getColor).orElse(null),
                        getDeckTop(PlayableCardType.RESOURCE).orElse(null)
                ));
            }
        }
        return playableCard;
    }

    public Set<PlayableCard> getShownPlayable(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> Set.copyOf(shownGold);
            case RESOURCE -> Set.copyOf(shownResources);
        };
    }

    public int getRemainingDeckOf(@NotNull PlayableCardType type) {
        return switch (type) {
            case GOLD -> goldDeck.getRemainingCards();
            case RESOURCE -> resourceDeck.getRemainingCards();
        };
    }

    public void addListener(TableListener listener) {
        pcs.addListener(listener);
    }

    public void removeListener(TableListener listener) {
        pcs.removeListener(listener);
    }

}
