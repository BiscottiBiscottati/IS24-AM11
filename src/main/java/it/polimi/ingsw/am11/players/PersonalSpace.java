package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.exceptions.MaxHandSizeException;

import java.util.ArrayList;


public class PersonalSpace {

    private final ArrayList<PlayableCard> playerHand;
    private final int maxSizeofHand;
    private final int maxObjectives;
    private final ArrayList<ObjectiveCard> playerObjective;

    public PersonalSpace(int maxSizeofHand, int maxObjectives) {
        //TODO: checking if sizeofHand is legal
        this.maxSizeofHand = maxSizeofHand;
        this.maxObjectives = maxObjectives;
        playerHand = new ArrayList<>(maxSizeofHand);
        playerObjective = new ArrayList<>(1);
    }

    public ArrayList<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
    }

    public int availableSpaceInHand() {
        return maxSizeofHand - playerHand.size();
    }

    public void addCardToHand(PlayableCard newCard) throws MaxHandSizeException {
        //throw alreadyMaxCardInHand
        if (playerHand.size() == maxSizeofHand) {
            throw new MaxHandSizeException("already Max Card In Hand");
        } else {
            playerHand.add(newCard);
        }
    }

    public void pickCard(PlayableCard cardToPick) {
        if (playerHand.contains(cardToPick)) {
            playerHand.remove(cardToPick);
        } else {
            //throw not card in hand exception
        }
    }

    public void addObjective(ObjectiveCard newObjective) throws IllegalPlayerSpaceActionException {
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
