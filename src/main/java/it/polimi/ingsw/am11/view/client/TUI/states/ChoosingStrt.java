package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ChoosingStrt extends TUIState {
    static final String askForSide = "Place it on its front or on its retro >>> \033[K";
    private static final String infoBar = "STATUS: Placing the starter card...";
    private boolean alreadyError = false;
    private boolean isBlocked = false;


    public ChoosingStrt(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {

        ArgParser parser = setUpOptions();

        if (isBlocked) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
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
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> {
                if (parser.getPositionalArgs().size() > 1) {
                    errorsHappensEvenTwice("ERROR: Too many arguments, starting from: " +
                                           parser.getPositionalArgs().get(1));
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
                Actuator.help();
            }
            case "exit" -> {
                if (parser.getPositionalArgs().size() > 1) {
                    errorsHappensEvenTwice("ERROR: Too many arguments, starting from: " +
                                           parser.getPositionalArgs().get(1));
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
                Actuator.close();
            }
            case "get" -> {
                get(actuator, parser);
            }
            case "msg", "send" -> {
                String note;
                try {
                    note = Chat.chatter(actuator, Arrays.copyOfRange(args, 1, args.length),
                                        model.getPlayers());
                } catch (ParsingErrorException e) {
                    errorsHappensEvenTwice("ERROR: " + e.getMessage());
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
                if (! note.isEmpty()) {
                    errorsHappensEvenTwice(note);
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                } else {
                    System.out.print("\033[F");
                    TuiStates.printAskLine(this);
                }
            }
            case "front" -> {
                if (parser.getPositionalArgs().size() > 1) {
                    errorsHappensEvenTwice("ERROR: Too many arguments, starting from: " +
                                           parser.getPositionalArgs().get(1));
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
                isBlocked = true;
                actuator.setStarter(false);
            }
            case "retro" -> {
                if (parser.getPositionalArgs().size() > 1) {
                    errorsHappensEvenTwice("ERROR: Too many arguments, starting from: " +
                                           parser.getPositionalArgs().get(1));
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
                isBlocked = true;
                actuator.setStarter(true);
            }
            default -> {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid option");
                alreadyError = true;
                TuiStates.printAskLine(this);
            }
        }
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        isBlocked = false;

        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        System.out.println("You received this starter card:");

        try {
            CardPrinter.printCardFrontAndBack(
                    model.getCliPlayer(model.myName()).getSpace().getStarterCard());
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }

        TuiStates.printAskLine(this);
    }

    @Override
    public TuiStates getState() {
        return TuiStates.CHOOSING_STARTER;
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

    private void get(Actuator actuator, ArgParser parser) {
        if (parser.getPositionalArgs().size() < 2) {
            errorsHappensEvenTwice("ERROR: get command requires an argument");
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }

        String word = parser.getPositionalArgs().get(1);

        if (word.equalsIgnoreCase("chat")) {
            actuator.setTuiState(TuiStates.CHAT);
        }

    }

}
