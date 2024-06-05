package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Waiting extends TUIState {
    public static final String askLine = "To ask is permissible , to reply is polite >>> \033[K";
    private boolean alreadyError = false;

    public Waiting(MiniGameModel model) {
        super(model);
    }

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
            case "help" -> {
                Actuator.help();
                return;
            }
            case "exit" -> Actuator.close();
            case "get" -> {
                if (model.table().getStatus().equals(GameStatus.CHOOSING_STARTERS) ||
                    model.table().getStatus().equals(GameStatus.CHOOSING_OBJECTIVES)) {
                    get(actuator, parser);
                } else {
                    errorsHappensEvenTwice("ERROR: get command is not available now");
                    alreadyError = true;
                    System.out.print(askLine);
                }
            }
            case "msg", "send" -> {
                if (model.table().getStatus().equals(GameStatus.CHOOSING_STARTERS) ||
                    model.table().getStatus().equals(GameStatus.CHOOSING_OBJECTIVES)) {
                    String note;
                    try {
                        note = Chat.chatter(actuator, Arrays.copyOfRange(args, 1, args.length),
                                            model.getplayers());
                    } catch (ParsingErrorException e) {
                        errorsHappensEvenTwice("ERROR: " + e.getMessage());
                        alreadyError = true;
                        System.out.print(askLine);
                        return;
                    }
                    if (! note.isEmpty()) {
                        errorsHappensEvenTwice(note);
                        alreadyError = true;
                        System.out.print(askLine);
                        return;
                    }
                } else {
                    errorsHappensEvenTwice("ERROR: chat is not available now");
                    alreadyError = true;
                    System.out.print(askLine);
                }
            }
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

    private void get(Actuator actuator, ArgParser parser) {
        if (parser.getPositionalArgs().size() < 2) {
            errorsHappensEvenTwice("ERROR: get command requires an argument");
            alreadyError = true;
            System.out.print(askLine);
            return;
        }

        String word = parser.getPositionalArgs().get(1);

        if (word.toLowerCase().equals("chat")) {
            actuator.setTuiState(TuiStates.CHAT);
            return;
        }

    }
}
