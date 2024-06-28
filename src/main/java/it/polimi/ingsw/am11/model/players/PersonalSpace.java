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


/**
 * This class represents the personal space of a player. It contains the player's hand, objectives,
 * and candidate objectives.
 */
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

    /**
     * This method sets the maximum size of the hand.
     *
     * @param maxSizeofHand the maximum size of the hand
     */
    public static void setMaxSizeofHand(int maxSizeofHand) {
        PersonalSpace.maxSizeofHand = maxSizeofHand;
    }

    /**
     * This method sets the maximum number of objectives that a player can have.
     *
     * @param maxObjectives the maximum number of objectives
     */
    public static void setMaxObjectives(int maxObjectives) {
        PersonalSpace.maxObjectives = maxObjectives;
    }

    /**
     * This method sets the maximum number of candidate objectives that a player can have.
     *
     * @param maxCandidate the maximum number of candidate objectives
     */
    public static void setMaxCandidateObjectives(int maxCandidate) {
        PersonalSpace.maxCandidateObjectives = maxCandidate;
    }

    /**
     * This method returns the player's hand.
     *
     * @return the player's hand
     */
    public @NotNull Set<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    /**
     * This method returns the player's objectives.
     *
     * @return the player's objectives
     */
    public @NotNull Set<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
    }

    /**
     * This method returns the player's starter card.
     *
     * @return the player's starter card
     */
    public @NotNull Optional<StarterCard> getStarterCard() {
        return Optional.ofNullable(starterCard);
    }

    /**
     * This method sets the player's starter card.
     *
     * @param starter the starter card
     * @throws IllegalPlayerSpaceActionException if the starter card is already set
     */
    public void setStarterCard(@NotNull StarterCard starter)
    throws IllegalPlayerSpaceActionException {
        if (starterCard == null) {
            starterCard = starter;
        } else {
            throw new IllegalPlayerSpaceActionException(
                    "You are trying to set a starter card twice");
        }
    }

    /**
     * This method returns the candidate objectives.
     *
     * @return the candidate objectives
     */
    public Set<ObjectiveCard> getCandidateObjectives() {
        return Set.copyOf(candidateObjectives);
    }

    /**
     * This method returns the candidate objective with the given id.
     *
     * @param id the id of the candidate objective
     * @return the candidate objective with the given id
     * @throws IllegalPlayerSpaceActionException if the objective with the given id is not one of
     *                                           the
     */
    public ObjectiveCard getCandidateObjectiveByID(int id)
    throws IllegalPlayerSpaceActionException {
        return candidateObjectives.stream()
                                  .filter(obj -> obj.getId() == id)
                                  .findFirst()
                                  .orElseThrow(() -> new IllegalPlayerSpaceActionException(
                                          "The objective that you chose is not" +
                                          " one of yours"));
    }

    /**
     * This method sets the new candidate objectives.
     *
     * @param objective the new candidate objective
     */
    public void setNewCandidateObjectives(@NotNull ObjectiveCard objective) {
        candidateObjectives.add(objective);
    }

    /**
     * This method returns the number of available spaces in the player's hand.
     *
     * @return the number of available spaces in the player's hand
     */
    public int availableSpaceInHand() {
        return maxSizeofHand - playerHand.size();
    }

    /**
     * This method adds a new card to the player's hand.
     *
     * @param newCard the new card to add
     * @throws MaxHandSizeException if the player's hand is already full
     */
    public void addCardToHand(PlayableCard newCard) throws MaxHandSizeException {
        //throw alreadyMaxCardInHand
        if (playerHand.size() >= maxSizeofHand) {
            throw new MaxHandSizeException("already Max Card In Hand");
        } else {
            playerHand.add(newCard);
        }
    }

    /**
     * This method removes a card from the player's hand.
     *
     * @param cardId the id of the card to remove
     * @throws NotInHandException if the card with the given id is not in the player's hand
     */
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

    /**
     * This method adds a new objective to the player's objectives.
     *
     * @param newObjective the new objective to add
     * @throws IllegalPlayerSpaceActionException if the player's objectives are already full
     */
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

    /**
     * This method returns true if the player has all the objectives that he should have.
     *
     * @return true if the player has all the objectives that he should have
     */
    public boolean areObjectiveGiven() {
        return maxObjectives == playerObjective.size();
    }

    /**
     * This method saves the current state of the player's personal space.
     *
     * @return the memento with the current state of the player's personal space
     */
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

    /**
     * This method reloads an old status of the player's personal space from a memento.
     *
     * @param memento the memento to reload
     */
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

    /**
     * This method clears all the player's personal space.
     */
    public void clearAll() {
        playerHand.clear();
        playerObjective.clear();
        candidateObjectives.clear();
        starterCard = null;

    }
}
