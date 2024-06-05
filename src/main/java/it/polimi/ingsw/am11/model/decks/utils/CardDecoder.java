package it.polimi.ingsw.am11.model.decks.utils;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.starter.StarterDeckFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CardDecoder {
    private static final Deck<ObjectiveCard> objectiveDeck = ObjectiveDeckFactory.createDeck();
    private static final Deck<ResourceCard> resourceDeck = ResourceDeckFactory.createDeck();
    private static final Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();
    private static final Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();

    public static Optional<ObjectiveCard> decodeObjectiveCard(int cardId) {
        return objectiveDeck.getCardById(cardId);
    }

    public static @NotNull Optional<FieldCard> decodeFieldCard(int cardId) {
        return decodeResourceCard(cardId).map(FieldCard.class::cast)
                                         .or(() -> decodeGoldCard(cardId))
                                         .or(() -> decodeStarterCard(cardId));
    }

    public static Optional<ResourceCard> decodeResourceCard(int cardId) {
        return resourceDeck.getCardById(cardId);
    }

    public static Optional<GoldCard> decodeGoldCard(int cardId) {
        return goldDeck.getCardById(cardId);
    }

    public static Optional<StarterCard> decodeStarterCard(int cardId) {
        return starterDeck.getCardById(cardId);
    }

    public static @NotNull Optional<PlayableCard> decodePlayableCard(int cardId) {
        return decodeResourceCard(cardId).map(PlayableCard.class::cast)
                                         .or(() -> decodeGoldCard(cardId));
    }
}
