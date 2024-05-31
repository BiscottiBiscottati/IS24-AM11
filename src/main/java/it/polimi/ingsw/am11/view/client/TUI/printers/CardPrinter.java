package it.polimi.ingsw.am11.view.client.TUI.printers;

import com.google.common.base.Strings;
import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.TUI.utils.Line;
import it.polimi.ingsw.am11.view.client.TUI.utils.Matrix;
import it.polimi.ingsw.am11.view.client.TUI.utils.Part;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


// FIXME playable cards doesn't show symbols on their corners
public class CardPrinter {

    // FIXME we should use a dedicated class for decoding is better for understanding
    private static final Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();
    private static final Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();
    private static final Deck<ResourceCard> resDeck = ResourceDeckFactory.createDeck();
    private static final Deck<ObjectiveCard> objDeck = ObjectiveDeckFactory.createDeck();

    public static void printObjectives(List<Integer> ids) throws IllegalCardBuildException {
        List<List<String>> allObj = new ArrayList<>();
        for (Integer id : ids) {
            allObj.add(CardArchitect.buildObjective(id));
        }

        for (int i = 0; i < 7; i++) {
            for (List<String> singObj : allObj) {
                System.out.print(singObj.get(i));
            }
            System.out.print("\n");
        }
    }

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

            List<String> topLinesFront = CardArchitect.buildCornerLines(tlf, trf, true, null);
            List<String> topLinesBack = CardArchitect.buildCornerLines(tlb, trb, true, null);

            String centerLineFront = CardArchitect.buildCenterString(center);
            String centerLineBack = CardArchitect.buildCenterString(null);

            List<String> bottomLinesFront = CardArchitect.buildCornerLines(blf, brf, false, null);
            List<String> bottomLinesBack = CardArchitect.buildCornerLines(blb, brb, false, null);


            int spaces = 4; //Spaces between front and back view of the card

