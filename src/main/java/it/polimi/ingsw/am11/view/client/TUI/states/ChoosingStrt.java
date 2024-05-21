package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.ConsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChoosingStrt implements TUIState {
    private static final String askForSide = "Place it on its front or on its back >>> \033[K";
    private boolean alreadyError = false;
    private boolean isBlocked = false;

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        if (isBlocked) {
            System.out.print("\033[A");
            return;
        }

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F" + askForSide);
            return;
        }

        // Handle exception
        try {
            parser.parse(args);
            if (parser.getPositionalArgs().size() > 1) {
                throw new ParsingErrorException(
                        "Too many arguments, starting from: " + parser.getPositionalArgs().get(1));
            }
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            System.out.print(askForSide);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            //Maybe a return is needed after help
            case "exit" -> Actuator.close();
            case "front" -> {
                isBlocked = true;
                actuator.setStarter(false);
            }
            case "retro" -> {
                isBlocked = true;
                actuator.setStarter(true);
            }
            default -> {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid option");
                alreadyError = true;
                System.out.print(askForSide);
            }
        }
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        isBlocked = false;

        ConsUtils.clear();
        System.out.println("""
                                   ++++++++++++++++++++++++++++
                                   \s
                                    STATUS: Placing the starter card...
                                   \s
                                   ++++++++++++++++++++++++++++
                                   \s""");

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }
        System.out.println("You received this starter card:");
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
