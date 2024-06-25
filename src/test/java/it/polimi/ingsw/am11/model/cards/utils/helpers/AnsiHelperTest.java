package it.polimi.ingsw.am11.model.cards.utils.helpers;

import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

class AnsiHelperTest {

    @Test
    void addColors() {
        System.out.println(AnsiHelper.addColors("string", Ansi.Color.RED));
    }
}