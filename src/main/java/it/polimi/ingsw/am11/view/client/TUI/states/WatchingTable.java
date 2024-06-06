package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class WatchingTable extends TUIState {
    static final String askToSee = "What you wanna do? Go back? >>> \033[K";
    static final String askLine = "Try to ask something >>> \033[K";
    static final String askForCommand = "What you wanna do? Draw a card? >>> \033[K";
    private static final String helpDraw = "HELP: draw <cardId/gold/resource> \033[K";
    private static final String helpGet = "GET: get <table/[nickname]> \033[K";
    private static String gameStatus = "";
    private boolean alreadyError = false;

    public WatchingTable(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            askSpecLine();
            return;
        }

        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            askSpecLine();
            return;
        }

        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();
            case "exit" -> Actuator.close();
            case "get" -> get(actuator, parser);
            case "back" -> actuator.goBack();
            case "msg", "send" -> {
                String note;
                try {
                    note = Chat.chatter(actuator, Arrays.copyOfRange(args, 1, args.length),
                                        model.getPlayers());
                } catch (ParsingErrorException e) {
                    errorsHappensEvenTwice("ERROR: " + e.getMessage());
                    alreadyError = true;
                    askSpecLine();
                    return;
                }
                if (! note.isEmpty()) {
                    errorsHappensEvenTwice(note);
                    alreadyError = true;
                    askSpecLine();
                } else {
                    System.out.print("\033[F" + "\033[K");
                    askSpecLine();
                }
            }
            case "draw", "pick" -> {
                assert model.getCurrentTurn() != null;
                if (model.getCurrentTurn().equals(model.myName())) {
                    if (model.getiPlaced()) {
                        draw(actuator, parser);
                    } else {
                        errorsHappensEvenTwice("ERROR: Before drawing a card you have to place " +
                                               "one");
                        alreadyError = true;
                        System.out.print(askToSee);
                    }
                } else {
                    errorsHappensEvenTwice("ERROR: It's not your turn");
                    alreadyError = true;
                    System.out.print(askLine);
                }
            }
            default -> {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid option");
                alreadyError = true;
                askSpecLine();
            }
        }

    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {

        if (model.table().getStatus().equals(GameStatus.ARMAGEDDON)) {
            gameStatus = "ATTENTION: the game is going to end in 2 rounds!";
        } else if (model.table().getStatus().equals(GameStatus.LAST_TURN)) {
            gameStatus = "ATTENTION: last round!";
        }

        ConsUtils.clear();

        assert model.getCurrentTurn() != null;
        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, draw a card...
                                           """ + gameStatus + """
                                           ++++++++++++++++++++++++++++
                                           \s""");

            } else {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, you have to place a card...
                                           """ + gameStatus + """
                                           ++++++++++++++++++++++++++++
                                           \s""");
            }
            CardPrinter.printWaitingForTrn(model);
        } else {
            System.out.println("""
                                       ++++++++++++++++++++++++++++
                                       \s
                                        STATUS: It's not your turn, please wait...
                                       """ + gameStatus + """
                                       ++++++++++++++++++++++++++++
                                       \s""");

            CardPrinter.printWaitingForTrn(model);
        }

        if (dueToEx) {
            assert exception != null;
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }
        askSpecLine();
    }

    @Override
    public TuiStates getState() {
        return TuiStates.WATCHING_TABLE;
    }

    private static @NotNull ArgParser setUpOptions() {
        return new ArgParser();
    }

    private void askSpecLine() {
        assert model.getCurrentTurn() != null;
        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                System.out.print(askForCommand);
            } else {
                System.out.print(askToSee);
            }
        } else {
            System.out.print(askLine);
        }
    }

    private void errorsHappensEvenTwice(String text) {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
        }
        System.out.println("\033[F" + "\033[K" + text);
    }

    private void get(Actuator actuator, ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        Set<String> playerList = model.getPlayers();

        if (positionalArgs.size() == 2) {
            if (playerList.contains(positionalArgs.get(1))) {
                String word = positionalArgs.get(1);
                String[] args = {"get", word};
                actuator.setTuiState(TuiStates.WATCHING_FIELD);
                actuator.getCurrentTuiState().passArgs(actuator, args);
                return;
            }
            String secondWord = parser.getPositionalArgs().get(1);
            switch (secondWord.toLowerCase()) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpGet);
                    alreadyError = true;
                    askSpecLine();
                }
                case "table" -> {
                    errorsHappensEvenTwice("You already are watching the table");
                    alreadyError = true;
                    System.out.print(askLine);
                }
                default -> {
                    errorsHappensEvenTwice("ERROR: " + secondWord + " is not a valid option");
                    alreadyError = true;
                    askSpecLine();
                }
            }
        }
    }

    private void draw(Actuator actuator, ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        if (positionalArgs.size() != 2) {
            errorsHappensEvenTwice("ERROR: too many arguments for draw command");
            alreadyError = true;
            System.out.print(askForCommand);
            return;
        }

        String secondWord = parser.getPositionalArgs().get(1);
        try {
            switch (secondWord.toLowerCase()) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpDraw);
                    alreadyError = true;
                    System.out.print(askForCommand);
                    return;
                }
                case "gold" -> {
                    actuator.draw(null, PlayableCardType.GOLD);
                    return;
                }
                case "resource", "resources", "res" -> {
                    actuator.draw(null, PlayableCardType.RESOURCE);
                    return;
                }
            }
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        int val;
        try {
            val = Integer.parseInt(secondWord);
            if (val < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            errorsHappensEvenTwice(
                    "ERROR: " + secondWord + " is neither a valid command nor a valid " +
                    "cardId");
            alreadyError = true;
            System.out.print(askForCommand);
            return;
        }

        if (model.table().getShownCards().contains(val)) {
            try {
                actuator.draw(val, null);
            } catch (IllegalCardBuildException e) {
                errorsHappensEvenTwice("ERROR" + secondWord + " is not a valid cardId");
                alreadyError = true;
                System.out.print(askForCommand);
            }
        } else {
            errorsHappensEvenTwice("ERROR: " + secondWord + " is not cardId in the table");
            alreadyError = true;
            System.out.print(askForCommand);
        }


    }
}
