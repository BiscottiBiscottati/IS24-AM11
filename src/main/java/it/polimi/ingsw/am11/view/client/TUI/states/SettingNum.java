package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingNum extends TUIState {
    static final String askForNum = "How many players will play? >>> \033[K";
    private static final String infoBar = "STATUS: Choosing the number of players...";
    private boolean alreadyError = false;
    private boolean isBlocked = false;

    public SettingNum(@NotNull MiniGameModel model) {
        super(model);
    }

    private static void errorsHappens(String text) {
        System.out.println("\033[F" + "\033[K" + text);
    }

    @Override
    public void passArgs(@NotNull Actuator actuator, String @NotNull [] args) {
        ArgParser parser = setUpOptions();
        int val;

        if (isBlocked) {
            System.out.print("\033[A");
            return;
        }

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
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
            TuiStates.printAskLine(this);
            return;
        }

        isBlocked = true;
        actuator.setNumOfPlayers(val);
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        isBlocked = false;
        alreadyError = false;

        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        if (dueToEx) {
            assert exception != null;
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        }
        System.out.println("You are the moderator of the game, you have to choose the number of " +
                           "players:");
        TuiStates.printAskLine(this);
    }

    @Override
    public @NotNull TuiStates getState() {
        return TuiStates.SETTING_NUM;
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
