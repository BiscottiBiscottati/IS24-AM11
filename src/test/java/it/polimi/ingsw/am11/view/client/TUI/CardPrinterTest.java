package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.Test;

class CardPrinterTest {

    @Test
    void printCardFrontAndBack() throws IllegalCardBuildException {
        Deck<ObjectiveCard> objDeck = ObjectiveDeckFactory.createDeck();

        int remainingCards = objDeck.getRemainingCards();
        for (int i = 0; i < remainingCards; i++) {
            CardPrinter.printCardFrontAndBack(objDeck.draw().orElseThrow().getId());
        }
    }

    @Test
    void testPrintCardFrontAndBack() {
        try {
            StarterCard sCard = CardPrinter.getStarterCard(102);
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        try {
            CardPrinter.printCardFrontAndBack(102);
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getStarterCard() {
    }

    @Test
    void getLetter() {

        StarterCard sCard = null;
        try {
            sCard = CardPrinter.getStarterCard(102);
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        System.out.println(sCard.getItemCorner(Corner.TOP_LX, false));

        char tlf = CardPrinter.getLetter(sCard.getItemCorner(Corner.TOP_LX, false));
        char trf = CardPrinter.getLetter(sCard.getItemCorner(Corner.TOP_RX, false));
        char blf = CardPrinter.getLetter(sCard.getItemCorner(Corner.DOWN_LX, false));
        char brf = CardPrinter.getLetter(sCard.getItemCorner(Corner.DOWN_RX, false));

        char tlb = CardPrinter.getLetter(sCard.getItemCorner(Corner.TOP_LX, true));
        char trb = CardPrinter.getLetter(sCard.getItemCorner(Corner.TOP_RX, true));
        char blb = CardPrinter.getLetter(sCard.getItemCorner(Corner.DOWN_LX, true));
        char brb = CardPrinter.getLetter(sCard.getItemCorner(Corner.DOWN_RX, true));

        System.out.println(
                "|" + tlf + "|" + trf + "|" + blf + "|" + brf + "|" + tlb + "|" + trb + "|" + blb +
                "|" + brb);
    }

    @Test
    void buildCornerLines() {

        CardPrinter.buildCornerLines(' ', ' ', true, null);

    }

    @Test
    void buildCenterString() {
    }

    @Test
    void spaces() {
    }

    @Test
    void getColorLetter() {
    }

    @Test
    void getCard() {
    }

    @Test
    void buildRequirementsString() {
    }

    @Test
    void printTable() {
    }

    @Test
    void printHand() {
    }

    @Test
    void buildCard() {
    }

    @Test
    void buildDeck() {
    }

    @Test
    void printObjectives() {
    }

    @Test
    void buildPointsString() {
    }
}