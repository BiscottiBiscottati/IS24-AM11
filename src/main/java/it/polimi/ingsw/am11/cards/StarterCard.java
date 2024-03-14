package it.polimi.ingsw.am11.cards;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class StarterCard {
    private final EnumMap<Corner, Availability> availableCornersFront;
    private final EnumMap<Corner, Color> availableColorCornerBack;
    private final EnumSet<Color> centerColorsFront;

    protected StarterCard(@NotNull Builder builder) {
        this.availableCornersFront = builder.availableCornersFront;
        this.availableColorCornerBack = builder.availableColorCornerBack;
        this.centerColorsFront = builder.centerColors;
    }

    public boolean isFrontCornerAvail(@NotNull Corner corner) {
        return availableCornersFront.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    public Color getRetroColorIn(@NotNull Corner corner) {
        return availableColorCornerBack.get(corner);
    }

    public Set<Color> getCenterColorsFront() {
        return centerColorsFront;
    }

    public static class Builder {
        private final EnumMap<Corner, Availability> availableCornersFront;
        private final EnumMap<Corner, Color> availableColorCornerBack;
        private final EnumSet<Color> centerColors;

        public Builder() {
            availableCornersFront = new EnumMap<>(Corner.class);
            availableColorCornerBack = new EnumMap<>(Corner.class);
            centerColors = EnumSet.noneOf(Color.class);
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                    .filter(key -> !availableColorCornerBack.containsKey(key))
                    .noneMatch(e -> true);
        }

        public Builder hasAvailableFrontCorner(@NotNull Corner corner) {
            availableCornersFront.put(corner, Availability.EMPTY);
            return this;
        }

        public Builder hasColorBackIn(@NotNull Corner corner, @NotNull Color color) {
            availableColorCornerBack.put(corner, color);
            return this;
        }

        public Builder hasCenterColors(Set<Color> colors) {
            centerColors.addAll(colors);
            return this;
        }

        public StarterCard build() throws IllegalBuildException {
            if (checkAllBackCornerCovered()) {
                return new StarterCard(this);
            }
            throw new IllegalBuildException("Not all Corners Covered");
        }
    }
}
