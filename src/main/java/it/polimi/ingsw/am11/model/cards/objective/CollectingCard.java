package it.polimi.ingsw.am11.model.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This is the abstract <code>CollectingCard</code> class which extends the
 * <code>ObjectiveCard</code> class.
 * It is sealed, meaning it can only be subclassed by the classes specified in the permit clause. In
 * this case, the permitted subclasses are <code>ColorCollectCard</code> and
 * <code>SymbolCollectCard</code>.
 * <p>
 * The class contains methods to get color and symbol requirements, and a nested Builder class for
 * creating instances of CollectingCard.
 */
public abstract sealed class CollectingCard extends ObjectiveCard
        permits ColorCollectCard, SymbolCollectCard {

    private static final ImmutableMap<Symbol, Integer> SYMBOL_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.init(Symbol.class, 0)
    );

    private static final ImmutableMap<GameColor, Integer> COLOR_TO_COLLECT = Maps.immutableEnumMap(
            EnumMapUtils.init(GameColor.class, 0)
    );

    protected CollectingCard(@NotNull Builder<?> builder) {
        super(builder);
    }

    /**
     * This method is used to get the color requirements of the card. It returns an
     * <code>ImmutableMap</code> where the key is the color and the value is the integer
     * requirement for that color.
     *
     * @return a map of the color requirements of the card.
     */
    @Override
    public @NotNull Map<GameColor, Integer> getColorRequirements() {
        return COLOR_TO_COLLECT;
    }

    /**
     * This method is used to get the symbol requirements of the card. It returns an
     * <code>ImmutableMap</code> where the key is the symbol and the value is the integer
     * requirement for that symbol.
     *
     * @return a map of the symbol requirements of the card.
     */
    @Override
    public @NotNull Map<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_TO_COLLECT;
    }

    public static abstract class Builder<T extends CollectingCard>
            extends ObjectiveCard.Builder<CollectingCard> {

        protected Builder(int id, int points) throws IllegalArgumentException {
            super(id, points);
        }

        @Override
        public abstract T build() throws IllegalCardBuildException;
    }
}
