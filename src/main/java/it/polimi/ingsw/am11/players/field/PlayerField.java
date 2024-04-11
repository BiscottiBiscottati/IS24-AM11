package it.polimi.ingsw.am11.players.field;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlayerField {

    private final ExposedItemManager itemManager;
    private final PositionManager positionManager;

    public PlayerField() {
        this.itemManager = new ExposedItemManager();
        this.positionManager = new PositionManager();
    }

    public void clearAll() {
        this.itemManager.reset();
        this.positionManager.reset();
    }

    public int placeStartingCard(@NotNull StarterCard firstCard,
                                 boolean isRetro)
            throws IllegalCardPlacingException {
        Position starterPos = Position.of(0, 0);

        // Position the card
        this.positionManager.placeCard(firstCard, starterPos, isRetro);

        // Update exposed items
        this.itemManager.addExposedItemOn(firstCard, isRetro);

        return 0;
    }

    public int place(@NotNull PlayableCard card,
                     @NotNull Position position,
                     boolean isRetro)
            throws IllegalCardPlacingException {

        // Check if starter position is available and position the card
        if (Objects.equals(position, Position.of(0, 0)))
            throw new IllegalCardPlacingException("Cannot place PlayableCard in starter position!");
        this.positionManager.placeCard(card, position, isRetro)
                            .forEach(this.itemManager::subToExposed);

        // Update exposed items
        // FIXME does it remove covered colors and items?
        this.itemManager.addCardColor(card.getColor());
        this.itemManager.addExposedItemOn(card, isRetro);

        return card.countPoints(this, position);
    }

    public Set<Position> getAvailablePositions() {
        return this.positionManager.getAvailablePositions();
    }

    public boolean isAvailable(@NotNull Position position) {
        return this.positionManager.isAvailable(position);
    }

    public int getNumberOf(@NotNull Item item) {
        return itemManager.getExposedItem(item);
    }

    public boolean containsCard(@NotNull FieldCard card) {
        return this.positionManager.containsCard(card);
    }

    public Map<Position, CardContainer> getCardsPositioned() {
        return this.positionManager.getCardsPositioned();
    }

    public Map<Color, Integer> getPlacedCardColours() {
        return this.itemManager.getPlacedCardColors();
    }

    public int getNumberOfPositionedColor(@NotNull Color color) {
        return this.itemManager.getNumberPlacedCardOf(color);
    }

}
