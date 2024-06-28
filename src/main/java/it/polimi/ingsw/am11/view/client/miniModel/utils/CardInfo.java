package it.polimi.ingsw.am11.view.client.miniModel.utils;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;

public class CardInfo {
    private static final Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();
    private static final Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();
    private static final Deck<ResourceCard> resDeck = ResourceDeckFactory.createDeck();
    private static final Deck<ObjectiveCard> objDeck = ObjectiveDeckFactory.createDeck();


    public static PlayableCardType getPlayableCardType(int id) throws IllegalCardBuildException {
        return resDeck.getCardById(id)
                      .map(ResourceCard::getType)
                      .or(() -> goldDeck.getCardById(id).map(GoldCard::getType))
                      .orElseThrow(() -> new IllegalCardBuildException("Card not found"));
    }

    public static GameColor getPlayabelCardColor(int id) throws IllegalCardBuildException {
        return resDeck.getCardById(id)
                      .map(ResourceCard::getColor)
                      .or(() -> goldDeck.getCardById(id).map(GoldCard::getColor))
                      .orElseThrow(() -> new IllegalCardBuildException("Card not found"));
    }


}
