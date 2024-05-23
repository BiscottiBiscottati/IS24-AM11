package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CardPrinterTest {
    static final Logger LOGGER = LoggerFactory.getLogger(CardPrinterTest.class);

    @Test
    void printObjectiveCards() throws IllegalCardBuildException {
        Deck<ObjectiveCard> objDeck = ObjectiveDeckFactory.createDeck();

        int remainingCards = objDeck.getRemainingCards();
        for (int i = 0; i < remainingCards; i++) {
            int id = objDeck.draw().orElseThrow().getId();
            LOGGER.info("Objective Card id : {}", id);
            CardPrinter.printCardFrontAndBack(id);
        }
    }

    @Test
    void printStartCard() throws IllegalCardBuildException {
        Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();

        int remainingCards = starterDeck.getRemainingCards();
        for (int i = 0; i < remainingCards; i++) {
            int id = starterDeck.draw().orElseThrow().getId();
            LOGGER.info("Starter Card id : {}", id);
            CardPrinter.printCardFrontAndBack(id);
        }
    }

    @Test
    void printResourceCards() throws IllegalCardBuildException {
        Deck<ResourceCard> resourceDeck = ResourceDeckFactory.createDeck();

        int remainingCards = resourceDeck.getRemainingCards();
        for (int i = 0; i < remainingCards; i++) {
            int id = resourceDeck.draw().orElseThrow().getId();
            LOGGER.info("Resource Card id : {}", id);
            CardPrinter.printCardFrontAndBack(id);
        }
    }

    @Test
    void printGoldCard() throws IllegalCardBuildException {
        Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();

        int remainingCards = goldDeck.getRemainingCards();
        for (int i = 0; i < remainingCards; i++) {
            int id = goldDeck.draw().orElseThrow().getId();
            LOGGER.info("Gold Card id : {}", id);
            CardPrinter.printCardFrontAndBack(id);
        }
    }
}