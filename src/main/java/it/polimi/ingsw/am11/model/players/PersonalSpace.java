package it.polimi.ingsw.am11.model.players;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.model.exceptions.MaxHandSizeException;
import it.polimi.ingsw.am11.model.exceptions.NotInHandException;
import it.polimi.ingsw.am11.model.utils.memento.PersonalSpaceMemento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class PersonalSpace {


    private static int maxSizeofHand;
    private static int maxObjectives;
    private static int maxCandidateObjectives;

    private final @NotNull Set<PlayableCard> playerHand;
    private final @NotNull Set<ObjectiveCard> playerObjective;
    private final @NotNull Set<ObjectiveCard> candidateObjectives;
    private @Nullable StarterCard starterCard;

    public PersonalSpace() {
        playerHand = new HashSet<>(maxSizeofHand << 1);
        playerObjective = new HashSet<>(maxObjectives << 1);
        candidateObjectives = new HashSet<>(maxCandidateObjectives << 1);
        starterCard = null;
    }

    public static void setMaxSizeofHand(int maxSizeofHand) {
        PersonalSpace.maxSizeofHand = maxSizeofHand;
    }

    public static void setMaxObjectives(int maxObjectives) {
        PersonalSpace.maxObjectives = maxObjectives;
    }

    public static void setMaxCandidateObjectives(int maxCandidate) {
        PersonalSpace.maxCandidateObjectives = maxCandidate;
    }

    public @NotNull Set<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    public @NotNull Set<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
    }

    public @NotNull Optional<StarterCard> getStarterCard() {
        return Optional.ofNullable(starterCard);
    }

    public void setStarterCard(@NotNull StarterCard starter)
    throws IllegalPlayerSpaceActionException {
        if (starterCard == null) {
            starterCard = starter;
        } else {
            throw new IllegalPlayerSpaceActionException(
                    "You are trying to set a starter card twice");
        }
    }

    public Set<ObjectiveCard> getCandidateObjectives() {
        return Set.copyOf(candidateObjectives);
    }

    public ObjectiveCard getCandidateObjectiveByID(int id)
    throws IllegalPlayerSpaceActionException {
        return candidateObjectives.stream()
                                  .filter(obj -> obj.getId() == id)
                                  .findFirst()
                                  .orElseThrow(() -> new IllegalPlayerSpaceActionException(
                                          "The objective that you chose is not" +
                                          " one of yours"));
    }

    public void setNewCandidateObjectives(@NotNull ObjectiveCard objective) {
        candidateObjectives.add(objective);
    }

    public int availableSpaceInHand() {
        return maxSizeofHand - playerHand.size();
    }

    public void addCardToHand(PlayableCard newCard) throws MaxHandSizeException {
        //throw alreadyMaxCardInHand
        if (playerHand.size() >= maxSizeofHand) {
            throw new MaxHandSizeException("already Max Card In Hand");
        } else {
            playerHand.add(newCard);
        }
    }

    public void pickCard(int cardId) throws NotInHandException {
        PlayableCard cardToRemove = playerHand.stream()
                                              .filter(card -> card.getId() == cardId)
                                              .findFirst()
                                              .orElseThrow(() -> new NotInHandException(
                                                      "Card not in hand"));

        playerHand.stream()
                  .filter(cardToRemove::equals)
                  .findFirst()
                  .ifPresent(playerHand::remove);

    }

    public void addObjective(@NotNull ObjectiveCard newObjective)
    throws IllegalPlayerSpaceActionException {
        Optional.of(newObjective)
                .filter(objective -> ! playerObjective.contains(objective))
                .orElseThrow(() -> new IllegalPlayerSpaceActionException(
                        "You are trying to add the same objective twice"));
        if (playerObjective.size() < maxObjectives) {
            playerObjective.add(newObjective);
        } else {
            throw new IllegalPlayerSpaceActionException(
                    "You are trying to add too many personal objectives");
        }
    }

    public void removeCandidateObjective(int id) {
        candidateObjectives.removeIf(obj -> obj.getId() == id);
    }

    public boolean areObjectiveGiven() {
        return maxObjectives == playerObjective.size();
    }

    public @NotNull PersonalSpaceMemento save() {
        return new PersonalSpaceMemento(playerHand.stream()
                                                  .map(PlayableCard::getId)
                                                  .collect(Collectors.toSet()),
                                        playerObjective.stream()
                                                       .map(ObjectiveCard::getId)
                                                       .collect(Collectors.toSet()),
                                        candidateObjectives.stream()
                                                           .map(ObjectiveCard::getId)
                                                           .collect(Collectors.toSet()),
                                        starterCard != null ? starterCard.getId() : - 1);
    }

    public void load(@NotNull PersonalSpaceMemento memento) {
        clearAll();

        memento.hand()
               .forEach(id -> playerHand.add(
                       CardDecoder.decodePlayableCard(id).orElseThrow()));
        memento.personalObjs()
               .forEach(id -> playerObjective.add(
                       CardDecoder.decodeObjectiveCard(id).orElseThrow()));
        memento.candidateObjs()
               .forEach(id -> candidateObjectives.add(
                       CardDecoder.decodeObjectiveCard(id).orElseThrow()));
        starterCard = CardDecoder.decodeStarterCard(memento.starterCard()).orElseThrow();
    }

    public void clearAll() {
        playerHand.clear();
        playerObjective.clear();
        candidateObjectives.clear();
        starterCard = null;

    }
}
