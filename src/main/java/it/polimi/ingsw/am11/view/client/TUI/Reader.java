package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.chat.ClientChatController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.Scanner;

public class Reader {

    private final Scanner input;
    private final TuiUpdater tuiUpdater;
    private final MiniGameModel model;
    private String command;
    private String word;
    private CltToNetConnector connector;
    private ClientChatController chatController;

    public Reader(MiniGameModel model, TuiUpdater tuiUpdater) {
        this.input = new Scanner(System.in);
        this.model = model;
        this.tuiUpdater = tuiUpdater;
    }

    private static void notRightTime() {
        System.out.println("There's a Time and Place for Everything, But Not Now!");
    }

    public CltToNetConnector listenForConnect() {
        command = input.nextLine();
        command = command.replaceAll("\\s+", " ");
        command = command.strip();
        command = command.toLowerCase();
        Scanner args = new Scanner(command);
        word = args.next();

        switch (word) {
            case "connect": {
                connect(args);
                break;
            }
            case "help": {
                help();
                break;
            }
            default: {
                System.out.println("You need to connect to the server, please use the connect " +
                                   "command");
                System.out.println("connect type-of-connection ip-address port");
            }
        }
        return connector;
    }

    private void connect(Scanner args) {
        String type;
        String ip;
        int port;
        if (args.hasNext()) {
            type = args.next();
        } else {
            invalidArguments();
            return;
        }
        switch (type) {
            case "rmi": {
                if (args.hasNext()) {
                    ip = args.next();
                } else {
                    invalidArguments();
                    return;
                }
                if (args.hasNextInt()) {
                    port = args.nextInt();
                } else {
                    invalidArguments();
                    break;
                }
                if (args.hasNext()) {
                    invalidArguments();
                    break;
                }
                //TODO
                break;
            }
            case "socket": {
                if (args.hasNext()) {
                    ip = args.next();
                } else {
                    invalidArguments();
                    return;
                }
                if (args.hasNextInt()) {
                    port = args.nextInt();
                } else {
                    invalidArguments();
                    break;
                }
                if (args.hasNext()) {
                    invalidArguments();
                    break;
                }
                System.out.print("");
                //FIXME
                ClientSocket clientSocket = new ClientSocket(ip, port, tuiUpdater);
                connector = clientSocket.getConnector();
                //TODO add chat
                break;
            }
            default: {
                invalidArguments();
            }
        }
    }

    private static void help() {
        System.out.println("Commands are not case sensitive");
        System.out.println("These are all possible commands with their attributes:");

        System.out.println("connect type-of-connection ip-address port");
        System.out.println("setNick nickname");
        System.out.println("setNumOfPlayers number");
        System.out.println("setStarter front/retro");
        System.out.println("setObjective cardID");
        System.out.println("getPlayers");
        System.out.println("getMyHand");
        System.out.println("getField");
        System.out.println("getObjectives");
        System.out.println("getCandidateObjectives");
        System.out.println("getTable");
        System.out.println("help");


        System.out.println("place X Y cardID front/retro");
        System.out.println("draw visible/deck res/gold cardID");
    }

    private static void invalidArguments() {
        System.out.println("Invalid arguments for this command, type help to get a list of all " +
                           "commands and arguments");
    }

    public void listenForNick() {
        command = input.nextLine();
        command = command.replaceAll("\\s+", " ");
        command = command.strip();
        Scanner args = new Scanner(command);
        word = args.next();
        word = word.toLowerCase();

        switch (word) {
            case "setnick": {
                setNick(args);
                break;
            }
            case "help": {
                help();
                break;
            }
            default: {
                System.out.println("You are connected to the server, now choose your nickname:");
                System.out.println("setnick nickname");
            }
        }
    }

    private void setNick(Scanner args) {
        String nickname;
        if (args.hasNext()) {
            nickname = args.next();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
        } else {
            connector.connect(nickname);
        }
    }

    public void listenForNumOfP() {
        command = input.nextLine();
        command = command.replaceAll("\\s+", " ");
        command = command.strip();
        command = command.toLowerCase();
        Scanner args = new Scanner(command);
        word = args.next();

        switch (word) {
            case "setnumofplayers": {
                setNumOfPlayers(args);
                break;
            }
            case "help": {
                help();
                break;
            }
            default: {
                System.out.println("You are the game moderator, choose the number of players for " +
                                   "this game:");
                System.out.println("setnumofplayers number");
            }
        }

    }

    private void setNumOfPlayers(Scanner args) {
        int num;
        if (args.hasNextInt()) {
            num = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
        } else {
            connector.setNumOfPlayers(num);
        }
    }

