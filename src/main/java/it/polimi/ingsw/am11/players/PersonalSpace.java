package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.exceptions.MaxHandSizeException;
import it.polimi.ingsw.am11.exceptions.NotInHandException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class PersonalSpace {


    private static int maxSizeofHand;
    private static int maxObjectives;
    private final Set<PlayableCard> playerHand;
    private final Set<ObjectiveCard> playerObjective;

    public PersonalSpace() {
        playerHand = new HashSet<>(maxSizeofHand << 1);
        playerObjective = new HashSet<>(maxObjectives << 1);
    }

    public static void setMaxSizeofHand(int maxSizeofHand) {
        PersonalSpace.maxSizeofHand = maxSizeofHand;
    }

    public static void setMaxObjectives(int maxObjectives) {
        PersonalSpace.maxObjectives = maxObjectives;
    }

    public Set<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    public Set<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
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

    public void clearAll() {
        playerHand.clear();
        playerObjective.clear();
    }

}
