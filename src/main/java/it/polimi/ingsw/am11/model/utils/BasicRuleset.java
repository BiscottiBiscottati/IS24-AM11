package it.polimi.ingsw.am11.model.utils;

public class BasicRuleset implements RuleSet {

    private static final int HandSize = 3;
    private static final int maxRevealedCardsPerType = 2;
    private static final int numOfPersonalObjective = 1;
    private static final int numOfCommonObjectives = 2;
    private static final int maxPlayers = 4;

    private static final int pointsToArmageddon = 20;
    private static final int goldAtStart = 1;
    private static final int resourceAtStart = 2;

    private static final int objectiveToChooseFrom = 2;

    /**
     * @return the number of objectives that a player can choose between at the start of the game
     */
    @Override
    public int getObjectiveToChooseFrom() {
        return objectiveToChooseFrom;
    }

    /**
     * @return the number of resource cards dealt with at the beginning
     */
    @Override
    public int getResourceAtStart() {
        return resourceAtStart;
    }

    /**
     * @return the number of gold cards dealt at the beginning
     */
    @Override
    public int getGoldAtStart() {
        return goldAtStart;
    }

    /**
     * @return the number of cards that the hand can hold
     */
    @Override
    public int getHandSize() {
        return HandSize;
    }

    /**
     * A Revealed card is a card facing up on the PickableTable
     *
     * @return the number of revealed cards on the table per type (e.g., Resource, Gold)
     */
    @Override
    public int getMaxRevealedCardsPerType() {
        return maxRevealedCardsPerType;
    }

    /**
     * @return the number of personal Objectives that a player has to receive
     */
    @Override
    public int getNumOfPersonalObjective() {
        return numOfPersonalObjective;
    }

    /**
     * @return the number of Objectives that are in common between players
     */
    @Override
    public int getNumOfCommonObjectives() {
        return numOfCommonObjectives;
    }

    /**
     * @return the maximum number of players that can join in a match
     */
    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return the minimum number of points that are needed for the beginning of the final phase of
     * the game
     */
    @Override
    public int getPointsToArmageddon() {
        return pointsToArmageddon;
    }
}
