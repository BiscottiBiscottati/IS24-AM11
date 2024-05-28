package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.model.utils.BasicRuleset;
import it.polimi.ingsw.am11.model.utils.RuleSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BasicRulesetTest {

    RuleSet ruleSet;

    @BeforeEach
    void setUp() {
        ruleSet = new BasicRuleset();
    }

    @Test
    void getHandSize() {
        Assertions.assertEquals(3, ruleSet.getHandSize());
    }

    @Test
    void getMaxRevealedCardsPerType() {
    }

    @Test
    void getNumOfPersonalObjective() {
    }

    @Test
    void getNumOfCommonObjectives() {
    }

    @Test
    void getMaxPlayers() {
    }

    @Test
    void getPointsToArmageddon() {
    }
}