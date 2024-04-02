package it.polimi.ingsw.am11.decks;

import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBConnectionManagerTest {


    DBConnectionManager mng;

    @BeforeEach
    void setUp() {
        mng = new DBConnectionManager();
    }

    @Test
    void getCards() {
        int result = mng.selectType(PlayableDeckType.GOLD);
        System.out.println(result);
    }
}