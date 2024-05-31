package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.view.client.GUI.window.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.TUI.TuiHandler;
import javafx.application.Application;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Client {

    static void start(@NotNull ArgParser parser) throws IOException {
        String uiMode = parser.getOption("ui").orElseThrow().getValue();
        switch (uiMode) {
            case "gui" -> Application.launch(CodexNaturalis.class);
            case "tui" -> new TuiHandler().start();
            case null, default -> System.out.println("Invalid UI mode " + uiMode + ". Please " +
                                                     "choose between " +
                                                     "'gui' and 'tui'");
        }
    }
}
