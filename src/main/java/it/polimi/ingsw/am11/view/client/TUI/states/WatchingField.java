package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.printers.FieldPrinter;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class WatchingField extends TUIState {
    static final String askToSee = "What you wanna do? Go back? >>> \033[K";
    static final String askLine = "Try to ask something >>> \033[K";
    static final String askForCommand = "What you wanna do? Place a card? >>> \033[K";
    private static final String helpPlace = "HELP: place <x> <y> <cardId> <front/retro> \033[K";
    private static final String helpGet = "GET: get <table/[nickname]/chat> \033[K";
    private static @NotNull String gameStatus = "";
    private boolean alreadyError = false;
    private @Nullable String currentFieldShowed = "";

    protected WatchingField(@NotNull MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(@Nullable Actuator actuator, String @NotNull [] args) {
        ArgParser parser = setUpOptions();

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
            return;
        }

        //Parse
        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }

        if (actuator == null) {
            String firstArg = parser.getPositionalArgs().getFirst();
            String secondArg = parser.getPositionalArgs().get(1);

            if (firstArg.equals("notify") && model.getPlayers().contains(secondArg)) {
                assert currentFieldShowed != null;
                if (currentFieldShowed.equals(secondArg)) {
                    refresh();
                }
            } else {
                throw new RuntimeException();
            }
            return;
        }


        String word = parser.getPositionalArgs().getFirst();
        switch (word.toLowerCase()) {
            case "help" -> Actuator.help();

            case "exit" -> Actuator.close();

            case "get" -> get(actuator, parser);

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
                    System.out.print("\033[F" + "\033[K");
                    TuiStates.printAskLine(this);
                }
            }
            case "place" -> {
                assert model.getCurrentTurn() != null;
                if (model.getCurrentTurn().equals(model.myName())) {
                    if (model.getiPlaced()) {
                        errorsHappensEvenTwice("You already placed a card, now you need to draw " +
                                               "one");
                        alreadyError = true;
                        TuiStates.printAskLine(this);
                    } else {
                        place(actuator, parser);
                    }
                } else {
                    errorsHappensEvenTwice("It's not your turn");
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                }
            }
            case "back" -> actuator.goBack();

            default -> {
                errorsHappensEvenTwice("ERROR: " + word + " is not a valid option");
                alreadyError = true;
                TuiStates.printAskLine(this);
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

        currentFieldShowed = model.getCurrentTurn();

        assert model.getCurrentTurn() != null;
        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                InfoBarPrinter.printInfoBar("", "STATUS: It's your turn, you have to draw a " +
                                                "card...", gameStatus);
            } else {
                InfoBarPrinter.printInfoBar("", "STATUS: It's your turn, place a card...",
                                            gameStatus);
            }
            System.out.println("This is your field, quite impressive:");
            FieldPrinter.render(model.getCliPlayer(model.getCurrentTurn()).getField(), true);
            try {
                CardPrinter.printHand(new ArrayList<>(
                        model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
            } catch (IllegalCardBuildException e) {
                throw new RuntimeException(e);
            }
        } else {
            InfoBarPrinter.printInfoBar("", "STATUS: It's not your turn, please wait...",
                                        gameStatus);

            System.out.println("This is " + model.getCurrentTurn() + " field:");
            FieldPrinter.render(model.getCliPlayer(model.getCurrentTurn()).getField(), false);
        }

        if (dueToEx) {
            assert exception != null;
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }
        TuiStates.printAskLine(this);
    }

    @Override
    public @NotNull TuiStates getState() {
        return TuiStates.WATCHING_FIELD;
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

    private void refresh() {
        if (model.table().getStatus().equals(GameStatus.ARMAGEDDON)) {
            gameStatus = "ATTENTION: the game is going to end in 2 rounds!";
        } else if (model.table().getStatus().equals(GameStatus.LAST_TURN)) {
            gameStatus = "ATTENTION: last round!";
        }

        ConsUtils.clear();

        assert model.getCurrentTurn() != null;
        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                InfoBarPrinter.printInfoBar("",
                                            "STATUS: It's your turn, you have to draw a card...",
                                            gameStatus);
            } else {
                InfoBarPrinter.printInfoBar("", "STATUS: It's your turn, place a card...",
                                            gameStatus);
            }
        } else {
            InfoBarPrinter.printInfoBar("", "STATUS: It's not your turn, please wait...",
                                        gameStatus);
        }

        if (model.myName().equals(currentFieldShowed)) {
            System.out.println("This is your field, quite impressive:");
            FieldPrinter.render(model.getCliPlayer(currentFieldShowed).getField(),
                                true);
            try {
                CardPrinter.printHand(new ArrayList<>(
                        model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
            } catch (IllegalCardBuildException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("This is " + currentFieldShowed + " field:");
            FieldPrinter.render(model.getCliPlayer(currentFieldShowed).getField(),
                                false);
        }

        TuiStates.printAskLine(this);
    }

    private void get(@NotNull Actuator actuator, @NotNull ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        Set<String> playerList = model.getPlayers();

        String secondWord = parser.getPositionalArgs().get(1);

        if (positionalArgs.size() == 2) {
            if (playerList.contains(secondWord)) {
                currentFieldShowed = secondWord;
                refresh();
                return;
            }

            switch (secondWord.toLowerCase()) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpGet);
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                }
                case "table" -> actuator.setTuiState(TuiStates.WATCHING_TABLE);
                case "chat" -> actuator.setTuiState(TuiStates.CHAT);
                default -> {
                    errorsHappensEvenTwice("ERROR: " + secondWord + " is not a valid option");
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                }
            }
        } else {
            errorsHappensEvenTwice("ERROR: wrong number of arguments for get command");
            alreadyError = true;
            TuiStates.printAskLine(this);
        }

    }

    private void place(@NotNull Actuator actuator, @NotNull ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        if (positionalArgs.size() == 2) {
            String secondWord = parser.getPositionalArgs().get(1);
            switch (secondWord.toLowerCase()) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpPlace);
                    alreadyError = true;
                    TuiStates.printAskLine(this);
                    return;
                }
            }
        }

        if (positionalArgs.size() != 5) {
            errorsHappensEvenTwice("ERROR: wrong number of arguments for place command");
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }
        int x;
        int y;
        int cardId;
        String frontOrRetro = parser.getPositionalArgs().get(4).toLowerCase();
        try {
            x = Integer.parseInt(positionalArgs.get(1));
            y = Integer.parseInt(positionalArgs.get(2));
            cardId = Integer.parseInt(positionalArgs.get(3));
        } catch (NumberFormatException e) {
            errorsHappensEvenTwice("ERROR: Invalid arguments, <x>, <y> and <cardId> have to be an" +
                                   " integer");
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }

        if (! model.getCliPlayer(model.myName()).getSpace().getPlayerHand().contains(cardId)) {
            errorsHappensEvenTwice("ERROR: You don't have this card in your hand");
            alreadyError = true;
            TuiStates.printAskLine(this);
            return;
        }


        switch (frontOrRetro.toLowerCase()) {
            case "front" -> {
                actuator.place(x, y, cardId, false);
                model.setIPlaced(true);
            }
            case "retro" -> {
                actuator.place(x, y, cardId, true);
                model.setIPlaced(true);
            }
            default -> {
                errorsHappensEvenTwice("ERROR: Invalid argument, specify front or retro ");
                alreadyError = true;
                TuiStates.printAskLine(this);
            }
        }
    }
}


