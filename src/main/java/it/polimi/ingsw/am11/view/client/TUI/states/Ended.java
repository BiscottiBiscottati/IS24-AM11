package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.printers.PlayersPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Ended extends TUIState {
    static String tellNothing = "Nothing to do here, you can exit the game >>> \033[K";
    private static String infoBar = "STATUS: The game has ended";
    private boolean alreadyError = false;

    public Ended(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();
        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> {
                Actuator.help();
                return;
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
                    return;
                }
            }
            //Maybe a return is needed after help
            case "exit" -> Actuator.close();
            default -> {
                errorsHappensEvenTwice("ERROR: Command not found");
                TuiStates.printAskLine(this);
            }
        }
    }

    private static @NotNull ArgParser setUpOptions() {
        return new ArgParser();
    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        System.out.println("This is the final leaderboard:");
        PlayersPrinter.printLeaderboard(model.getFinalLeaderboard());
    }

    @Override
    public TuiStates getState() {
        return TuiStates.ENDED;
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

        if (word.toLowerCase().equals("chat")) {
            actuator.setTuiState(TuiStates.CHAT);
            return;
        }

    }


}
