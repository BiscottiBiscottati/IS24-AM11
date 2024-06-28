package it.polimi.ingsw.am11.model.cards.utils.helpers;

import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class AnsiHelper {

    public static String addColors(String string, Ansi.Color color) {
        return ansi().fg(color).a(string).reset().toString();
    }
}
