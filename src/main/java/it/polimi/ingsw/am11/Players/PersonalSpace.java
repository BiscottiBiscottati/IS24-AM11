package it.polimi.ingsw.am11.Players;

import it.polimi.ingsw.am11.cards.ObjectiveCard;
import it.polimi.ingsw.am11.cards.PlayableCard;

import java.util.ArrayList;

public class PersonalSpace {

    private final ArrayList playerHand;
    private ObjectiveCard playerObjective;

    public PersonalSpace() {
        playerHand = new ArrayList();
    }

    public ArrayList getPlayerHand() {
        return playerHand;
    }

    public ObjectiveCard getPlayerObjective() {
        return playerObjective;
    }

    public void addCardToHand(PlayableCard newCard) {
        if (playerHand.size() < maxHandCards) {
            playerHand.add(newCard);
        } else {
            //throw alreadyMaxCardInHand
        }
    }

    public void addObjectiveToHand(ObjectiveCard newObjective) {
        playerObjective = newObjective;
    }

    public void pickCard(PlayableCard cardToPick) {
        if (playerHand.contains(cardToPick)) {
            playerHand.remove(cardToPick);
        } else {
            //throw not card in hand exception
        }
    }

    //clear both hand and objective
    public void clearAll() {
        playerHand.clear();
        playerObjective = null;
    }
}
