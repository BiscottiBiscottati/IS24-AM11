package it.polimi.ingsw.am11.Players;

import it.polimi.ingsw.am11.Cards.ObjectiveCard;
import it.polimi.ingsw.am11.Cards.ObjectiveCardType;

import java.util.ArrayList;

public class PersonalSpace {
    static {
        maxHandCards = getMaxHand;
    }
    private static final int maxHandCards;
    private ArrayList playerHand;
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

    public void addCardToHand(){

    }
}
