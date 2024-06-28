package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

public class SettingName extends TUIState {

    static final String askYourName = "What's your name? >>> \033[K";
    private static final String infoBar = "STATUS: Choosing a nickname...";
    private boolean isQuote = false;
    private boolean isBlocked = false;
    private boolean alreadyError = false;

    public SettingName(@NotNull MiniGameModel model) {
        super(model);
    }

    private static void errorsHappens(String text) {
        System.out.println("\033[F" + "\033[K" + text);
    }

    @Override
    public void passArgs(@NotNull Actuator actuator, String @NotNull [] args) {
        ArgParser parser = setUpOptions();


        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
            return;
        }

        if (isBlocked) {
            System.out.print("\033[A");
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
            TuiStates.printAskLine(this);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> {
                Actuator.help();
                return;
            }
            case "exit" -> Actuator.close();
        }

        if (word.length() > 19) {
            errorsHappensEvenTwice("The name is too long, it must be less than 30 " +
                                   "characters");
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }

        try {
            isBlocked = true;
            actuator.setName(word);
        } catch (TooManyRequestsException e) {
            if (! isQuote) {
                errorsHappensEvenTwice(
                        "\"One moment of patience may ward off great disaster. One moment " +
                        "of impatience may ruin a whole life.\"");
                isQuote = true;
            }
            System.out.println("ERROR: " + e.getMessage() + "\033[K");
            alreadyError = true;
            TuiStates.printAskLine(this);
        }

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

    @Override
    public void restart(boolean dueToEx, @NotNull Exception exception) {
        isBlocked = false;
        alreadyError = false;

        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        }
        System.out.println("You can now choose your nickname, it can't contain spaces");
        TuiStates.printAskLine(this);
    }

    @Override
    public @NotNull TuiStates getState() {
        return TuiStates.SETTING_NAME;
    }

}
