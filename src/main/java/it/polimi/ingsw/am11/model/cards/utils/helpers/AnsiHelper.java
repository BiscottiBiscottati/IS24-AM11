package it.polimi.ingsw.am11.model.cards.utils.helpers;

import org.fusesource.jansi.Ansi;

import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.ansi;

public class AnsiHelper {
    private static final Pattern ANSI_CODES_REMOVER = Pattern.compile("\u001B\\[[;\\d]*m");

    public static String addColors(String string, Ansi.Color color) {
        return ansi().fg(color).a(string).reset().toString();
    }
}
