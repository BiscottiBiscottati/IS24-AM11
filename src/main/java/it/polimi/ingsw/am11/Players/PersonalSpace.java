package it.polimi.ingsw.am11.Players;

import it.polimi.ingsw.am11.Cards.ObjectiveCard;
import it.polimi.ingsw.am11.Cards.ObjectiveCardType;

import java.util.ArrayList;

public class PersonalSpace {

    private ArrayList playerHand;
    private ObjectiveCard playerObjective;

    public PersonalSpace(ArrayList playerHand, ObjectiveCard playerObjective) {
        this.playerHand = playerHand;
        this.playerObjective = playerObjective;
    }

    public ArrayList getPlayerHand() {
        return playerHand;
    }

    public ObjectiveCard getPlayerObjective() {
        return playerObjective;
    }
}
