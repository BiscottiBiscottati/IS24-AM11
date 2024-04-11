package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ExposedItemManager {

    private final EnumMap<Color, Integer> exposedColors;
    private final EnumMap<Symbol, Integer> exposedSymbols;
    private final EnumMap<Color, Integer> placedCardColors;

    ExposedItemManager() {
        exposedColors = EnumMapUtils.Init(Color.class, 0);
        exposedSymbols = EnumMapUtils.Init(Symbol.class, 0);
        placedCardColors = EnumMapUtils.Init(Color.class, 0);
    }

    void reset() {
        exposedColors.replaceAll((color, count) -> 0);
        exposedSymbols.replaceAll((symbol, count) -> 0);
        placedCardColors.replaceAll((color, count) -> 0);
    }

    void addToExposed(@NotNull Item item) {
        switch (item) {
            case Color color -> this.exposedColors.merge(color, 1, Integer::sum);
            case Symbol symbol -> this.exposedSymbols.merge(symbol, 1, Integer::sum);
        }
    }

    void subToExposed(@NotNull Item item) {
        switch (item) {
            case Color color -> this.exposedColors.merge(color, -1, Integer::sum);
            case Symbol symbol -> this.exposedSymbols.merge(symbol, -1, Integer::sum);
        }
    }

    void addCardColor(@NotNull Color color) {
        this.placedCardColors.merge(color, 1, Integer::sum);
    }

    void addExposedItemOn(@NotNull FieldCard card,
                          boolean isRetro) {
        // add card's corner items to exposed counter
        Stream.of(Corner.values())
              .map(corner -> card.getItemCorner(corner, isRetro).getItem())
              .filter(Optional::isPresent)
              .map(Optional::get)
              .forEach(this::addToExposed);

        // add card center colors to exposed counter
        card.getCenter(isRetro)
            .forEach(this::addToExposed);
    }

    public int getExposedItem(@NotNull Item item) {
        return switch (item) {
            case Color color -> this.exposedColors.get(color);
            case Symbol symbol -> this.exposedSymbols.get(symbol);
        };
    }

    public int getNumberPlacedCardOf(@NotNull Color color) {
        return this.placedCardColors.get(color);
    }

    public Map<Color, Integer> getPlacedCardColors() {
        return Map.copyOf(this.placedCardColors);
    }

}
