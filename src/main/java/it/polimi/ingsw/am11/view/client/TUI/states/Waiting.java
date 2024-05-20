package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.ConsUtils;
import org.jetbrains.annotations.NotNull;

public class Waiting implements TUIState {
    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        if (args[0].isEmpty()) {
            System.out.print("\033[A");
            return;
        }

        // Handle exception
        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            errorsHappens("To ask is permissible , to reply is polite >>>");
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            case "exit" -> Actuator.close();
            case "kira" -> kiraIsAGoodGirl();
            default -> errorsHappens("To ask is permissible , to reply is polite >>>");
        }
    }

    @Override
    public void restart(boolean dueToEx, Exception exception) {
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
        }
        System.out.println("Everybody is waiting for something...");
        System.out.print("To ask is permissible , to reply is polite >>>");
    }

    private static @NotNull ArgParser setUpOptions() {
        return new ArgParser();
    }

    private static void errorsHappens(String text) {
        System.out.println("\033[F" + "\033[K" + text);
    }

    private static void kiraIsAGoodGirl() {
        System.out.println("Kira is the best dog one could ever have!! ");
        System.out.print("To ask is permissible , to reply is polite >>>");
    }
}
