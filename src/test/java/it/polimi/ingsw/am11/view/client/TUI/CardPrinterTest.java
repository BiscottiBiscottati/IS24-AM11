package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
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
}