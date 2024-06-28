package it.polimi.ingsw.am11;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.TUI.TuiHandler;
import javafx.application.Application;
import org.jetbrains.annotations.NotNull;

/**
 * The main class for the client.
 */
public class Client {

    /**
     * The main method to start the client, it will start the UI mode specified in the arguments.
     *
     * @param parser The parser containing the arguments for the client.
     */
    static void start(@NotNull ArgParser parser) {
        String uiMode = parser.getOption("ui").orElseThrow().getValue();
        switch (uiMode) {
            case "gui" -> Application.launch(CodexNaturalis.class);
            case "tui" -> TuiHandler.start();
            case null, default -> System.out.println("Invalid UI mode " + uiMode + ". Please " +
                                                     "choose between " +
                                                     "'gui' and 'tui'");
        }
    }
}
