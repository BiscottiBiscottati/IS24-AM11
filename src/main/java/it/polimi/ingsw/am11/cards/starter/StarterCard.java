package it.polimi.ingsw.am11.cards.starter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.polimi.ingsw.am11.cards.utils.*;
import it.polimi.ingsw.am11.exceptions.IllegalBuildException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public final class StarterCard implements FieldCard {
    private final ImmutableMap<Corner, Availability> availableCornersFront;
    private final ImmutableMap<Corner, Color> availableColorCornerBack;
    private final ImmutableSet<Color> centerColorsFront;

    private StarterCard(@NotNull Builder builder) {
        this.availableCornersFront = Maps.immutableEnumMap(builder.availableCornersFront);
        this.availableColorCornerBack = Maps.immutableEnumMap(builder.availableColorCornerBack);
        this.centerColorsFront = Sets.immutableEnumSet(builder.centerColors);
    }

    public boolean isFrontCornerAvail(@NotNull Corner corner) {
        return availableCornersFront.get(corner).isAvailable();
    }

    @NotNull
    public Color getRetroColorIn(@NotNull Corner corner) {
        return availableColorCornerBack.get(corner);
    }

    @NotNull
    public Set<Color> getCenterColorsFront() {
        return centerColorsFront;
    }

    public static class Builder {
        private final EnumMap<Corner, Availability> availableCornersFront;
        private final EnumMap<Corner, Color> availableColorCornerBack;
        private final EnumSet<Color> centerColors;

        public Builder() {
            availableCornersFront = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
            availableColorCornerBack = new EnumMap<>(Corner.class);
            centerColors = EnumSet.noneOf(Color.class);
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                         .filter(key -> !availableColorCornerBack.containsKey(key))
                         .noneMatch(e -> true);
        }

        public @NotNull Builder hasAvailableFrontCorner(@NotNull Corner corner) {
            availableCornersFront.put(corner, Availability.USABLE);
            return this;
        }

        public @NotNull Builder hasColorBackIn(@NotNull Corner corner, @NotNull Color color) {
            availableColorCornerBack.put(corner, color);
            return this;
        }

        public @NotNull Builder hasCenterColors(@NotNull Set<Color> colors) {
            centerColors.addAll(colors);
            return this;
        }


        public @NotNull Builder hasCenterColor(@NotNull Color color) {
            centerColors.add(color);
            return this;
        }

        @NotNull
        public StarterCard build() throws IllegalBuildException {
            if (checkAllBackCornerCovered()) return new StarterCard(this);
            else throw new IllegalBuildException("Not all Corners Covered");
        }
    }
}
