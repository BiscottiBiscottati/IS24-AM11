package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayersPrinter {

    public static List<String> buildPlayers(MiniGameModel model) {
        //Players INFO: Name, color, points, isStarter

        //Size: 96*

        String startingPlayer = model.getStartingPlayer();
        List<String> playersList = new ArrayList<>(model.getplayers());
        List<String> res = new ArrayList<>();


        String lineStartingPlayer = "";
        String line1 = baseLine(false).repeat(playersList.size());
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();
        StringBuilder line4 = new StringBuilder();
        String line5 = baseLine(true).repeat(playersList.size());
        String line6 = "";

        if (! startingPlayer.isEmpty()) {
            lineStartingPlayer = spaces(7) + "STARTER";

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

                if (player.equals(model.getCurrentTurn())) {
                    line6 = isTurn(i);
                }
                i++;
            }
        }


        res.add(lineStartingPlayer);
        res.add(line1);
        res.add(line2.toString());
        res.add(line3.toString());
        res.add(line4.toString());
        res.add(line5);
        res.add(line6);

        return res;


//        System.out.println("""
//                                          STARTER
//                                   1╔═══════════════════╗    ╔═══╤═══════════╤═══╗
//                                   2║      Franco       ║    ║ W │           │ W ║
//                                   3║       BLUE        ║    ╟───┘           └───╢
//                                   4║        3          ║    ║                   ║
//                                   5╚═══════════════════╝    ╚═══╧═══════════╧═══╝
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
                 + color
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
        String word = val + "Playing ^";
        String spaces = spaces(21).repeat(val - 1) + spaces(5);
        return spaces + word;
    }

    public static void printLeaderboard(Map<String, Integer> leaderboard) {
        for (String player : leaderboard.keySet()) {
            if (leaderboard.get(player) == 1) {
                System.out.println(
                        "╔ Winner: " + "═".repeat(Math.max(player.length() - 9, 0)) + "╗");
                System.out.println("║ " + player + spaces(Math.max(9 - player.length(), 0)) + " ║");
                System.out.println("╚" + "═".repeat(Math.max(player.length(), 9)) + "╝");
            } else {
                for (int i = 2; i < leaderboard.size(); i++) {
                    System.out.println(
                            "╔ " + i + ": " + "═".repeat(Math.max(player.length() - 4, 0)) + "╗");
                    System.out.println(
                            "║ " + player + spaces(Math.max(4 - player.length(), 0)) + " ║");
                    System.out.println("╚" + "═".repeat(Math.max(player.length(), 4)) + "╝");
                }
            }
        }

    }

}
