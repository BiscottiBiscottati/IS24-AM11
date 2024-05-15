package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.view.client.GUI.window.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.TUI.TuiHandler;
import javafx.application.Application;
import org.jetbrains.annotations.NotNull;

public class Client {

    static void start(@NotNull ArgParser parser) {
        String uiMode = parser.getOption("ui").orElseThrow().getValue();
        // TODO gui or tui and setup connection to server
        switch (uiMode) {
            case "gui" -> Application.launch(CodexNaturalis.class);
            case "tui" -> new TuiHandler().start();
            default -> System.out.println("Invalid UI mode " + uiMode + ". Please choose between " +
                                          "'gui' and 'tui'");
        }
    }
}
