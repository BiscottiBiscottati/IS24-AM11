package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;

import java.util.ArrayList;

public class PersonalSpace {

    private final ArrayList<PlayableCard> playerHand;
    private final int sizeofHand;
    private final int maxObjectives;
    private ObjectiveCard playerObjective;

    public PersonalSpace(int sizeofHand, int maxObjectives) {
        //TODO: checking if sizeofHand is legal
        this.sizeofHand = sizeofHand;
        this.maxObjectives = maxObjectives;
        playerHand = new ArrayList<>(sizeofHand);
    }

    public ArrayList getPlayerHand() {
        return playerHand;
    }

    public ObjectiveCard getPlayerObjective() {
        return playerObjective;
    }

    public void addCardToHand(PlayableCard newCard) {
//        if (playerHand.size() < maxHandCards) {
//            playerHand.add(newCard);
//        } else {
//            //throw alreadyMaxCardInHand
//        }
    }

//    public void addObjectiveToHand(ObjectiveCard newObjective) {
//        playerObjective = newObjective;
//    }
//
//    public void pickCard(PlayableCard cardToPick) {
//        if (playerHand.contains(cardToPick)) {
//            playerHand.remove(cardToPick);
//        } else {
//            //throw not card in hand exception
//        }
//    }
//
//    //clear both hand and objective
//    public void clearAll() {
//        playerHand.clear();
//        playerObjective = null;
//    }

}
