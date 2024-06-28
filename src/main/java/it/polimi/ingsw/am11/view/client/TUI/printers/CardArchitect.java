package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.*;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.TUI.utils.Line;
import it.polimi.ingsw.am11.view.client.TUI.utils.Part;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardArchitect {

    public static List<String> buildTable(List<Integer> visiblesId, @Nullable GameColor goldColor,
                                          @Nullable GameColor resColor)
    throws IllegalCardBuildException {

        //size: 73*19


        List<String> result = new ArrayList<>(8);

        List<String> goldDeckLines = buildDeck(goldColor, PlayableCardType.GOLD);
        List<String> resDeckLines = buildDeck(resColor, PlayableCardType.RESOURCE);

        //FIXME case of empty deck

        List<String> card1 = buildCard(visiblesId.get(0));
        List<String> card2 = buildCard(visiblesId.get(1));
        List<String> card3 = buildCard(visiblesId.get(2));
        List<String> card4 = buildCard(visiblesId.get(3));

        result.add("╔═ Table: " + "═".repeat(12) + "═".repeat(42) + "═".repeat(8) + "╗");

        int i;

        int size = card1.size();
        for (i = 0; i < size; i++) {
            result.add("║" + card1.get(i) + CardPrinter.spaces(4) + card2.get(i) +
                       CardPrinter.spaces(4) + goldDeckLines.get(i) + "║");
        }

        result.add("║" + spaces(71) + "║");

        for (i = 0; i < size; i++) {
            result.add("║" + card3.get(i) + CardPrinter.spaces(4) + card4.get(i) +
                       CardPrinter.spaces(4) + resDeckLines.get(i) + "║");
        }
        result.add("╚" + "═".repeat(63) + "═".repeat(8) + "╝");

        return result;
    }

    public static List<String> buildHand(List<Integer> cardIds) throws IllegalCardBuildException {

        //size 73 * 10
        if (cardIds.size() > 3) {
            throw new IllegalCardBuildException("Hand can have 3 cards at maximum");
        }

        List<String> result = new ArrayList<>(8);

        List<List<String>> cards = new ArrayList<>(3);

        for (Integer i : cardIds) {
            cards.add(buildCard(i));
        }

        result.add("╔═ Hand: " + "═".repeat(13) + "═".repeat(42) + "═".repeat(8) + "╗");

        int i;
        int size = buildCard(1).size();
        int cardLenght = 21;
        for (i = 0; i < size; i++) {
            List<String> strings = new ArrayList<>();

            for (List<String> card : cards) {
                strings.add(card.get(i));
            }
            for (int j = 0; strings.size() < 3; j++) {
                strings.add(CardPrinter.spaces(cardLenght));
            }

            result.add("║" + strings.get(0) + CardPrinter.spaces(4) + strings.get(1) +
                       CardPrinter.spaces(4) + strings.get(2) + "║");
        }

        result.add("╚" + "═".repeat(63) + "═".repeat(8) + "╝");

        return result;
    }

    public static List<String> buildCard(int id) throws IllegalCardBuildException {

        List<String> card = new ArrayList<>();

        PlayableCard pCard = CardPrinter.getPlayableCard(id);
        char tlf = CardPrinter.getLetter(pCard.getItemCorner(Corner.TOP_LX, false));
        char trf = CardPrinter.getLetter(pCard.getItemCorner(Corner.TOP_RX, false));
        char blf = CardPrinter.getLetter(pCard.getItemCorner(Corner.DOWN_LX, false));
        char brf = CardPrinter.getLetter(pCard.getItemCorner(Corner.DOWN_RX, false));

        List<GameColor> center = new ArrayList<>(pCard.getCenter(true));

        List<String> topLinesFront = buildCornerLines(tlf, trf, true, pCard);

        String centerLineFront = buildCenterString(center);

        List<String> bottomLinesFront = buildCornerLines(blf, brf, false, pCard);

        card.add(topLinesFront.get(0));
        card.add(topLinesFront.get(1));
        card.add(topLinesFront.get(2));
        card.add(centerLineFront);
        card.add(bottomLinesFront.get(0));
        card.add(bottomLinesFront.get(1));
        card.add(bottomLinesFront.get(2));
        return card;
    }

    public static List<String> buildCornerLines(char left, char right, boolean isTop,
                                                PlayableCard card) {
        String line1;
        String line2;
        String line3;

        List<Line> linesComposite = new ArrayList<>(3);
        List<String> lines = new ArrayList<>(3);

        String leftPart;
        String centralPart;
        String rightPart;

        List<Part> parts = new ArrayList<>(3);

        if (isTop) {
            // LINE 1
            if (left == 'X') {
                leftPart = "╔════";
                parts.add(new Part("╔════"));
            } else {
                leftPart = "╔═══╤";
                parts.add(new Part("╔═══╤"));
            }
            if (right == 'X') {
                rightPart = "═══════════════╗";
                parts.add(new Part("═══════════════╗"));
            } else {
                rightPart = "═══════════╤═══╗";
                parts.add(new Part("═══════════╤═══╗"));
            }
            line1 = leftPart + rightPart;
            linesComposite.add(new Line(parts));
            //LINE 2
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "║ " + left + " │";
            }
            if (card != null) {
                centralPart = buildPointsString(card);
            } else {
                centralPart = CardPrinter.spaces(11);
            }
            if (right == 'X') {
                rightPart = "    ║";
            } else {
                rightPart = "│ " + right + " ║";
            }
            line2 = leftPart + centralPart + rightPart;
            //LINE 3
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "╟───┘";
            }
            if (card != null) {
                if (card.getType() == PlayableCardType.GOLD) {
                    centralPart = "    GOLD   ";
                } else {
                    centralPart = "  RESOURCE ";
                }
            } else {
                centralPart = CardPrinter.spaces(11);
            }
            if (right == 'X') {
                rightPart = "    ║";
            } else {
                rightPart = "└───╢";
            }
            line3 = leftPart + centralPart + rightPart;
        } else {
            //LINE 1
            if (left == 'X') {
                leftPart = "║    ";
            } else {
                leftPart = "╟───┐";
            }
            if (right == 'X') {
                rightPart = "               ║";
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
                centralPart = CardPrinter.spaces(11);
            }
            if (right == 'X') {
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
            if (card != null) {
                int id = card.getId();
                if (id < 10) {
                    centralPart = "════ " + id + " ════";
                } else if (id < 100) {
                    centralPart = "════ " + id + " ═══";
                } else {
                    centralPart = "═══ " + id + " ═══";
                }
            } else {
                centralPart = "═══════════";
            }
            if (right == 'X') {
                rightPart = "════╝";
            } else {
                rightPart = "╧═══╝";
            }
            line3 = leftPart + centralPart + rightPart;
        }


        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        return lines;
    }

    // FIXME the center does not print as it says there's a null
    public static String buildCenterString(List<GameColor> center) {
        char center1;
        char center2;
        char center3;
        if (center == null || center.isEmpty()) {
            return "║                   ║";
        }
        if (center.size() == 1) {
            center1 = CardPrinter.getColorLetter(center.getFirst());
            return "║         " + center1 + "         ║";
        }
        if (center.size() == 2) {
            center1 = CardPrinter.getColorLetter(center.get(0));
            center2 = CardPrinter.getColorLetter(center.get(1));
            return "║       " + center1 + " │ " + center2 + "       ║";
        }
        if (center.size() == 3) {
            center1 = CardPrinter.getColorLetter(center.get(0));
            center2 = CardPrinter.getColorLetter(center.get(1));
            center3 = CardPrinter.getColorLetter(center.get(2));
            return "║     " + center1 + " │ " + center2 + " │ " + center3 + "     ║";
        }
        return "║       error       ║";
    }

    public static String buildPointsString(PlayableCard card) {
        int points = card.getPoints();
        PointsRequirementsType type = card.getPointsRequirements();
        Optional<Symbol> symbol = card.getSymbolToCollect();

        if (type == PointsRequirementsType.CLASSIC) {
            if (points == 0) {
                return CardPrinter.spaces(11);
            }
            return CardPrinter.spaces(5) + points + CardPrinter.spaces(5);
        }
        if (type == PointsRequirementsType.SYMBOLS) {
            if (symbol.isEmpty()) {
                return CardPrinter.spaces(11);
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

    public static String buildRequirementsString(Map<GameColor, Integer> requirements) {
        int i;

        int numOfRed = requirements.get(GameColor.RED);
        int numOfBlue = requirements.get(GameColor.BLUE);
        int numOfGreen = requirements.get(GameColor.GREEN);
        int numOfPurple = requirements.get(GameColor.PURPLE);

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
                return CardPrinter.spaces(11);
            case 2:
                return CardPrinter.spaces(5) + string + CardPrinter.spaces(4);
            case 4:
                return CardPrinter.spaces(4) + string + CardPrinter.spaces(3);
            case 6:
                return CardPrinter.spaces(3) + string + CardPrinter.spaces(2);
            case 8:
                return CardPrinter.spaces(2) + string + CardPrinter.spaces(1);
            case 10:
                return CardPrinter.spaces(1) + string;
            default:
                throw new RuntimeException("Invalid number of requirements");
        }
    }

    public static List<String> buildDeck(@Nullable GameColor color, PlayableCardType type) {
        List<String> deck = new ArrayList<>(8);
        List<GameColor> center = new ArrayList<>(1);
        if (color != null) {
            center.add(color);
        }

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
        deck.add("╚═══╧═══ Deck ══╧═══╝");

        return deck;
    }

    public static List<String> buildVertObj(int comm1, int comm2, int pers)
    throws IllegalCardBuildException {
        //size: comm: 16 lines
        //size: pers: 9 lines
        //total: 25 lines

//            ╔══ Common objectives: ═╗
//            ║ ╔═══════ Gather: ═══╗ ║
//            ║ ║ ┌───┐    ┌───┐    ║ ║
//            ║ ║ │ 2 │  ┌─┘ R └─┐  ║ ║
//            ║ ║ └───┘  │ R   R │  ║ ║
//            ║ ║        └───────┘  ║ ║
//            ║ ╚═══════════════════╝ ║
//                        id
//            ╔ Personal objectives: ═╗


        int i;

        List<String> res = new ArrayList<>();

        res.add("╔══ Common objectives: ═╗");


        List<String> comm1List = buildObjective(comm1);
        List<String> comm2List = buildObjective(comm2);
        List<String> persList = buildObjective(pers);

        for (i = 0; i < 7; i++) {
            res.add("║ " + comm1List.get(i) + " ║");
        }
        for (i = 0; i < 7; i++) {
            res.add("║ " + comm2List.get(i) + " ║");
        }
        res.add("╚═══════════════════════╝");
        res.add("╔ Personal objectives: ═╗");
        for (i = 0; i < 7; i++) {
            res.add("║ " + persList.get(i) + " ║");
        }
        res.add("╚═══════════════════════╝");

        return res;
    }

    public static List<String> buildObjective(int id) throws IllegalCardBuildException {
        List<String> result = new ArrayList<>();
        ObjectiveCard objectiveCard = CardPrinter.getObjective(id);

        String line1 = "╔══════";
        String line2 = "║ ┌───┐";
        String line3 = "║ │ " + objectiveCard.getPoints() + " │";
        String line4 = "║ └───┘";
        String line5 = "║      ";
        String line6 = "╚══════";
        String line7;
        if (id < 10) {
            line7 = spaces(10) + id + spaces(10);
        } else if (id < 100) {
            line7 = spaces(10) + id + spaces(9);
        } else {
            line7 = spaces(9) + id + spaces(9);
        }

        switch (objectiveCard) {
            case ColorCollectCard colorCollectCard -> {
//              1╔═══════ Gather: ═══╗
//              2║ ┌───┐    ┌───┐    ║
//              3║ │ 2 │  ┌─┘ R └─┐  ║
//              4║ └───┘  │ R   R │  ║
//              5║        └───────┘  ║
//              6╚═══════════════════╝
//              7          id
                Map<GameColor, Integer> req = colorCollectCard.getColorRequirements();
                GameColor color = GameColor.RED;

                for (Map.Entry<GameColor, Integer> entry : req.entrySet()) {
                    if (entry.getValue() > 0) {
                        color = entry.getKey();
                        break;
                    }
                }

                line1 = line1 + "═ Gather: ═══╗";
                line2 = line2 + "    ┌───┐    ║";
                line3 = line3 + "  ┌─┘ " + color.getTUICode() + " └─┐  ║";
                line4 = line4 + "  │ " + color.getTUICode() + "   " + color.getTUICode() + " │  ║";
                line5 = line5 + "  └───────┘  ║";
                line6 = line6 + "═════════════╝";

            }
            case SymbolCollectCard symbolCollectCard -> {
//              1╔═══════ Gather: ═══╗    ╔═══════ Gather: ═══╗
//              2║ ┌───┐             ║    ║ ┌───┐             ║
//              3║ │ 2 │  ┌───────┐  ║    ║ │ 2 │ ┌─────────┐ ║
//              4║ └───┘  │ Q   I │  ║    ║ └───┘ │ Q  S  I │ ║
//              5║        └───────┘  ║    ║       └─────────┘ ║
//              6╚═══════════════════╝    ╚═══════════════════╝
//              7          id                       id
                Map<Symbol, Integer> symbols = symbolCollectCard.getSymbolRequirements();

                line1 = line1 + "═ Gather: ═══╗";
                line2 = line2 + "             ║";

                if (symbols.values().stream().reduce(0, Integer::sum) == 2) {
                    Symbol symbol = Symbol.FEATHER;
                    for (Map.Entry<Symbol, Integer> entry : symbols.entrySet()) {
                        if (entry.getValue() > 0) {
                            symbol = entry.getKey();
                            break;
                        }
                    }
                    line3 = line3 + "  ┌───────┐  ║";
                    line4 = line4 + "  │ " + symbol.getTUICode() + "   " + symbol.getTUICode() +
                            " │ " +
                            " ║";
                    line5 = line5 + "  └───────┘  ║";

                } else {
                    line3 = line3 + " ┌─────────┐ ║";
                    line4 = line4 + " │ Q  S  I │ ║";
                    line5 = line5 + " └─────────┘ ║";
                }

                line6 = line6 + "═════════════╝";
            }
            case TripletCard tripletCard -> {
//              1╔═══════ Pattern: ══╗  ╔═══════ Pattern: ══╗
//              2║ ┌───┐    ┌──│ R │ ║  ║ ┌───┐ │ R │──┐    ║
//              3║ │ 2 │ ┌──│ R │──┘ ║  ║ │ 2 │ └──│ R │──┐ ║
//              4║ └───┘ │ R │──┘    ║  ║ └───┘    └──│ R │ ║
//              5║       └───┘       ║  ║             └───┘ ║
//              6╚═══════════════════╝  ╚═══════════════════╝
//              7          id                     id
//              Flipped: F              Flipped: T


                line1 = line1 + "═ Pattern: ══╗";

                List<List<GameColor>> pattern = tripletCard.getPattern();

                if (! tripletCard.isFlipped()) {
                    line2 = line2 + "    ┌──│ " + pattern.get(0).get(2).getTUICode() + " │ ║";
                    line3 = line3 + " ┌──│ " + pattern.get(1).get(1).getTUICode() + " │──┘ ║";
                    line4 = line4 + " │ " + pattern.get(2).getFirst().getTUICode() + " │──┘    ║";
                    line5 = line5 + " └───┘       ║";
                } else {
                    line2 = line2 + " │ " + pattern.get(0).getFirst().getTUICode() + " │──┐    ║";
                    line3 = line3 + " └──│ " + pattern.get(1).get(1).getTUICode() + " │──┐ ║";
                    line4 = line4 + "    └──│ " + pattern.get(2).get(2).getTUICode() + " │ ║";
                    line5 = line5 + "       └───┘ ║";
                }

                line6 = line6 + "═════════════╝";
            }
            case LCard lCard -> {
//          1╔═══════ Pattern: ══╗╔═══════ Pattern: ══╗╔═══════ Pattern: ══╗╔═══════ Pattern: ══╗
//          2║ ┌───┐  │ R │      ║║ ┌───┐     │ R │   ║║ ┌───┐  │ G │──┐   ║║ ┌───┐  ┌──│ R │   ║
//          3║ │ 3 │  ╞═══╡      ║║ │ 3 │     ╞═══╡   ║║ │ 3 │  └──│ R │   ║║ │ 3 │  │ R │──┘   ║
//          4║ └───┘  │ R │──┐   ║║ └───┘  ┌──│ R │   ║║ └───┘     ╞═══╡   ║║ └───┘  ╞═══╡      ║
//          5║        └──│ G │   ║║        │ G │──┘   ║║           │ R │   ║║        │ R │      ║
//          6╚═══════════╧═══╧═══╝╚════════╧═══╧══════╝╚═══════════╧═══╧═══╝╚════════╧═══╧══════╝
//          7          id                   id                   id                   id
//               Flip: T Rot: T       Flip: F Rot: T       Flip: T Rot: F       Flip: F Rot: F

                line1 = line1 + "═ Pattern: ══╗";

                List<List<GameColor>> pattern = lCard.getPattern();

                if (! lCard.isFlipped() && ! lCard.isRotated()) {
                    line2 = line2 + "  ┌──│ " + pattern.get(0).get(2).getTUICode() + " │   ║";
                    line3 = line3 + "  │ " + pattern.get(1).get(1).getTUICode() + " │──┘   ║";
                    line4 = line4 + "  ╞═══╡      ║";
                    line5 = line5 + "  │ " + pattern.get(3).get(1).getTUICode() + " │      ║";
                    line6 = line6 + "══╧═══╧══════╝";
                } else if (lCard.isFlipped() && ! lCard.isRotated()) {
                    line2 = line2 + "  │ " + pattern.get(0).getFirst().getTUICode() + " │──┐   ║";
                    line3 = line3 + "  └──│ " + pattern.get(1).get(1).getTUICode() + " │   ║";
                    line4 = line4 + "     ╞═══╡   ║";
                    line5 = line5 + "     │ " + pattern.get(3).get(1).getTUICode() + " │   ║";
                    line6 = line6 + "═════╧═══╧═══╝";
                } else if (! lCard.isFlipped()) {
                    line2 = line2 + "     │ " + pattern.get(0).get(1).getTUICode() + " │   ║";
                    line3 = line3 + "     ╞═══╡   ║";
                    line4 = line4 + "  ┌──│ " + pattern.get(2).get(1).getTUICode() + " │   ║";
                    line5 = line5 + "  │ " + pattern.get(3).getFirst().getTUICode() + " │──┘   ║";
                    line6 = line6 + "══╧═══╧══════╝";
                } else {
                    line2 = line2 + "  │ " + pattern.get(0).get(1).getTUICode() + " │      ║";
                    line3 = line3 + "  ╞═══╡      ║";
                    line4 = line4 + "  │ " + pattern.get(2).get(1).getTUICode() + " │──┐   ║";
                    line5 = line5 + "  └──│ " + pattern.get(3).get(2).getTUICode() + " │   ║";
                    line6 = line6 + "═════╧═══╧═══╝";
                }
            }
            default -> throw new IllegalCardBuildException("Is not objective");

        }

        result.add(line1);
        result.add(line2);
        result.add(line3);
        result.add(line4);
        result.add(line5);
        result.add(line6);
        result.add(line7);

        return result;
    }

    public static String spaces(int num) {
        return " ".repeat(Math.max(0, num));
    }
}
