package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardArchitect;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.smartcardio.Card;
import java.util.ArrayList;

public class ChoosingObj extends TUIState {
    private static final String askForObj = "Choose one of the objectives above >>> \033[K";
    private boolean alreadyError = false;
    private boolean isBlocked = false;


    public ChoosingObj(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();
        int val;

        if (isBlocked) {
            System.out.print("\033[A" + askForObj);
            return;
        }

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F" + askForObj);
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
            System.out.print(askForObj);
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
                                   "cardId");
            alreadyError = true;
            System.out.print(askForObj);
            return;
        }

        isBlocked = true;
        actuator.setObjective(val);
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        isBlocked = false;

        ConsUtils.clear();
        System.out.println("""
                                   ++++++++++++++++++++++++++++
                                   \s
                                    STATUS: Choosing the personal objective...
                                   \s
                                   ++++++++++++++++++++++++++++
                                   \s""");

        System.out.println("You received this objectives:");

        try {
            CardPrinter.printObjectives(new ArrayList<>(model.getCliPlayer(model.myName()).getSpace().getCandidateObjectives()));
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }

        System.out.print(askForObj);
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
