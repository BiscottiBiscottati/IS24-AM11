package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPickActionException;
import it.polimi.ingsw.am11.model.utils.memento.PickablesTableMemento;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionTableMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.table.CommonObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.DeckTopChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.ShownPlayableEvent;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PickablesTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PickablesTable.class);

    private static int numOfCommonObjectives;
    private static int numOfShownPerType;
    private static int numOfCandidatesObjectives;
    private final @NotNull DeckManager deckManager;
    private final @NotNull Map<PlayableCardType, Set<PlayableCard>> shownPlayable;
    private final @NotNull Set<ObjectiveCard> commonObjectives;
    private final @NotNull GameListenerSupport pcs;

    public PickablesTable(@NotNull GameListenerSupport pcs) {
        this.deckManager = new DeckManager();

        this.commonObjectives = new HashSet<>(numOfCommonObjectives << 1);
        this.shownPlayable = EnumMapUtils.init(
                PlayableCardType.class,
                () -> new HashSet<>(numOfShownPerType << 1));

        this.pcs = pcs;

        resetDecks();
        shuffleDecks();
    }

    private void resetDecks() {
        deckManager.reset();

        Stream.of(PlayableCardType.values())
              .forEach(type -> pcs.fireEvent(new DeckTopChangeEvent(
                      type,
                      null,
                      getDeckTop(type).orElse(null)
              )));
    }

    private void shuffleDecks() {

        LOGGER.debug("MODEL: Shuffling decks...");

        deckManager.shuffle();

        Stream.of(PlayableCardType.values())
              .forEach(type -> pcs.fireEvent(new DeckTopChangeEvent(
                      type,
                      null,
                      getDeckTop(type).orElse(null)
              )));
    }

    /**
     * Get the color of the top card of the deck of the specified type
     *
     * @param type the type of the deck
     * @return the color of the top card of the deck, if present
     */
    public @NotNull Optional<GameColor> getDeckTop(@NotNull PlayableCardType type) {
        return deckManager.getDeckTop(type);
    }

    /**
     * Set the number of common objectives to be picked
     *
     * @param numOfCommonObjectives the number of common objectives to be picked
     */
    public static void setNumOfCommonObjectives(int numOfCommonObjectives) {
        PickablesTable.numOfCommonObjectives = numOfCommonObjectives;
    }

    /**
     * Set the number of shown cards per type
     *
     * @param numOfShownPerType the number of shown cards per type
     */
    public static void setNumOfShownPerType(int numOfShownPerType) {
        PickablesTable.numOfShownPerType = numOfShownPerType;
    }

    /**
     * Set the number of candidate objectives to be picked for each player
     *
     * @param numOfCandidatesObjectives the number of candidate objectives to be picked for each
     *                                  player
     */
    public static void setNumOfCandidatesObjectives(int numOfCandidatesObjectives) {
        PickablesTable.numOfCandidatesObjectives = numOfCandidatesObjectives;
    }

    /**
     * Get the common objectives, shared among all players
     *
     * @return the common objectives
     */
    public @NotNull Set<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableSet(commonObjectives);
    }

    /**
     * Draw a playable card from the deck of the specified type
     *
     * @param type the type of the deck
     * @return the drawn card
     * @throws EmptyDeckException if the deck is empty
     */
    public @NotNull PlayableCard drawPlayableFrom(@NotNull PlayableCardType type)
    throws EmptyDeckException {
        PlayableCard card = deckManager.drawPlayableFrom(type).orElseThrow(
                () -> new EmptyDeckException("Deck is empty!"));
        DeckTopChangeEvent event = new DeckTopChangeEvent(
                type,
                card.getColor(),
                getDeckTop(type).orElse(null));

        LOGGER.info("MODEL: Card color on top of the {} deck changed to: {}",
                    event.getCardType(),
                    event.getNewValue());

        pcs.fireEvent(event);
        return card;
    }

    /**
     * Draw a starter card from the deck
     *
     * @return the drawn card
     */
    public @NotNull StarterCard pickStarterCard() {
        return deckManager.drawStarter()
                          .orElseThrow();
    }

    /**
     * Pick a set of candidate objectives
     *
     * @return the set of candidate objectives
     */
    public Set<ObjectiveCard> pickObjectiveCandidates() {
        Set<ObjectiveCard> objs = new HashSet<>(numOfCandidatesObjectives << 1);
        IntStream.range(0, numOfCandidatesObjectives)
                 .forEach(i -> objs.add(pickObjectiveCard()));
        return Set.copyOf(objs);
    }


    private @NotNull ObjectiveCard pickObjectiveCard() {
        return deckManager.drawObjective()
                          .orElseThrow();
    }

    /**
     * Initialize the table, picking common objectives and showing the first cards
     */
    public void initialize() {
        clearTable();
        try {
            pickCommonObjectives();
        } catch (EmptyDeckException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < numOfShownPerType; i++) {
            Stream.of(PlayableCardType.values())
                  .map(type -> new Pair<>(shownPlayable.get(type),
                                          deckManager.drawPlayableFrom(type).orElseThrow()))
                  .forEach(pair -> {
                      pair.getKey().add(pair.getValue());
                      LOGGER.info("MODEL: {} card with ID {} is shown on the table",
                                  pair.getValue().getType(),
                                  pair.getValue().getId());

                      pcs.fireEvent(new ShownPlayableEvent(
                              pair.getValue().getType(),
                              null,
                              pair.getValue().getId()
                      ));
                  });
        }
    }

    /**
     * Clear the table, removing all cards, common objectives and shown cards
     */
    private void clearTable() {
        commonObjectives.clear();
        shownPlayable.values().forEach(Set::clear);
    }

    /**
     * Used to pick common objectives
     *
     * @throws EmptyDeckException if the deck of Objective Cards is empty
     */
    public void pickCommonObjectives() throws EmptyDeckException {
        for (int i = 0; i < numOfCommonObjectives; i++) {
            commonObjectives.add(pickObjectiveCard());
        }

        Set<Integer> objsID = commonObjectives.stream()
                                              .map(CardIdentity::getId)
                                              .collect(Collectors.toSet());

        LOGGER.info("MODEL: Common objectives are picked: {}", objsID);

        pcs.fireEvent(new CommonObjectiveChangeEvent(null,
                                                     objsID));
    }

    /**
     * Pick a visible card from the table and substitute it with a new one
     *
     * @param cardID the ID of the card to pick
     * @return the picked card
     * @throws IllegalPickActionException if the card is one of the shown cards
     */
    public PlayableCard pickPlayableVisible(int cardID) throws IllegalPickActionException {
        return shownPlayable.values().stream()
                            .flatMap(Collection::stream)
                            .filter(card -> card.getId() == cardID)
                            .findFirst()
                            .map(this::removePlayable)
                            .orElseThrow(() -> new IllegalPickActionException(
                                    "Card with ID " + cardID + " is not visible!"));
    }

    private @Nullable PlayableCard removePlayable(@NotNull PlayableCard playableCard) {
        if (! shownPlayable.get(playableCard.getType()).remove(playableCard)) return null;

        Optional<PlayableCard> newCard = deckManager.drawPlayableFrom(playableCard.getType());
        newCard.ifPresent(card -> shownPlayable.get(card.getType()).add(card));

        LOGGER.info("MODEL: Playable card with ID {} is substituted with {}",
                    playableCard.getId(),
                    newCard.map(PlayableCard::getId).orElse(null));

        pcs.fireEvent(new ShownPlayableEvent(
                playableCard.getType(),
                playableCard.getId(),
                newCard.map(PlayableCard::getId).orElse(null)
        ));

        LOGGER.info("MODEL: {} deck top color changed to: {}",
                    playableCard.getType(),
                    getDeckTop(playableCard.getType()).orElse(null));

        pcs.fireEvent(new DeckTopChangeEvent(
                playableCard.getType(),
                newCard.map(PlayableCard::getColor).orElse(null),
                getDeckTop(playableCard.getType()).orElse(null)
        ));

        return playableCard;
    }

    /**
     * Get the shown playable cards of the specified type
     *
     * @param type the PlayableCardType of the cards to get
     * @return the shown playable cards of the specified type
     */
    public Set<PlayableCard> getShownPlayable(@NotNull PlayableCardType type) {
        return Set.copyOf(shownPlayable.get(type));
    }

    /**
     * Method to get the number of remaining cards of the specified type on the deck
     *
     * @param type the PlayableCardType of the deck
     * @return the number of remaining cards of the specified type on the deck
     */
    public int getRemainingDeckOf(@NotNull PlayableCardType type) {
        return deckManager.getRemainingCardsOf(type);
    }

    /**
     * Used to save the current state of the PickablesTable, it will contain also information about
     * the order of the cards in the decks
     *
     * @return a memento of the PickablesTable
     */
    public @NotNull PickablesTableMemento save() {
        return new PickablesTableMemento(
                deckManager.save(),
                shownToIntMap(),
                commonToIntSet()
        );
    }

    private @NotNull @Unmodifiable Map<PlayableCardType, Set<Integer>> shownToIntMap() {
        Map<PlayableCardType, Set<Integer>> temp = new EnumMap<>(PlayableCardType.class);
        shownPlayable.forEach((type, cards) -> temp.put(type, cards.stream()
                                                                   .map(PlayableCard::getId)
                                                                   .collect(Collectors.toSet())));
        return Map.copyOf(temp);
    }

    private @NotNull @Unmodifiable Set<Integer> commonToIntSet() {
        return commonObjectives.stream()
                               .map(ObjectiveCard::getId)
                               .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Used to save the current state of the PickablesTable, it will contain only public information
     * about the table, without the order of the cards in the decks
     *
     * @return
     */
    public @NotNull ReconnectionTableMemento savePublic() {
        Map<PlayableCardType, GameColor> deckTops = new EnumMap<>(PlayableCardType.class);
        for (PlayableCardType type : PlayableCardType.values()) {
            deckTops.put(type, getDeckTop(type).orElse(null));
        }

        return new ReconnectionTableMemento(
                Map.copyOf(deckTops),
                shownToIntMap(),
                commonToIntSet());
    }

    /**
     * Used to load a PickablesTableMemento on the PickablesTable
     *
     * @param memento the information to load
     */
    public void load(@NotNull PickablesTableMemento memento) {
        hardReset();
        deckManager.load(memento.deckManager());
        Map<PlayableCardType, Set<PlayableCard>> temp = new EnumMap<>(PlayableCardType.class);
        memento.shownPlayable()
               .forEach((type, ids) -> temp.put(type, ids.stream()
                                                         .map(CardDecoder::decodePlayableCard)
                                                         .map(Optional::orElseThrow)
                                                         .collect(Collectors.toSet())));
        shownPlayable.putAll(temp);
        commonObjectives.addAll(memento.commonObjs().stream()
                                       .map(CardDecoder::decodeObjectiveCard)
                                       .map(Optional::orElseThrow)
                                       .collect(Collectors.toSet()));
    }

    /**
     * Used to reset the PickablesTable to the initial state
     */
    public void hardReset() {
        LOGGER.debug("MODEL: Hard resetting PickablesTable");

        resetDecks();
        clearTable();
    }
}
