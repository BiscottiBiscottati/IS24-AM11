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

    void reset() {
        exposedColors.replaceAll((color, count) -> 0);
        exposedSymbols.replaceAll((symbol, count) -> 0);
        placedCardColors.replaceAll((color, count) -> 0);
    }

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

    void addCardColor(@NotNull GameColor color) {
        this.placedCardColors.merge(color, 1, Integer::sum);
    }

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

    void addToExposed(@NotNull Item item) {
        switch (item) {
            case GameColor color -> this.exposedColors.merge(color, 1, Integer::sum);
            case Symbol symbol -> this.exposedSymbols.merge(symbol, 1, Integer::sum);
        }
    }

    int getPlacedCardOf(@NotNull GameColor color) {
        return this.placedCardColors.get(color);
    }

    Map<GameColor, Integer> getPlacedCardColors() {
        return Map.copyOf(this.placedCardColors);
    }

    boolean isRequirementsMet(@NotNull PlayableCard card, boolean isRetro) {
        if (isRetro) return true;
        return Stream.of(GameColor.values())
                     .allMatch(color -> card.getPlacingRequirementsOf(color) <=
                                        this.getExposedItem(color));
    }

    int getExposedItem(@NotNull Item item) {
        return switch (item) {
            case GameColor color -> this.exposedColors.get(color);
            case Symbol symbol -> this.exposedSymbols.get(symbol);
        };
    }

    @NotNull
    ItemManagerMemento save() {
        return new ItemManagerMemento(Map.copyOf(exposedColors), Map.copyOf(exposedSymbols),
                                      Map.copyOf(placedCardColors));
    }

    void load(@NotNull ItemManagerMemento memento) {
        exposedColors.putAll(memento.exposedColors());
        exposedSymbols.putAll(memento.exposedSymbols());
        placedCardColors.putAll(memento.placedCardColors());
    }

}
