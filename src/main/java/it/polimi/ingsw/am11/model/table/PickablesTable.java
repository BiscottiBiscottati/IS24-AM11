package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.am11.model.exceptions.IllegalPickActionException;
import it.polimi.ingsw.am11.persistence.memento.PickablesTableMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.table.CommonObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.DeckTopChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.ShownPlayableEvent;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final DeckManager deckManager;
    private final Map<PlayableCardType, Set<PlayableCard>> shownPlayable;
    private final Set<ObjectiveCard> commonObjectives;
    private final GameListenerSupport pcs;

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

    public Optional<Color> getDeckTop(@NotNull PlayableCardType type) {
        return deckManager.getDeckTop(type);
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

    public Set<ObjectiveCard> getCommonObjectives() {
        return Collections.unmodifiableSet(commonObjectives);
    }

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

    public StarterCard pickStarterCard() {
        return deckManager.drawStarter()
                          .orElseThrow();
    }

    public Set<ObjectiveCard> pickObjectiveCandidates() {
        Set<ObjectiveCard> objs = new HashSet<>(numOfCandidatesObjectives << 1);
        IntStream.range(0, numOfCandidatesObjectives)
                 .forEach(i -> objs.add(pickObjectiveCard()));
        return Set.copyOf(objs);
    }

    public ObjectiveCard pickObjectiveCard() {
        return deckManager.drawObjective()
                          .orElseThrow();
    }

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

    private void clearTable() {
        commonObjectives.clear();
        shownPlayable.values().forEach(Set::clear);
    }

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

    public Set<PlayableCard> getShownPlayable(@NotNull PlayableCardType type) {
        return Set.copyOf(shownPlayable.get(type));
    }

    public int getRemainingDeckOf(@NotNull PlayableCardType type) {
        return deckManager.getNumberRemainingOf(type);
    }

    public PickablesTableMemento save() {
        Map<PlayableCardType, Set<Integer>> temp = new EnumMap<>(PlayableCardType.class);
        shownPlayable.forEach((type, cards) -> temp.put(type, cards.stream()
                                                                   .map(PlayableCard::getId)
                                                                   .collect(Collectors.toSet())));
        return new PickablesTableMemento(
                deckManager.save(),
                Map.copyOf(temp),
                commonObjectives.stream()
                                .map(ObjectiveCard::getId)
                                .collect(Collectors.toSet())
        );
    }

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

    public void hardReset() {
        LOGGER.debug("MODEL: Hard resetting PickablesTable");

        resetDecks();
        clearTable();
    }
}
