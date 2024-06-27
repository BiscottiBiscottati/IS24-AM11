package it.polimi.ingsw.am11.model.cards.objective;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This is the abstract <code>PositioningCard</code> class that extends the
 * <code>ObjectiveCard</code> class.
 * <p>
 * It represents an objective card for positioning a specific pattern on the player's field.
 * <p>
 * It is sealed, meaning it can only be subclassed by the classes specified in the permit clause.
 * <p>
 * In this case, the permitted subclasses are <code>LCard</code> and <code>TripletCard</code>.
 * <p>
 * The class contains methods to get color and symbol requirements, a method to get the pattern, and
 * a nested Builder class for creating instances of PositioningCard.
 */
public abstract sealed class PositioningCard extends ObjectiveCard
        permits LCard, TripletCard {
    private static final ImmutableMap<Symbol, Integer> SYMBOL_REQUIREMENTS = Maps.immutableEnumMap(
            ImmutableMap.of(
                    Symbol.FEATHER, 0,
                    Symbol.GLASS, 0,
                    Symbol.PAPER, 0
            )
    );

    private final @NotNull ImmutableMap<GameColor, Integer> colorRequirements;

    protected PositioningCard(@NotNull Builder<?> builder,
                              @NotNull EnumMap<GameColor, Integer> colorRequirements) {
        super(builder);
        this.colorRequirements = Maps.immutableEnumMap(colorRequirements);
    }

    @Override
    public @NotNull Map<GameColor, Integer> getColorRequirements() {
        return colorRequirements;
    }

    @Override
    public @NotNull Map<Symbol, Integer> getSymbolRequirements() {
        return SYMBOL_REQUIREMENTS;
    }

    public abstract List<List<GameColor>> getPattern();

    public static abstract class Builder<T extends PositioningCard>
            extends ObjectiveCard.Builder<PositioningCard> {

        protected Builder(int id, int points) {
            super(id, points);
        }

        @Override
        public abstract T build() throws IllegalCardBuildException;
    }
}
