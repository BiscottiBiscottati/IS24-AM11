package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import org.jetbrains.annotations.NotNull;

public class Client {

    static void start(@NotNull ArgParser parser) {
        String uiMode = parser.getOption("ui").orElseThrow().getValue();
        // TODO gui or tui and setup connection to server
    }
}
