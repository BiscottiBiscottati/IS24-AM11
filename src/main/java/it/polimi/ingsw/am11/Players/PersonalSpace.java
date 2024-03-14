package it.polimi.ingsw.am11.Players;

import it.polimi.ingsw.am11.cards.ObjectiveCard;

import java.util.ArrayList;

public class PersonalSpace {
    private static final int maxHandCards;

    static {
        maxHandCards = getMaxHand;
    }

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

    public void addCardToHand() {

    }
}
