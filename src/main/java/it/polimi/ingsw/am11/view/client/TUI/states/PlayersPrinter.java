package it.polimi.ingsw.am11.view.client.TUI.states;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.ArrayList;
import java.util.List;

public class PlayersPrinter {

    public static void printPlayers(MiniGameModel model) {
        //Players INFO: Name, color, points, isStarte

        String startingPlayer = model.getStartingPlayer();
        List<String> playersList = new ArrayList<>(model.getplayers());


        String lineStartingPlayer = spaces(7) + startingPlayer;
        String line1 = baseLine(false).repeat(playersList.size());
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        String line5 = baseLine(true).repeat(playersList.size());
        String line6 = "";

        if (! startingPlayer.isEmpty()) {
            List<String> startingPlayerLines = infoLine(startingPlayer,
                                                        model.getCliPlayer(
                                                                startingPlayer).getColor(),
                                                        model.getCliPlayer(
                                                                startingPlayer).getPoints());
            line2 = new StringBuilder(startingPlayerLines.get(0));
            line3 = new StringBuilder(startingPlayerLines.get(1));
            line4 = new StringBuilder(startingPlayerLines.get(2));

            if (startingPlayer.equals(model.getCurrentTurn())) {
                line6 = isTurn(1);
            }
        }

        int i = 1;
        for (String player : playersList) {
            if (! player.equals(startingPlayer)) {
                List<String> playerLines = infoLine(player,
                                                    model.getCliPlayer(player).getColor(),
                                                    model.getCliPlayer(player).getPoints());
                line2.append(playerLines.get(0));
                line3.append(playerLines.get(1));
                line4.append(playerLines.get(2));

                i++;
                if (player.equals(model.getCurrentTurn())) {
                    line6 = isTurn(i);
                }
            }
        }

        System.out.println(lineStartingPlayer);
        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
        System.out.println(line6);


//        System.out.println("""
//                                          STARTER
//                                   1╔═══╤═══════════╤═══╗    ╔═══╤═══════════╤═══╗
//                                   2║      Franco       ║    ║ W │           │ W ║
//                                   3╟       BLUE        ╢    ╟───┘           └───╢
//                                   4║        3          ║    ║                   ║
//                                   5╚═══╧═══════════╧═══╝    ╚═══╧═══════════╧═══╝
//                                   6      Playing ^
//                                   """);
    }

    public static String spaces(int num) {
        return " ".repeat(Math.max(0, num));
    }

    public static String baseLine(boolean isBottom) {
        if (isBottom) {
            return "╚═══════════════════╝";
        } else {
            return "╔═══════════════════╗";
        }
    }

    public static List<String> infoLine(String nick, PlayerColor color, int points) {
        List<String> result = new ArrayList<>(2);
        String names;
        String colors;
        String point;
        if (nick.length() > 19) {
            names = "║" + nick.substring(0, 19) + "║";
        } else {
            names = "║"
                    + spaces((19 - nick.length()) / 2)
                    + nick
                    + spaces((int) Math.ceil((19 - nick.length()) / 2.0))
                    + "║";
        }
        colors = "║"
                 + spaces((19 - color.toString().length()) / 2)
                 + color.toString().length()
                 + spaces((int) Math.ceil((19 - color.toString().length()) / 2.0))
                 + "║";
        if (points < 10) {
            point = "║" + spaces(9) + points + spaces(9) + "║";
        } else {
            point = "║" + spaces(9) + points + spaces(8) + "║";
        }


        result.add(names);
        result.add(colors);
        result.add(point);

        return result;
    }

    public static String isTurn(int val) {
        String word = "Playing ^";
        String spaces = spaces(21).repeat(val - 1) + spaces(6);
        return spaces + word;
    }

}
