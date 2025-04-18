package it.polimi.ingsw.am11.model.utils;

public interface RuleSet {
    /**
     * @return the number of objectives that a player can choose between at the start of the game
     */
    int getObjectiveToChooseFrom();

    /**
     * @return the number of resource cards dealt at the beginning
     */
    int getResourceAtStart();

    /**
     * @return the number of gold cards dealt at the beginning
     */
    int getGoldAtStart();

    /**
     * @return the number of cards that the hand can hold
     */
    int getHandSize();

    /**
     * A Revealed card is a card facing up on the PickableTable
     *
     * @return the number of revealed cards on the table per type (e.g., Resource, Gold)
     */
    int getMaxRevealedCardsPerType();

    /**
     * @return the number of personal Objectives that a player has to receive
     */
    int getNumOfPersonalObjective();

    /**
     * @return the number of Objectives that are in common between players
     */
    int getNumOfCommonObjectives();

    /**
     * @return the maximum number of players that can join in a match
     */
    int getMaxPlayers();

    /**
     * @return the minimum number of points that are needed for the beginning of the final phase of
     * the game
     */
    int getPointsToArmageddon();
}
