package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import org.jetbrains.annotations.NotNull;

public class Waiting implements TUIState {
    public static final String askLine = "To ask is permissible , to reply is polite >>> \033[K";
    private boolean alreadyError = false;

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        if (args[0].isEmpty()) {
            System.out.print("\033[F" + askLine);
            return;
        }

        // Handle exception
        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            System.out.print(askLine);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            case "exit" -> Actuator.close();
            default -> {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid option");
                alreadyError = true;
                System.out.print(askLine);
            }
        }
    }

    @Override
    public void restart(boolean dueToEx, Exception exception) {
        alreadyError = false;

        ConsUtils.clear();
        System.out.println("""
                                   ++++++++++++++++++++++++++++
                                   \s
                                    STATUS: Waiting...
                                   \s
                                   ++++++++++++++++++++++++++++
                                   \s""");

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        }
        System.out.println("Everybody is waiting for something...");
        System.out.print(askLine);
    }

    private static @NotNull ArgParser setUpOptions() {
        return new ArgParser();
    }

    private void errorsHappensEvenTwice(String text) {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
        }
        System.out.println("\033[F" + "\033[K" + text);
    }
}
