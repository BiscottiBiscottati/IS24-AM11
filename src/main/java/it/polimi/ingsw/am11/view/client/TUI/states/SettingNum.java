package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingNum implements TUIState {
    private static final String askForNum = "How many players will play? >>> \033[K";
    private boolean alreadyError = false;
    private boolean isBlocked = false;

    private static void errorsHappens(String text) {
        System.out.println("\033[F" + "\033[K" + text);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();
        int val;

        if (isBlocked) {
            System.out.print("\033[A");
            return;
        }

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F" + askForNum);
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
            System.out.print(askForNum);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            //Maybe a return is needed after help
            case "exit" -> Actuator.close();
        }


        try {
            val = Integer.parseInt(word);
            if (val < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            errorsHappensEvenTwice("ERROR: " + word + " is neither a valid command nor a valid " +
                                   "number");
            alreadyError = true;
            System.out.print(askForNum);
            return;
        }

        isBlocked = true;
        actuator.setNumOfPlayers(val);
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        isBlocked = false;

        ConsUtils.clear();
        System.out.println("""
                                   ++++++++++++++++++++++++++++
                                   \s
                                    STATUS: Choosing a nickname...
                                   \s
                                   ++++++++++++++++++++++++++++
                                   \s""");

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
        }
        System.out.println("You are the moderator of the game, you have to choose the number of " +
                           "players:");
        System.out.print(askForNum);
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
