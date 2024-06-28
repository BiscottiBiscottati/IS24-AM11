package it.polimi.ingsw.am11.model.players.field;

import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.utils.memento.ItemManagerMemento;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ExposedItemManager {

    private final @NotNull EnumMap<GameColor, Integer> exposedColors;
    private final @NotNull EnumMap<Symbol, Integer> exposedSymbols;
    private final @NotNull EnumMap<GameColor, Integer> placedCardColors;

    ExposedItemManager() {
        exposedColors = EnumMapUtils.init(GameColor.class, 0);
        exposedSymbols = EnumMapUtils.init(Symbol.class, 0);
        placedCardColors = EnumMapUtils.init(GameColor.class, 0);
    }

    /**
     * Reset the exposed items counters.
     */
    void reset() {
        exposedColors.replaceAll((color, count) -> 0);
        exposedSymbols.replaceAll((symbol, count) -> 0);
        placedCardColors.replaceAll((color, count) -> 0);
    }

    /**
     * Subtract an item from the exposed counter.
     *
     * @param item The item to subtract.
     */
    void subToExposed(@NotNull Item item) {
        switch (item) {
            case GameColor color -> this.exposedColors.computeIfPresent(color,
                                                                        (c, count) -> (count > 0) ?
                                                                                      count - 1
                                                                                                  : 0);
            case Symbol symbol -> this.exposedSymbols.computeIfPresent(symbol,
                                                                       (s, count) -> (count > 0) ?
                                                                                     count - 1 : 0);
        }
    }

    /**
     * Add a card color to the placed card counter.
     *
     * @param color The color of the card to add.
     */
    void addCardColor(@NotNull GameColor color) {
        this.placedCardColors.merge(color, 1, Integer::sum);
    }

    /**
     * Add the exposed items of the specified card to the exposed counter.
     *
     * @param card    The card to add the items from.
     * @param isRetro Whether the card is placed retro or not.
     */
    void addExposedItemOn(@NotNull FieldCard card,
                          boolean isRetro) {
        // add card's corner items to the exposed counter
        Stream.of(Corner.values())
              .map(corner -> card.getItemCorner(corner, isRetro).getItem())
              .filter(Optional::isPresent)
              .map(Optional::get)
              .forEach(this::addToExposed);

        // add card center colors to the exposed counter
        card.getCenter(isRetro)
            .forEach(this::addToExposed);
    }

    /**
     * Add an item to the exposed counter.
     *
     * @param item The item to add.
     */
    void addToExposed(@NotNull Item item) {
        switch (item) {
            case GameColor color -> this.exposedColors.merge(color, 1, Integer::sum);
            case Symbol symbol -> this.exposedSymbols.merge(symbol, 1, Integer::sum);
        }
    }

    /**
     * Get the number of placed cards of the specified color.
     *
     * @param color The color to get the number of placed cards of.
     * @return The number of placed cards of the specified color.
     */
    int getPlacedCardOf(@NotNull GameColor color) {
        return this.placedCardColors.get(color);
    }

    /**
     * Get the number of placed cards for each color
     *
     * @return An unmodifiable map of the number of placed cards for each color.
     */
    Map<GameColor, Integer> getPlacedCardColors() {
        return Map.copyOf(this.placedCardColors);
    }

    /**
     * Check if the requirements of the specified card are met.
     *
     * @param card    The card to check the requirements of.
     * @param isRetro Whether the card is placed retro or not.
     * @return True if the requirements are met, false otherwise.
     */
    boolean isRequirementsMet(@NotNull PlayableCard card, boolean isRetro) {
        if (isRetro) return true;
        return Stream.of(GameColor.values())
                     .allMatch(color -> card.getPlacingRequirementsOf(color) <=
                                        this.getExposedItem(color));
    }

    /**
     * Get the number of exposed items of the specified item.
     *
     * @param item The item to get the number of exposed items of.
     * @return The number of exposed items of the specified item.
     */
    int getExposedItem(@NotNull Item item) {
        return switch (item) {
            case GameColor color -> this.exposedColors.get(color);
            case Symbol symbol -> this.exposedSymbols.get(symbol);
        };
    }

    /**
     * Save the current state of the item manager into a memento.
     *
     * @return The memento with the current state of the item manager.
     */
    @NotNull
    ItemManagerMemento save() {
        return new ItemManagerMemento(Map.copyOf(exposedColors), Map.copyOf(exposedSymbols),
                                      Map.copyOf(placedCardColors));
    }

    /**
     * Load the state of the item manager from a memento.
     *
     * @param memento The memento to load the state from.
     */
    void load(@NotNull ItemManagerMemento memento) {
        exposedColors.putAll(memento.exposedColors());
        exposedSymbols.putAll(memento.exposedSymbols());
        placedCardColors.putAll(memento.placedCardColors());
    }

}
