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

/**
 * The CardDecoder class is a utility class used for decoding cards in the game.
 * <p>
 * It contains static methods for decoding different types of cards, such as ObjectiveCard,
 * FieldCard, ResourceCard, GoldCard, and StarterCard.
 * <p>
 * Each method takes an integer cardId as a parameter and returns an Optional of the corresponding
 * card.
 * <p>
 * The cardId is used to fetch the card from the appropriate deck.
 * <p>
 * If a card with the given id is found in the deck, it is returned wrapped in an Optional. If no
 * card is found, an empty Optional is returned.
 */
public class CardDecoder {
    private static final Deck<ObjectiveCard> objectiveDeck = ObjectiveDeckFactory.createDeck();
    private static final Deck<ResourceCard> resourceDeck = ResourceDeckFactory.createDeck();
    private static final Deck<GoldCard> goldDeck = GoldDeckFactory.createDeck();
    private static final Deck<StarterCard> starterDeck = StarterDeckFactory.createDeck();

    public static @NotNull Optional<ObjectiveCard> decodeObjectiveCard(int cardId) {
        return objectiveDeck.getCardById(cardId);
    }

    public static @NotNull Optional<FieldCard> decodeFieldCard(int cardId) {
        return decodeResourceCard(cardId).map(FieldCard.class::cast)
                                         .or(() -> decodeGoldCard(cardId))
                                         .or(() -> decodeStarterCard(cardId));
    }

    public static @NotNull Optional<ResourceCard> decodeResourceCard(int cardId) {
        return resourceDeck.getCardById(cardId);
    }

    public static @NotNull Optional<GoldCard> decodeGoldCard(int cardId) {
        return goldDeck.getCardById(cardId);
    }

    public static @NotNull Optional<StarterCard> decodeStarterCard(int cardId) {
        return starterDeck.getCardById(cardId);
    }

    public static @NotNull Optional<PlayableCard> decodePlayableCard(int cardId) {
        return decodeResourceCard(cardId).map(PlayableCard.class::cast)
                                         .or(() -> decodeGoldCard(cardId));
    }
}
