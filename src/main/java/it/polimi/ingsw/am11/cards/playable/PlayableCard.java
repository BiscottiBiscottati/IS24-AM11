package it.polimi.ingsw.am11.cards.playable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;


public abstract sealed class PlayableCard implements FieldCard permits GoldCard, ResourceCard {
    private static final ImmutableMap<Corner, Availability> retroCorners = Maps.immutableEnumMap(
            EnumMapUtils.Init(Corner.class, Availability.USABLE)
    );
    private final Color color;
    private final int points;

    /**
     * @param builder A builder of its subclasses to set color and points values
     */
    protected PlayableCard(@NotNull Builder builder) {
        this.color = builder.primaryColor;
        this.points = builder.cardPoints;
    }

    public static boolean isRetroAvailable(@NotNull Corner corner) {
        return Objects.requireNonNull(retroCorners.get(corner)).isAvailable();
    }

    /**
     * @return points value of the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return color of the card
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the type of playable card
     * @see PlayableCardType
     */
    @NotNull
    public abstract PlayableCardType getType();

    /**
     * @param corner The corner to check
     * @return true if available false otherwise
     */
    public abstract boolean isAvailable(@NotNull Corner corner);

    /**
     * @return A Map of keys colors and as values their requirements to place on the field in int
     */
    @NotNull
    public abstract ImmutableMap<Color, Integer> getPlacingRequirements();

    /**
     * @return type of requirement needed to place
     * @see PointsRequirementsType
     */
    @NotNull
    public abstract PointsRequirementsType getPointsRequirements();

    /**
     * @param corner The corner to check
     * @return an item if there's one otherwise gives its Availability
     * @see CornerContainer
     */
    @NotNull
    public abstract CornerContainer checkItemCorner(@NotNull Corner corner);

    /**
     * @return a symbol if the cards permits a symbol to collect otherwise empty optional
     */
    @NotNull
    public abstract Optional<Symbol> getSymbolToCollect();

    /**
     * @param color The color to check
     * @return true if this card's color is equal to color param otherwise false
     */
    @Override
    public boolean isColorEqual(Color color) {
        return this.color == color;
    }

    @Override
    public boolean equals(Object color) {
        if (color instanceof Color) return this.color == color;
        else return super.equals(color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.points);
    }

    /**
     * The builder for creating Playable cards needs to be inherited to subclasses
     */
    public abstract static class Builder {
        private final int cardPoints;
        private final @NotNull Color primaryColor;

        /**
         * @param cardPoints   Points value of the card to create
         * @param primaryColor Color of the card to create
         * @throws IllegalBuildException if cardPoints are negative
         */
        protected Builder(int cardPoints, @NotNull Color primaryColor) throws IllegalBuildException {
            if (cardPoints < 0) throw new IllegalBuildException("Points cannot be less than 0!");
            this.cardPoints = cardPoints;
            this.primaryColor = primaryColor;
        }

        /**
         * @return A new instance of PlayableCard
         * @throws IllegalBuildException if the build is incomplete or impossible
         */
        public abstract PlayableCard build() throws IllegalBuildException;
    }


}
