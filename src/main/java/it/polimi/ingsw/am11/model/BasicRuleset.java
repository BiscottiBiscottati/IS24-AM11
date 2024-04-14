package it.polimi.ingsw.am11.model;

public class BasicRuleset implements RuleSet {

    private static final int HandSize = 3;
    private static final int maxRevealedCardsPerType = 2;
    private static final int numOfPersonalObjective = 1;
    private static final int numOfCommonObjectives = 2;
    private static final int maxPlayers = 4;

    private static final int pointsToArmageddon = 20;

    /**
     * @return the number of cards that the hand can hold
     */
    public int getHandSize() {
        return HandSize;
    }

    /**
     * A Revealed card is a card facing up on the PickableTable
     *
     * @return the number of revealed cards on the table per type (e.g., Resource, Gold)
     */
    public int getMaxRevealedCardsPerType() {
        return maxRevealedCardsPerType;
    }

    /**
     * @return the number of personal Objectives that a player has to receive
     */
    public int getNumOfPersonalObjective() {
        return numOfPersonalObjective;
    }

    /**
     * @return the number of Objectives that are in common between players
     */
    public int getNumOfCommonObjectives() {
        return numOfCommonObjectives;
    }

    /**
     * @return the maximum number of players that can join in a match
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return the minimum number of points that are needed for the beginning of the final phase of
     * the game
     */
    public int getPointsToArmageddon() {
        return pointsToArmageddon;
    }
}