    public void listen() {

        command = input.nextLine();
        command = command.replaceAll("\\s+", " ");
        command = command.strip();
        command = command.toLowerCase();
        Scanner args = new Scanner(command);
        word = args.next();

        switch (word) {
            case "connect": {
                System.out.println("You are already connected");
                break;
            }
            case "setnick": {
                System.out.println("You already hava a name, it's good enough for you");
                break;
            }
            case "setnumofplayers": {
                System.out.println("Number of players already set");
                break;
            }
            case "setobjective": {
                if (! model.table().getStatus().equals(GameStatus.CHOOSING_OBJECTIVES)) {
                    System.out.println("There's a time and place for everything, but not now");
                }
                setObjective(args);
                break;
            }
            case "setstarter": {
                if (! model.table().getStatus().equals(GameStatus.CHOOSING_OBJECTIVES)) {
                    System.out.println("There's a time and place for everything, but not now");
                }
                setStarter(args);
                break;
            }
            case "place": {
                if (! model.getCurrentTurn().equals(model.myName())) {
                    System.out.println("It's not your turn");
                    break;
                }
                if (model.getiPlaced()) {
                    System.out.println("You already placed");
                    break;
                }
                place(args);
                break;
            }
            case "draw": {
                if (! model.getCurrentTurn().equals(model.myName())) {
                    System.out.println("It's not your turn");
                }
                if (! model.getiPlaced()) {
                    System.out.println("You first need to place a card");
                }
                draw(args);
                break;
            }
            case "getplayers": {
                model.getplayers().forEach(System.out::println);
                break;
            }
            case "getmyhand": {
                model.getCliPlayer(model.myName()).getSpace().getPlayerHand().forEach(
                        System.out::println);
                break;
            }
            case "getfield": {
                getthefield(args);
                break;
            }
            case "getobjectives": {
                model.getCliPlayer(model.myName()).getSpace().getPlayerObjective().forEach(
                        System.out::println);
                break;
            }
            case "getcandideteobjectives": {
                model.getCliPlayer(model.myName()).getSpace().getCandidateObjectives().forEach(
                        System.out::print);
                break;
            }
            case "gettable": {
                System.out.println("Here's the table:");
                System.out.println("Decks -> RESOURCE: " +
                                   model.table().getDeckTop(PlayableCardType.RESOURCE) +
                                   "; GOLD: " +
                                   model.table().getDeckTop(PlayableCardType.GOLD));
                System.out.println("Visibles -> " + model.table().getShownCards().toString());
                System.out.println(
                        "Common objectives -> " + model.table().getCommonObjectives().toString());
                System.out.println("Game status -> " + model.table().getStatus().toString());
                break;
            }
            case "chat": {
                chatter(args);
                break;
            }
            case "help": {
                help();
                break;
            }
            default: {
                System.out.println("command not found, type help to get al list of all commands " +
                                   "and arguments");
            }
        }
    }

    private void setObjective(Scanner args) {
        int idCard;
        if (args.hasNextInt()) {
            idCard = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
        } else {
            connector.setPersonalObjective(idCard);
        }
    }

    private void setStarter(Scanner args) {
        String isRetro;
        if (args.hasNext()) {
            isRetro = args.next();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
        } else {
            if (isRetro.equals("retro")) {
                connector.setStarterCard(true);
            } else if (isRetro.equals("front")) {
                connector.setStarterCard(false);
            } else {
                invalidArguments();
            }
        }
    }

    private void place(Scanner args) {
        int x;
        int y;
        int cardId;
        String frontOrRetro;
        boolean isRetro;
        //check x
        if (args.hasNextInt()) {
            x = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        //check y
        if (args.hasNextInt()) {
            y = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        //check cardId
        if (args.hasNextInt()) {
            cardId = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        //check retro
        if (args.hasNext()) {
            frontOrRetro = args.next();
            if (frontOrRetro.equals("retro")) {
                isRetro = true;
            } else if (frontOrRetro.equals("front")) {
                isRetro = false;
            } else {
                invalidArguments();
                return;
            }
        } else {
            invalidArguments();
            return;
        }
        //call method
        if (args.hasNext()) {
            invalidArguments();
        } else {
            Position pos = new Position(x, y);
            connector.placeCard(pos, cardId, isRetro);
        }
    }

    public void draw(Scanner args) {
        String where;
        boolean fromVisible;
        String type;
        PlayableCardType ptype;
        int cardID;
        //Choose from where
        if (args.hasNext()) {
            where = args.next();
        } else {
            invalidArguments();
            return;
        }
        switch (where) {
            case "visible": {
                fromVisible = true;
                break;
            }
            case "deck": {
                fromVisible = false;
                break;
            }
            default: {
                invalidArguments();
                return;
            }
        }
        //Choose type
        if (args.hasNext()) {
            type = args.next();
        } else {
            invalidArguments();
            return;
        }
        switch (type) {
            case "res": {
                ptype = PlayableCardType.RESOURCE;
                break;
            }
            case "gold": {
                ptype = PlayableCardType.GOLD;
                break;
            }
            default: {
                invalidArguments();
                return;
            }
        }
        //Choose id
        if (! fromVisible) {
            if (args.hasNext()) {
                invalidArguments();
                return;
            } else {
                connector.drawCard(false, ptype, 1);
                return;
            }
        }
        if (args.hasNextInt()) {
            cardID = args.nextInt();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
            return;
        }
        connector.drawCard(true, ptype, cardID);


    }

    private void getthefield(Scanner args) {
        String name;
        if (args.hasNext()) {
            name = args.next();
        } else {
            invalidArguments();
            return;
        }
        if (args.hasNext()) {
            invalidArguments();
            return;
        }
        System.out.println(model.getCliPlayer(name).getField().toString());
    }

    public void chatter(Scanner args) {
        String text;
        if (args.hasNext()) {
            text = args.nextLine();
        } else {
            invalidArguments();
            return;
        }
        chatController.writeMessage(text);
    }

    public void giveChatController(ClientChatController chatController) {
        this.chatController = chatController;
    }


}
