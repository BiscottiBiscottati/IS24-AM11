package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.printers.FieldPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WatchingField extends TUIState {
    public static final String askToSee = "What you wanna do? Go back? >>> \033[K";
    public static final String askLine = "Try to ask something >>> \033[K";
    private static final String askForCommand = "What you wanna do? Place a card? >>> \033[K";
    private static final String helpPlace = "HELP: place <x> <y> <cardId> <front/retro> \033[K";
    private static final String helpGet = "GET: get <table/[nickname]> \033[K";
    private boolean alreadyError = false;
    private String currentFieldShowed = "";

    protected WatchingField(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(@Nullable Actuator actuator, String[] args) {
        ArgParser parser = setUpOptions();

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            askSpecLine();
            return;
        }

        //Parse
        try {
            parser.parse(args);
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            askSpecLine();
            return;
        }

        if (actuator == null) {
            String firstArg = parser.getPositionalArgs().getFirst();
            String secondArg = parser.getPositionalArgs().get(1);

            if (firstArg.equals("notify") && model.getplayers().contains(secondArg)) {
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
            case "help" -> {
                Actuator.help();
            }
            case "exit" -> {
                Actuator.close();
            }
            case "get" -> {
                get(actuator, parser);
            }
            case "place" -> {
                if (model.getCurrentTurn().equals(model.myName())) {
                    if (model.getiPlaced()) {
                        errorsHappensEvenTwice("You already placed a card, now you need to draw " +
                                               "one");
                        alreadyError = true;
                        System.out.print(askForCommand);
                    } else {
                        place(actuator, parser);
                    }
                } else {
                    errorsHappensEvenTwice("It's not your turn");
                    alreadyError = true;
                    System.out.print(askToSee);
                }
            }
            case "back" -> {
                if (model.getCurrentTurn().equals(model.myName())) {
                    if (model.getiPlaced()) {
                        actuator.setTuiState(TuiStates.WATCHING_TABLE);
                    } else if (! currentFieldShowed.equals(model.myName())) {
                        restart(false, null);
                    } else {
                        errorsHappensEvenTwice("You already are on the root page, you need to " +
                                               "place a card");
                        alreadyError = true;
                        System.out.print(askForCommand);
                    }
                } else {
                    actuator.setTuiState(TuiStates.WATCHING_TABLE);
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
        ConsUtils.clear();

        currentFieldShowed = model.getCurrentTurn();

        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, you have to draw a card...
                                           \s
                                           ++++++++++++++++++++++++++++
                                           \s""");
            } else {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, place a card...
                                           \s
                                           ++++++++++++++++++++++++++++
                                           \s""");
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
            System.out.println("""
                                       ++++++++++++++++++++++++++++
                                       \s
                                        STATUS: It's not your turn, please wait...
                                       \s
                                       ++++++++++++++++++++++++++++
                                       \s""");

            System.out.println("This is " + model.getCurrentTurn() + " field:");
            FieldPrinter.render(model.getCliPlayer(model.getCurrentTurn()).getField(), false);
        }

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }
        askSpecLine();
    }

    private static @NotNull ArgParser setUpOptions() {
        return new ArgParser();
    }

    private void askSpecLine() {
        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                System.out.print(askToSee);
            } else {
                System.out.print(askForCommand);
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

    private void refresh() {
        String name = currentFieldShowed;
        ConsUtils.clear();

        if (model.getCurrentTurn().equals(model.myName())) {
            if (model.getiPlaced()) {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, you have to draw a card...
                                           \s
                                           ++++++++++++++++++++++++++++
                                           \s""");
            } else {
                System.out.println("""
                                           ++++++++++++++++++++++++++++
                                           \s
                                            STATUS: It's your turn, you have to place a card...
                                           \s
                                           ++++++++++++++++++++++++++++
                                           \s""");
            }
            if (model.myName().equals(name)) {
                System.out.println("This is your field, quite impressive:");
                FieldPrinter.render(model.getCliPlayer(name).getField(),
                                    true);
                try {
                    CardPrinter.printHand(new ArrayList<>(
                            model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("This is " + name + " field:");
                FieldPrinter.render(model.getCliPlayer(name).getField(),
                                    false);
            }
        } else {
            System.out.println("""
                                       ++++++++++++++++++++++++++++
                                       \s
                                        STATUS: It's not your turn, please wait...
                                       \s
                                       ++++++++++++++++++++++++++++
                                       \s""");

            if (model.myName().equals(name)) {
                System.out.println("This is your field, quite impressive:");
                FieldPrinter.render(model.getCliPlayer(name).getField(),
                                    true);
                try {
                    CardPrinter.printHand(new ArrayList<>(
                            model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("This is " + name + " field:");
                FieldPrinter.render(model.getCliPlayer(name).getField(),
                                    false);
            }
        }
        askSpecLine();
    }

    private void get(Actuator actuator, ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        Set<String> playerList = model.getplayers();

        if (positionalArgs.size() == 2) {
            if (playerList.contains(positionalArgs.get(1))) {
                String name = positionalArgs.get(1);
                currentFieldShowed = name;
                ConsUtils.clear();

                if (model.getCurrentTurn().equals(model.myName())) {
                    if (model.getiPlaced()) {
                        System.out.println("""
                                                   ++++++++++++++++++++++++++++
                                                   \s
                                                    STATUS: It's your turn, you have to draw a card...
                                                   \s
                                                   ++++++++++++++++++++++++++++
                                                   \s""");
                    } else {
                        System.out.println("""
                                                   ++++++++++++++++++++++++++++
                                                   \s
                                                    STATUS: It's your turn, you have to place a card...
                                                   \s
                                                   ++++++++++++++++++++++++++++
                                                   \s""");
                    }
                    if (model.myName().equals(name)) {
                        System.out.println("This is your field, quite impressive:");
                        FieldPrinter.render(model.getCliPlayer(name).getField(),
                                            true);
                        try {
                            CardPrinter.printHand(new ArrayList<>(
                                    model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
                        } catch (IllegalCardBuildException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("This is " + name + " field:");
                        FieldPrinter.render(model.getCliPlayer(name).getField(),
                                            false);
                    }
                } else {
                    System.out.println("""
                                               ++++++++++++++++++++++++++++
                                               \s
                                                STATUS: It's not your turn, please wait...
                                               \s
                                               ++++++++++++++++++++++++++++
                                               \s""");

                    if (model.myName().equals(name)) {
                        System.out.println("This is your field, quite impressive:");
                        FieldPrinter.render(model.getCliPlayer(name).getField(),
                                            true);
                        try {
                            CardPrinter.printHand(new ArrayList<>(
                                    model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
                        } catch (IllegalCardBuildException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("This is " + name + " field:");
                        FieldPrinter.render(model.getCliPlayer(name).getField(),
                                            false);
                    }
                }
                askSpecLine();
            }
            String secondWord = parser.getPositionalArgs().get(1);
            switch (secondWord) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpGet);
                    alreadyError = true;
                    askSpecLine();
                }
                case "table" -> {
                    actuator.setTuiState(TuiStates.WATCHING_TABLE);
                }
            }
        }

    }

    private void place(Actuator actuator, ArgParser parser) {
        List<String> positionalArgs = parser.getPositionalArgs();

        if (positionalArgs.size() == 2) {
            String secondWord = parser.getPositionalArgs().get(1);
            switch (secondWord) {
                case "?", "help" -> {
                    errorsHappensEvenTwice(helpPlace);
                    alreadyError = true;
                    System.out.print(askForCommand);
                    return;
                }
            }
        }

        if (positionalArgs.size() != 5) {
            errorsHappensEvenTwice("ERROR: wrong number of arguments for place command");
            alreadyError = true;
            System.out.println(askForCommand);
            return;
        }
        int x = 0;
        int y = 0;
        int cardid = 0;
        String frontOrRetro = parser.getPositionalArgs().get(4);
        try {
            x = Integer.parseInt(positionalArgs.get(1));
            y = Integer.parseInt(positionalArgs.get(2));
            cardid = Integer.parseInt(positionalArgs.get(3));
        } catch (NumberFormatException e) {
            errorsHappensEvenTwice("ERROR: Invalid arguments, <x>, <y> and <cardId> have to be an" +
                                   " integer");
            alreadyError = true;
            System.out.println(askForCommand);
            return;
        }
        switch (frontOrRetro) {
            case "front" -> {
                actuator.place(x, y, cardid, false);
                model.setiPlaced(true);
            }
            case "retro" -> {
                actuator.place(x, y, cardid, true);
                model.setiPlaced(true);
            }
            default -> {
                errorsHappensEvenTwice("ERROR: Invalid argument, specify front or retro ");
                alreadyError = true;
                System.out.println(askForCommand);
            }
        }
    }
}


