package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.exceptions.MaxHandSizeException;
import it.polimi.ingsw.am11.exceptions.NotInHandException;

import java.util.ArrayList;


public class PersonalSpace {


    private static int maxSizeofHand;
    private static int maxObjectives;
    private final ArrayList<PlayableCard> playerHand;
    private final ArrayList<ObjectiveCard> playerObjective;

    public PersonalSpace() {
        playerHand = new ArrayList<>(maxSizeofHand);
        playerObjective = new ArrayList<>(1);
    }

    public static void setConstants(int maxSizeofHand, int maxObjectives) {
        PersonalSpace.maxSizeofHand = maxSizeofHand;
        PersonalSpace.maxObjectives = maxObjectives;
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

    public void pickCard(int cardId) throws NotInHandException {
        int cardToRemove = playerHand.stream()
                                     .map(PlayableCard::getId)
                                     .filter(id -> id == cardId)
                                     .findFirst()
                                     .orElseThrow(() -> new NotInHandException("Card not in hand"));

        playerHand.stream()
                  .filter(card -> card.getId() == cardToRemove);

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
