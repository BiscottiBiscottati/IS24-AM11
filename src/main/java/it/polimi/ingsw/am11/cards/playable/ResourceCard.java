package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.*;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a resource card.
 * <p>
 * Each attribute is immutable.
 * <p>
 * The class can only be instantiated through the static inner builder
 * {@link ResourceCard.Builder}.
 */
public final class ResourceCard extends PlayableCard {
    private final static ImmutableMap<Color, Integer> PLACING_REQUIREMENTS = Maps.immutableEnumMap(
            EnumMapUtils.Init(Color.class, 0)
    );
    private final ImmutableMap<Corner, CornerContainer> availableCornerOrItem;

    /**
     * @param builder The builder used for the creation of this instance
     */
    private ResourceCard(@NotNull Builder builder) {
        super(builder);
        this.availableCornerOrItem = Maps.immutableEnumMap(builder.availableCornerOrItem);
    }

    @Override
    @NotNull
    public PlayableCardType getType() {
        return PlayableCardType.RESOURCE;
    }

    @Override
    @Contract(pure = true)
    public boolean isAvailable(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornerOrItem.getOrDefault(corner, Availability.NOT_USABLE))
                      .isAvailable();
    }

    @Override
    @NotNull
    public ImmutableMap<Color, Integer> getPlacingRequirements() {
        return PLACING_REQUIREMENTS;
    }

    @Override
    @NotNull
    public PointsRequirementsType getPointsRequirements() {
        return PointsRequirementsType.CLASSIC;
    }

    @Override
    @NotNull
    @Contract(pure = true)
    public CornerContainer checkItemCorner(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornerOrItem.get(corner));
    }

    @Override
    @NotNull
    public Optional<Symbol> getSymbolToCollect() {
        return Optional.empty();
    }

    /**
     * Builder class for creating instances of {@link ResourceCard}.
     * This builder provides methods to set the required attributes
     * for the target object.
     */
    public static class Builder extends PlayableCard.Builder<ResourceCard> {
        private final EnumMap<Corner, CornerContainer> availableCornerOrItem;

        /**
         * @param cardPoints       the point value of the card
         * @param cardPrimaryColor the color of the card
         * @throws IllegalBuildException if points are negative
         */
        public Builder(int id, int cardPoints, @NotNull Color cardPrimaryColor) throws IllegalBuildException {
            super(id, cardPoints, cardPrimaryColor);
            this.availableCornerOrItem = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
        }

        /**
         * @param corner          The corner to set
         * @param cornerContainer The option to put on the corner can contain an item or be empty or not usable
         * @return The modified builder
         * @see CornerContainer
         */
        @NotNull
        public Builder hasIn(@NotNull Corner corner, @NotNull CornerContainer cornerContainer) {
            availableCornerOrItem.put(corner, cornerContainer);
            return this;
        }

        /**
         * Constructs a new instance of <code>ResourceCard</code> using the
         * parameters set by the builder's methods.
         *
         * @return A fully constructed instance of <code>ResourceCard</code>.
         */
        @NotNull
        public ResourceCard build() {
            return new ResourceCard(this);
        }


    }
}
