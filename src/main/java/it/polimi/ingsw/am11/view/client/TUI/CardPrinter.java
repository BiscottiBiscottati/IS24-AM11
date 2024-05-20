package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.enums.*;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class CardPrinter {

    private static final Deck goldDeck = GoldDeckFactory.createDeck();
    private static final Deck starterDeck = StarterDeckFactory.createDeck();
    private static final Deck resDeck = ResourceDeckFactory.createDeck();
    private static final Deck objDeck = ObjectiveDeckFactory.createDeck();

    public static void printCardFeB(int id) throws Throwable {
        if (starterDeck.getCardById(id).isPresent()) {
            StarterCard sCard = getStarterCard(id);

            char tlf = getLetter(sCard.getItemCorner(Corner.TOP_LX, false));
            char trf = getLetter(sCard.getItemCorner(Corner.TOP_RX, false));
            char blf = getLetter(sCard.getItemCorner(Corner.DOWN_LX, false));
            char brf = getLetter(sCard.getItemCorner(Corner.DOWN_RX, false));

            char tlb = getLetter(sCard.getItemCorner(Corner.TOP_LX, true));
            char trb = getLetter(sCard.getItemCorner(Corner.TOP_RX, true));
            char blb = getLetter(sCard.getItemCorner(Corner.DOWN_LX, true));
            char brb = getLetter(sCard.getItemCorner(Corner.DOWN_RX, true));

            List<Color> center = new ArrayList<>(sCard.getCenter(false));

            List<String> topLinesFront = buildCornerLines(tlf, trf, true, null);
            List<String> topLinesBack = buildCornerLines(tlb, trb, true, null);

            String centerLineFront = buildCenterString(center);
            String centerLineBack = buildCenterString(null);

            List<String> bottomLinesFront = buildCornerLines(blf, brf, false, null);
            List<String> bottomLinesBack = buildCornerLines(blb, brb, false, null);


            int spaces = 4; //Spaces between front and back view of the card

            System.out.println(topLinesFront.get(0) + spaces(spaces) + topLinesBack.get(0));
            System.out.println(topLinesFront.get(1) + spaces(spaces) + topLinesBack.get(1));
            System.out.println(topLinesFront.get(2) + spaces(spaces) + topLinesBack.get(2));
            System.out.println(centerLineFront + spaces(spaces) + centerLineBack);
            System.out.println(bottomLinesFront.get(0) + spaces(spaces) + bottomLinesBack.get(0));
            System.out.println(bottomLinesFront.get(1) + spaces(spaces) + bottomLinesBack.get(1));
            System.out.println(bottomLinesFront.get(2) + spaces(spaces) + bottomLinesBack.get(2));
            System.out.println("        Front        " + spaces(spaces) + "        Retro        ");


//            System.out.println("""
//                                       ╔═══╤═══════════╤═══╗    ╔═══╤═══════════╤═══╗
//                                       ║ W │2xScroll: 3│ W ║    ║ W │           │ W ║
//                                       ╟───┘           └───╢    ╟───┘           └───╢
//                                       ║     M │ M │ M     ║    ║                   ║
//                                       ╟───┐           ┌───╢    ╟───┐           ┌───╢
//                                       ║ W │           │ W ║    ║ W │           │ W ║
//                                       ╚═══╧═══════════╧═══╝    ╚═══╧═══════════╧═══╝
//                                      "         GOLD        "
//                                       """);

        } else if (objDeck.getCardById(id).isPresent()) {
            //TODO

        } else {
            PlayableCard pCard = getCard(id);
            char tlf = getLetter(pCard.getItemCorner(Corner.TOP_LX, false));
            char trf = getLetter(pCard.getItemCorner(Corner.TOP_RX, false));
            char blf = getLetter(pCard.getItemCorner(Corner.DOWN_LX, false));
            char brf = getLetter(pCard.getItemCorner(Corner.DOWN_RX, false));

            char tlb = getLetter(pCard.getItemCorner(Corner.TOP_LX, true));
            char trb = getLetter(pCard.getItemCorner(Corner.TOP_RX, true));
            char blb = getLetter(pCard.getItemCorner(Corner.DOWN_LX, true));
            char brb = getLetter(pCard.getItemCorner(Corner.DOWN_RX, true));

            List<Color> center = new ArrayList<>(pCard.getCenter(true));

            List<String> topLinesFront = buildCornerLines(tlf, trf, true, null);
            List<String> topLinesBack = buildCornerLines(tlb, trb, true, null);

            String centerLineFront = buildCenterString(center);
            String centerLineBack = buildCenterString(null);

            List<String> bottomLinesFront = buildCornerLines(blf, brf, false, null);
            List<String> bottomLinesBack = buildCornerLines(blb, brb, false, null);

            int spaces = 4; //Spaces between front and back view of the card

            System.out.println(topLinesFront.get(0) + spaces(spaces) + topLinesBack.get(0));
            System.out.println(topLinesFront.get(1) + spaces(spaces) + topLinesBack.get(1));
            System.out.println(topLinesFront.get(2) + spaces(spaces) + topLinesBack.get(2));
            System.out.println(centerLineFront + spaces(spaces) + centerLineBack);
            System.out.println(bottomLinesFront.get(0) + spaces(spaces) + bottomLinesBack.get(0));
            System.out.println(bottomLinesFront.get(1) + spaces(spaces) + bottomLinesBack.get(1));
            System.out.println(bottomLinesFront.get(2) + spaces(spaces) + bottomLinesBack.get(2));
            System.out.println("        Front        " + spaces(spaces) + "        Retro        ");

        }
    }

    public static StarterCard getStarterCard(int id) throws Throwable {
        return (StarterCard) starterDeck.getCardById(id).orElseThrow(
                () -> new IllegalCardBuildException(
                        "Starter Card not found"));
    }

    public static char getLetter(@Nullable CornerContainer container) {
        if (container == null) {
            return 'X';
        }

        if (! container.isAvailable()) {
            return 'X';
        }
        if (container.getItem().isEmpty()) {
            return ' ';
        }

        switch (container.getItem().orElseThrow().getColumnName()) {
            case "red" -> {return 'R';}
            case "blue" -> {return 'B';}
            case "green" -> {return 'G';}
            case "purple" -> {return 'P';}
            case "feather" -> {return 'F';}
            case "glass" -> {return 'I';}
            case "paper" -> {return 'S';}
            default -> throw new RuntimeException("Item is not one of the default ones: "
                                                  + container.getItem().orElseThrow());
        }

    }

    public static List<String> buildCornerLines(char left, char right, boolean isTop,
                                                PlayableCard card) {
        String line1 = "";
        String line2 = "";
        String line3 = "";

        List<String> lines = new ArrayList<>(3);
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);

        String leftPart;
        String centralPart;
        String rightPart;

        if (isTop) {
            // LINE 1
            if (left == 'X') {
                leftPart = "╔════";
            } else {
                leftPart = "╔═══╤";
            }
            if (left == 'X') {
                rightPart = "═══════════════╗";
            } else {
                rightPart = "═══════════╤═══╗";
            }
            line1 = leftPart + rightPart;
            //LINE 2
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "║ " + left + " │";
            }
            if (card != null) {
                centralPart = buildRequirementsString(card.getPlacingRequirements());
            } else {
                centralPart = spaces(11);
            }
            if (left == 'X') {
                rightPart = "               ║";
            } else {
                rightPart = "           │ " + right + " ║";
            }
            line2 = leftPart + rightPart;
            //LINE 3
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "╟───┘";
            }
            if (card != null) {
                centralPart = (card.getType() == PlayableCardType.GOLD) ? ("    GOLD   ") : ("  " +
                                                                                             "RESOURCE" +
                                                                                             " ");
            } else {
                centralPart = spaces(11);
            }
            if (left == 'X') {
                rightPart = "    ║";
            } else {
                rightPart = "└───╢";
            }
            line3 = leftPart + rightPart;
        } else {
            //LINE 1
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "╟───┐";
            }
            if (left == 'X') {
                rightPart = "               ╢";
            } else {
                rightPart = "           ┌───╢";
            }
            line1 = leftPart + rightPart;
            //LINE 2
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "║ " + left + " │";
            }
            if (card != null) {
                centralPart = buildRequirementsString(card.getPlacingRequirements());
            } else {
                centralPart = spaces(11);
            }
            if (left == 'X') {
                rightPart = "    ║";
            } else {
                rightPart = "│ " + right + " ║";
            }
            line2 = leftPart + centralPart + rightPart;
            //LINE 3
            if (left == 'X') {
                leftPart = "╚════";
            } else {
                leftPart = "╚═══╧";
            }
            if (left == 'X') {
                rightPart = "═══════════════╝";
            } else {
                rightPart = "═══════════╧═══╝";
            }
            line3 = leftPart + rightPart;
        }


        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        return lines;
    }

    public static String buildCenterString(@Nullable List<Color> center) {
        char center1 = getColorLetter(center.get(0));
        char center2;
        char center3;
        if (center == null || center.isEmpty()) {
            return "║                   ║";
        }
        if (center.size() == 1) {
            return "║         " + center1 + "         ║";
        }
        if (center.size() == 2) {
            center2 = getColorLetter(center.get(1));
            return "║       " + center1 + "   " + center2 + "       ║";
        }
        if (center.size() == 3) {
            center2 = getColorLetter(center.get(1));
            center3 = getColorLetter(center.get(2));
            return "║     " + center1 + " │ " + center2 + " │ " + center3 + "     ║";
        }
        return "║       error       ║";
    }

    public static String spaces(int num) {
        return " ".repeat(Math.max(0, num));
    }

    public static PlayableCard getCard(int id) throws Throwable {
        PlayableCard card = (PlayableCard) resDeck.getCardById(id)
                                                  .map(PlayableCard.class::cast)
                                                  .or(() -> goldDeck.getCardById(id)).orElseThrow(
                        () -> new IllegalCardBuildException(
                                "Card not found"));
        return card;
    }

    public static String buildRequirementsString(Map<Color, Integer> requirements) {
        int i;

        int numOfRed = requirements.get(Color.RED);
        int numOfBlue = requirements.get(Color.BLUE);
        int numOfGreen = requirements.get(Color.GREEN);
        int numOfPurple = requirements.get(Color.PURPLE);

        StringBuilder stringBuilder = new StringBuilder();

        for (i = 0; i < numOfRed; i++) {
            stringBuilder.append("R ");
        }
        for (i = 0; i < numOfBlue; i++) {
            stringBuilder.append("B ");
        }
        for (i = 0; i < numOfGreen; i++) {
            stringBuilder.append("G ");
        }
        for (i = 0; i < numOfPurple; i++) {
            stringBuilder.append("P ");
        }

        String string = stringBuilder.toString();

        switch (string.length()) {
            case 0:
                return spaces(11);
            case 2:
                return spaces(5) + string + spaces(4);
            case 4:
                return spaces(4) + string + spaces(3);
            case 6:
                return spaces(3) + string + spaces(2);
            case 8:
                return spaces(2) + string + spaces(1);
            case 10:
                return spaces(1) + string;
            default:
                throw new RuntimeException("Invalid number of requirements");
        }
    }

    public static char getColorLetter(Color color) {
        switch (color.getColumnName()) {
            case "red" -> {return 'R';}
            case "blue" -> {return 'B';}
            case "green" -> {return 'G';}
            case "purple" -> {return 'P';}
            default -> throw new RuntimeException("Color is not one of the default ones: "
                                                  + color.getItem().orElseThrow());
        }
    }

    public static void printTable(List<Integer> visiblesId, Color goldColor, Color resColor)
    throws Throwable {

        List<String> goldDeckLines = buildDeck(goldColor, PlayableCardType.GOLD);
        List<String> resDeckLines = buildDeck(resColor, PlayableCardType.RESOURCE);

        List<String> card1 = buildCard(visiblesId.get(0));
        List<String> card2 = buildCard(visiblesId.get(1));
        List<String> card3 = buildCard(visiblesId.get(2));
        List<String> card4 = buildCard(visiblesId.get(3));

        int i;

        for (i = 0; i < 8; i++) {
            System.out.println(
                    card1.get(i) + spaces(4) + card2.get(i) + spaces(4) + goldDeckLines.get(i));
        }

        System.out.println(" ");

        for (i = 0; i < 8; i++) {
            System.out.println(
                    card3.get(i) + spaces(4) + card4.get(i) + spaces(4) + resDeckLines.get(i));
        }
    }

    public static void printHand(List<Integer> cardIds) throws Throwable {

        List<String> card1 = buildCard(cardIds.get(0));
        List<String> card2 = buildCard(cardIds.get(1));
        List<String> card3 = buildCard(cardIds.get(2));

        int i;

        for (i = 0; i < 8; i++) {
            System.out.println(
                    card1.get(i) + spaces(4) + card2.get(i) + spaces(4) + card3.get(i));
        }
    }

    public static List<String> buildCard(int id) throws Throwable {
        List<String> card = new ArrayList<>();

        PlayableCard pCard = getCard(id);
        char tlf = getLetter(pCard.getItemCorner(Corner.TOP_LX, false));
        char trf = getLetter(pCard.getItemCorner(Corner.TOP_RX, false));
        char blf = getLetter(pCard.getItemCorner(Corner.DOWN_LX, false));
        char brf = getLetter(pCard.getItemCorner(Corner.DOWN_RX, false));

        List<Color> center = new ArrayList<>(pCard.getCenter(false));

        List<String> topLinesFront = buildCornerLines(tlf, trf, true, null);

        String centerLineFront = buildCenterString(center);

        List<String> bottomLinesFront = buildCornerLines(blf, brf, false, null);

        card.add(topLinesFront.get(0));
        card.add(topLinesFront.get(1));
        card.add(topLinesFront.get(2));
        card.add(centerLineFront);
        card.add(bottomLinesFront.get(0));
        card.add(bottomLinesFront.get(1));
        card.add(bottomLinesFront.get(2));
        if (id < 10) {
            card.add(spaces(10) + id + spaces(10));
        } else if (id < 100) {
            card.add(spaces(10) + id + spaces(9));
        } else {
            card.add(spaces(9) + id + spaces(9));
        }
        return card;
    }

    public static List<String> buildDeck(Color color, PlayableCardType type) {
        List<String> deck = new ArrayList<>();
        List<Color> center = new ArrayList<>();
        center.add(color);

        List<String> topLines = buildCornerLines(' ', ' ', true, null);

        String typeLine = "╟───┘" +
                          ((type == PlayableCardType.GOLD) ? ("    GOLD   ") : ("  " +
                                                                                "RESOURCE" +
                                                                                " ")) +
                          "└───╢";

        String centerLine = buildCenterString(center);

        List<String> bottomLines = buildCornerLines(' ', ' ', false, null);

        deck.add(topLines.get(0));
        deck.add(topLines.get(1));
        deck.add(typeLine);
        deck.add(centerLine);
        deck.add(bottomLines.get(0));
        deck.add(bottomLines.get(1));
        deck.add(bottomLines.get(2));
        deck.add(spaces(9) + "Deck" + spaces(8));
        return deck;
    }

    public static void printObjectives(List<Integer> ids) throws Throwable {
        //TODO;
    }

    public static String buildPointsString(PlayableCard card) {
        int points = card.getPoints();
        PointsRequirementsType type = card.getPointsRequirements();
        Optional<Symbol> symbol = card.getSymbolToCollect();

        if (type == PointsRequirementsType.CLASSIC) {
            if (points == 0) {
                return spaces(11);
            }
            return spaces(5) + points + spaces(5);
        }
        if (type == PointsRequirementsType.SYMBOLS) {
            if (symbol.isEmpty()) {
                return spaces(11);
            }
            if (symbol.orElseThrow() == Symbol.PAPER) {
                return " 1:Scroll  ";
            }
            if (symbol.orElseThrow() == Symbol.GLASS) {
                return "   1:Ink   ";
            }
            if (symbol.orElseThrow() == Symbol.FEATHER) {
                return "  1:Quill  ";
            }
        }
        if (type == PointsRequirementsType.COVERING_CORNERS) {
            return " 2xCorner  ";
        }
        return "   error   ";
    }

}
