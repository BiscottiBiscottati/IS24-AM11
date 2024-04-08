package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;

import java.util.ArrayList;

public class PersonalSpace {

    private final ArrayList<PlayableCard> playerHand;
    private final int sizeofHand;
    private final int maxObjectives;
    private ArrayList<ObjectiveCard> playerObjective;

    public PersonalSpace(int sizeofHand, int maxObjectives) {
        //TODO: checking if sizeofHand is legal
        this.sizeofHand = sizeofHand;
        this.maxObjectives = maxObjectives;
        playerHand = new ArrayList<>(sizeofHand);
    }

    public ArrayList<PlayableCard> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<ObjectiveCard> getPlayerObjective() {
        return playerObjective;
    }

    public void addCardToHand(PlayableCard newCard) {
        // TODO this has been commented out because it does not let the compiler compile

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
