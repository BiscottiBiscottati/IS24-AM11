package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.utils.ArgParser;
import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Chat extends TUIState {
    private static final String askForMsg = "Write: >>> \033[K";
    private static final String askForHelp = "Use /mgs <nickname> to send a private message, use " +
                                             "/back to exit chat, use /exit to exit the game >>> " +
                                             "\033[K";
    private boolean alreadyError = false;

    public Chat(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F" + askForMsg);
            return;
        }

        String note;
        try {
            note = chatter(actuator, args, model.getplayers());
        } catch (ParsingErrorException e) {
            errorsHappensEvenTwice("ERROR: " + e.getMessage());
            alreadyError = true;
            System.out.print(askForMsg);
            return;
        }

        if (! note.isEmpty()) {
            errorsHappensEvenTwice(note);
            alreadyError = true;
            System.out.print(askForMsg);
            return;
        }


    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {

        ConsUtils.clear();
        System.out.println("""
                                   ++++++++++++++++++++++++++++
                                   \s
                                    CHAT: Talk with your enemies...
                                   \s
                                   ++++++++++++++++++++++++++++
                                   \s""");

        model.getChatMessages().forEach(System.out::println);

        if (dueToEx) {
            System.out.println("ERROR: " + exception.getMessage());
            alreadyError = true;
        } else {
            alreadyError = false;
        }

        System.out.print(askForMsg);
    }

    private void errorsHappensEvenTwice(String text) {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
        }
        System.out.println("\033[F" + "\033[K" + text);
    }

    public static String chatter(Actuator actuator, String[] args, Set<String> players)
    throws ParsingErrorException {
        //This method will receive already checked arguments
        switch (args[0].toLowerCase()) {
            case "/help" -> {
                return askForHelp;
            }
            case "/exit" -> {
                Actuator.close();
                return "";
            }
            case "/back" -> {
                actuator.goBack();
                return "";
            }
            case "/msg" -> {
                if (args.length < 2) {
                    throw new ParsingErrorException("Not enough arguments");
                }
                if (args.length < 3) {
                    return "";
                }

                if (players.contains(args[1])) {
                    StringBuilder sb = new StringBuilder(8);
                    int length = args.length;
                    for (int i = 2; i < length; i++) {
                        sb.append(args[i]);
                    }
                    String msg = sb.toString();
                    actuator.sendPrivateMessage(args[1], msg);
                    //System.out.print(askForMsg);
                    return "";
                } else {
                    throw new ParsingErrorException("Player not found");
                }
            }
            default -> {
                StringBuilder sb = new StringBuilder(8);
                for (String arg : args) {
                    sb.append(arg);
                }
                String msg = sb.toString();
                actuator.sendChatMessage(msg);
                //System.out.print(askForMsg);
                return "";
            }
        }
    }

}
