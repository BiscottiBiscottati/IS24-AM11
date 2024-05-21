package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.enums.*;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.TUI.utils.Line;
import it.polimi.ingsw.am11.view.client.TUI.utils.Matrix;
import it.polimi.ingsw.am11.view.client.TUI.utils.Part;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;


public class CardPrinter {

    // FIXME we should use a dedicated class for decoding is better for understanding
    private static final Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();
    private static final Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();
    private static final Deck<ResourceCard> resDeck = ResourceDeckFactory.createDeck();
    private static final Deck<ObjectiveCard> objDeck = ObjectiveDeckFactory.createDeck();

    public static void printCardFrontAndBack(int id) throws IllegalCardBuildException {
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
            ObjectiveCard objectiveCard = objDeck.getCardById(id).orElseThrow();

            List<Line> lines = new ArrayList<>(7);

            Line topLine = new Line(List.of(new Part("╔"),
                                            new Part("═".repeat(19)),
                                            new Part("╗")));

            Line botLine = new Line(List.of(new Part("╚"),
                                            new Part("═".repeat(19)),
                                            new Part("╝")));

            Line emptyCenterBorder = new Line(List.of(new Part("║"),
                                                      new Part(" ".repeat(19)),
                                                      new Part("║")));
            lines.add(topLine);

            int points = objectiveCard.getPoints();
            Line pointsLine = new Line(List.of(new Part("║"),
                                               new Part(" ".repeat(9)),
                                               new Part(String.valueOf(points)),
                                               new Part(" ".repeat(9)),
                                               new Part("║")));
            lines.add(pointsLine);

            switch (objectiveCard) {
                case ColorCollectCard colorCollectCard -> {
                    Map.Entry<Color, Integer> colorToCollect =
                            colorCollectCard.getColorRequirements()
                                            .entrySet()
                                            .stream().filter(
                                                    entry -> entry.getValue() > 0).findFirst()
                                            .orElseThrow();
                    char colorLetter = getColorLetter(colorToCollect.getKey());
                    lines.add(emptyCenterBorder);
                    lines.add(new Line(List.of(new Part("║"),
                                               new Part(" ".repeat(8)),
                                               new Part(colorToCollect.getValue().toString()),
                                               new Part("x"),
                                               new Part(String.valueOf(colorLetter)),
                                               new Part(" ".repeat(8)),
                                               new Part("║"))));
                    lines.add(emptyCenterBorder);
                }
                case SymbolCollectCard symbolCollectCard -> {
                    for (Map.Entry<Symbol, Integer> entry :
                            symbolCollectCard.getSymbolRequirements().entrySet()) {
                        if (entry.getValue() == 0) {
                            continue;
                        }
                        char symbolLetter = getLetter(entry.getKey());
                        lines.add(new Line(List.of(new Part("║"),
                                                   new Part(" ".repeat(8)),
                                                   new Part(entry.getValue().toString()),
                                                   new Part("x"),
                                                   new Part(String.valueOf(symbolLetter)),
                                                   new Part(" ".repeat(8)),
                                                   new Part("║"))));
                    }
                    if (5 - lines.size() > 0) {
                        IntStream.range(0, 5 - lines.size())
                                 .forEach(i -> lines.add(emptyCenterBorder));
                    }
                    lines.add(emptyCenterBorder);
                }
                case TripletCard tripletCard -> {
                    List<List<String>> pattern =
                            IntStream.range(0, 4)
                                     .mapToObj(i -> tripletCard.getPattern().get(i)
                                                               .stream()
                                                               .map(color -> color == null ?
                                                                             " " :
                                                                             String.valueOf(
                                                                                     getLetter(
                                                                                             color)))
                                                               .toList())
                                     .toList();

                    for (int i = 0; i < 4; i++) {
                        List<Part> parts = new ArrayList<>(4);
                        parts.add(new Part("║"));
                        parts.add(new Part(" ".repeat(7)));

                        parts.add(new Part(String.join(" ", pattern.get(i))));

                        parts.add(new Part(" ".repeat(7)));
                        parts.add(new Part("║"));
                        lines.add(new Line(parts));
                    }

                }
                case LCard lCard -> {

                    List<List<String>> pattern =
                            IntStream.range(0, 4)
                                     .mapToObj(i -> lCard.getPattern().get(i)
                                                         .stream()
                                                         .map(color -> color == null ?
                                                                       " " :
                                                                       String.valueOf(
                                                                               getLetter(color)))
                                                         .toList())
                                     .toList();

                    for (int i = 0; i < 4; i++) {
                        List<Part> parts = new ArrayList<>(4);
                        parts.add(new Part("║"));
                        parts.add(new Part(" ".repeat(7)));

                        parts.add(new Part(String.join(" ", pattern.get(i))));

                        parts.add(new Part(" ".repeat(7)));
                        parts.add(new Part("║"));
                        lines.add(new Line(parts));
                    }
                }
                default -> IntStream.range(0, 3).forEach(i -> lines.add(emptyCenterBorder));

            }

            lines.add(botLine);

            new Matrix(lines).print();


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

    public static StarterCard getStarterCard(int id) throws IllegalCardBuildException {
        return starterDeck.getCardById(id).orElseThrow(
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

        switch (container.getItem().orElseThrow()) {
            case Color.RED -> {return 'R';}
            case Color.BLUE -> {return 'B';}
            case Color.GREEN -> {return 'G';}
            case Color.PURPLE -> {return 'P';}
            case Symbol.FEATHER -> {return 'F';}
            case Symbol.GLASS -> {return 'I';}
            case Symbol.PAPER -> {return 'S';}
            default -> throw new RuntimeException("Item is not one of the default ones: "
                                                  + container.getItem().orElseThrow());
        }

    }

    public static List<String> buildCornerLines(char left, char right, boolean isTop,
                                                PlayableCard card) {
        String line1 = "";
        String line2 = "";
        String line3 = "";

        List<Line> linesComposite = new ArrayList<>(3);
        List<String> lines = new ArrayList<>(3);
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);

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
            if (left == 'X') {
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
                centralPart = (card.getType() == PlayableCardType.GOLD) ?
                              ("    GOLD   ") :
                              ("  RESOURCE ");
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

    // FIXME the center does not print as it says there's a null
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

    public static PlayableCard getCard(int id) throws IllegalCardBuildException {
        return resDeck.getCardById(id)
                      .map(PlayableCard.class::cast)
                      .or(() -> goldDeck.getCardById(id))
                      .orElseThrow(() -> new IllegalCardBuildException("Card not found"));
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