            System.out.println(topLinesFront.get(0) + spaces(spaces) + topLinesBack.get(0));
            System.out.println(topLinesFront.get(1) + spaces(spaces) + topLinesBack.get(1));
            System.out.println(topLinesFront.get(2) + spaces(spaces) + topLinesBack.get(2));
            System.out.println(centerLineFront + spaces(spaces) + centerLineBack);
            System.out.println(bottomLinesFront.get(0) + spaces(spaces) + bottomLinesBack.get(0));
            System.out.println(bottomLinesFront.get(1) + spaces(spaces) + bottomLinesBack.get(1));
            System.out.println(bottomLinesFront.get(2) + spaces(spaces) + bottomLinesBack.get(2));
            System.out.println("        Front        " + spaces(spaces) + "        Retro        ");


//                                       ╔═══╤═══════════╤═══╗    ╔═══╤═══════════╤═══╗
//                                       ║ W │2xScroll: 3│ W ║    ║ W │           │ W ║
//                                       ╟───┘           └───╢    ╟───┘           └───╢
//                                       ║     M │ M │ M     ║    ║                   ║
//                                       ╟───┐           ┌───╢    ╟───┐           ┌───╢
//                                       ║ W │           │ W ║    ║ W │           │ W ║
//                                       ╚═══╧═══════════╧═══╝    ╚═══╧═══════════╧═══╝
//                                      "         GOLD        "

        } else if (objDeck.getCardById(id).isPresent()) {
            printObjectives(new ArrayList<>(List.of(id)));
        } else {
            PlayableCard pCard = getPlayableCard(id);
            char tlf = getLetter(pCard.getItemCorner(Corner.TOP_LX, false));
            char trf = getLetter(pCard.getItemCorner(Corner.TOP_RX, false));
            char blf = getLetter(pCard.getItemCorner(Corner.DOWN_LX, false));
            char brf = getLetter(pCard.getItemCorner(Corner.DOWN_RX, false));

            char tlb = getLetter(pCard.getItemCorner(Corner.TOP_LX, true));
            char trb = getLetter(pCard.getItemCorner(Corner.TOP_RX, true));
            char blb = getLetter(pCard.getItemCorner(Corner.DOWN_LX, true));
            char brb = getLetter(pCard.getItemCorner(Corner.DOWN_RX, true));

            List<Color> center = new ArrayList<>(pCard.getCenter(true));

            List<String> topLinesFront = CardArchitect.buildCornerLines(tlf, trf, true, null);
            List<String> topLinesBack = CardArchitect.buildCornerLines(tlb, trb, true, null);

            String centerLineFront = CardArchitect.buildCenterString(center);
            String centerLineBack = CardArchitect.buildCenterString(center);

            List<String> bottomLinesFront = CardArchitect.buildCornerLines(blf, brf, false, null);
            List<String> bottomLinesBack = CardArchitect.buildCornerLines(blb, brb, false, null);

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

    public static PlayableCard getPlayableCard(int id) throws IllegalCardBuildException {
        return resDeck.getCardById(id)
                      .map(PlayableCard.class::cast)
                      .or(() -> goldDeck.getCardById(id))
                      .orElseThrow(() -> new IllegalCardBuildException("Card not found"));
    }

    public static FieldCard getFieldCard(int id) throws IllegalCardBuildException {
        return resDeck.getCardById(id)
                      .map(FieldCard.class::cast)
                      .or(() -> goldDeck.getCardById(id))
                      .map(FieldCard.class::cast)
                      .or(() -> starterDeck.getCardById(id))
                      .orElseThrow(() -> new IllegalCardBuildException("Card not found"));
    }

    public static ObjectiveCard getObjective(int id) throws IllegalCardBuildException {
        return objDeck.getCardById(id).orElseThrow(
                () -> new IllegalCardBuildException("Card not found"));
    }

    public static void printWaitingForTrn(MiniGameModel model) {
        List<String> playersInfo = PlayersPrinter.buildPlayers(model);
        List<String> table;
        try {
            table =
                    CardArchitect.buildTable(new ArrayList<>(model.table().getShownCards()),
                                             model.table().getDeckTop(PlayableCardType.GOLD),
                                             model.table().getDeckTop(PlayableCardType.RESOURCE));
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
        List<Integer> commObjs = new ArrayList<>(model.table().getCommonObjectives());
        List<Integer> persOnj =
                new ArrayList<>(model.getCliPlayer(model.myName()).getSpace().getPlayerObjective());

        List<String> obj;
        try {
            obj = CardArchitect.buildVertObj(commObjs.get(0),
                                             commObjs.get(1),
                                             persOnj.get(0));
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        List<String> handList;
        try {
            handList =
                    CardArchitect.buildHand(new ArrayList<>(
                            model.getCliPlayer(model.myName()).getSpace().getPlayerHand()));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        int playersInfoSize = playersInfo.size();
        int tableSize = table.size();
        int handDepth = playersInfoSize + tableSize;
        int handSize = handList.size();
        int totalSize = playersInfoSize + tableSize + handSize;

        for (int i = 0; i < totalSize; i++) {
            if (i < playersInfo.size()) {
                System.out.println(
                        playersInfo.get(i) + spaces(96 - playersInfo.get(i).length()) + obj.get(i));
            } else if (i - playersInfoSize < table.size()) {
                System.out.println(table.get(i - playersInfoSize) + spaces(23) + obj.get(i));
            } else if (i < obj.size()) {
                System.out.println(handList.get(i - handDepth) + spaces(23) + obj.get(i));
            } else {
                System.out.println(handList.get(i - handDepth));
            }
        }
    }

    public static void printHand(List<Integer> cardIds) throws IllegalCardBuildException {
        List<String> hand = CardArchitect.buildHand(cardIds);
        for (String str : hand) {
            System.out.println(str);
        }
    }

}
