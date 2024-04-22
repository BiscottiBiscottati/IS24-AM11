package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.exceptions.MaxHandSizeException;
import it.polimi.ingsw.am11.exceptions.NotInHandException;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class PersonalSpace {


    private static int maxSizeofHand;
    private static int maxObjectives;
    private static int maxCandidateObjectives;
    private final Set<PlayableCard> playerHand;
    private final Set<ObjectiveCard> playerObjective;
    private final Map<Integer, ObjectiveCard> candidateObjectives;
    private StarterCard starterCard;
    private boolean placed;

    public PersonalSpace() {
        playerHand = new HashSet<>(maxSizeofHand << 1);
        playerObjective = new HashSet<>(maxObjectives << 1);
        candidateObjectives = new HashMap<>(maxCandidateObjectives << 1);
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

    public void setCardBeenPlaced(boolean val) {
        placed = val;
    }

    public boolean hasCardBeenPlaced() {
        return placed;
    }

    public Set<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    public Set<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
    }

    public Optional<StarterCard> getStarterCard() {
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
        return Set.copyOf(candidateObjectives.values());
    }

    public ObjectiveCard getCandidateObjectiveByID(int id)
    throws IllegalPlayerSpaceActionException {
        ObjectiveCard objective = candidateObjectives.get(id);
        if (objective != null) {
            return objective;
        } else {
            throw new IllegalPlayerSpaceActionException(
                    "The objective that you chose is not one of yours");
        }
    }

    public void setNewCandidateObjectives(ObjectiveCard objective) {
        candidateObjectives.put(objective.getId(), objective);
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

    public void addObjective(ObjectiveCard newObjective) throws IllegalPlayerSpaceActionException {
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
        candidateObjectives.remove(id);
    }

    public void clearAll() {
        playerHand.clear();
        playerObjective.clear();
        candidateObjectives.clear();
        starterCard = null;
    }

    public boolean areObjectiveGiven() {
        return maxObjectives == playerObjective.size();
    }

}
