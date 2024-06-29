package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import it.polimi.ingsw.am11.view.client.TUI.Actuator;
import it.polimi.ingsw.am11.view.client.TUI.printers.InfoBarPrinter;
import it.polimi.ingsw.am11.view.client.TUI.utils.ConsUtils;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class Chat extends TUIState {
    static final String askForMsg = "Write: >>> \033[K";
    private static final String askForHelp = "Use /mgs <nickname> to send a private message, use " +
                                             "/back to exit chat, use /exit to exit the game" +
                                             "\033[K";
    private static final String infoBar = "CHAT: Talk with your enemies...";
    private boolean alreadyError = false;

    public Chat(MiniGameModel model) {
        super(model);
    }

    @Override
    public void passArgs(Actuator actuator, String[] args) {

        //Empty string
        if (args[0].isEmpty()) {
            System.out.print("\033[F");
            TuiStates.printAskLine(this);
            return;
        }

        String note;
        try {
            note = chatter(actuator, args, model.getPlayers());
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
        }


    }

    @Override
    public void restart(boolean dueToEx, @Nullable Exception exception) {
        ConsUtils.clear();
        InfoBarPrinter.printInfoBar(infoBar);

        model.getChatMessages().forEach(System.out::println);

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
        return TuiStates.CHAT;
    }

    /**
     * This method parse and handle the chat messages
     *
     * @param actuator the actuator to send messages
     * @param args     the arguments, it comprehends the command and the message
     * @param players  the players in the game
     * @return the message to print on the screen
     * @throws ParsingErrorException if the arguments are not correct
     */
    public static String chatter(Actuator actuator, String[] args,
                                 Set<String> players)
    throws ParsingErrorException {
        //This method will receive already checked arguments
        switch (args[0].toLowerCase()) {
            case "/help" -> {
                if (args.length > 1) {
                    throw new ParsingErrorException("Too many arguments");
                }
                return askForHelp;
            }
            case "/exit" -> {
                if (args.length > 1) {
                    throw new ParsingErrorException("Too many arguments");
                }
                Actuator.close();
                return "";
            }
            case "/back" -> {
                if (args.length > 1) {
                    throw new ParsingErrorException("Too many arguments");
                }
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
                        sb.append(args[i]).append(" ");
                    }
                    String msg = sb.toString();
                    actuator.sendPrivateMessage(args[1], msg);
                    return "";
                } else {
                    throw new ParsingErrorException("Player not found");
                }
            }
            default -> {
                StringBuilder sb = new StringBuilder(8);
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String msg = sb.toString();
                actuator.sendChatMessage(msg);
                return "";
            }
        }
    }

    private void errorsHappensEvenTwice(String text) {
        if (alreadyError) {
            System.out.print("\033[F" + "\033[K");
        }
        System.out.println("\033[F" + "\033[K" + text);
    }

}
