package it.polimi.ingsw.am11.cards.starter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.polimi.ingsw.am11.cards.exceptions.IllegalBuildException;
import it.polimi.ingsw.am11.cards.utils.Availability;
import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.Corner;
import it.polimi.ingsw.am11.cards.utils.EnumMapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class StarterCard {
    private final @NotNull ImmutableMap<Corner, Availability> availableCornersFront;
    private final @NotNull ImmutableMap<Corner, Color> availableColorCornerBack;
    private final @NotNull ImmutableSet<Color> centerColorsFront;

    protected StarterCard(@NotNull Builder builder) {
        this.availableCornersFront = Maps.immutableEnumMap(builder.availableCornersFront);
        this.availableColorCornerBack = Maps.immutableEnumMap(builder.availableColorCornerBack);
        this.centerColorsFront = Sets.immutableEnumSet(builder.centerColors);
    }

    public boolean isFrontCornerAvail(@NotNull Corner corner) {
        return availableCornersFront.getOrDefault(corner, Availability.NOT_USABLE).isAvailable();
    }

    public @Nullable Color getRetroColorIn(@NotNull Corner corner) {
        return availableColorCornerBack.get(corner);
    }

    public Set<Color> getCenterColorsFront() {
        return centerColorsFront;
    }

    public static class Builder {
        private final @NotNull EnumMap<Corner, Availability> availableCornersFront;
        private final @NotNull EnumMap<Corner, Color> availableColorCornerBack;
        private final @NotNull EnumSet<Color> centerColors;

        public Builder() {
            availableCornersFront = EnumMapUtils.defaultInit(Corner.class, Availability.NOT_USABLE);
            availableColorCornerBack = new EnumMap<>(Corner.class);
            centerColors = EnumSet.noneOf(Color.class);
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                    .filter(key -> !availableColorCornerBack.containsKey(key))
                    .noneMatch(e -> true);
        }

        public @NotNull Builder hasAvailableFrontCorner(@NotNull Corner corner) {
            availableCornersFront.put(corner, Availability.EMPTY);
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

        public @NotNull StarterCard build() throws IllegalBuildException {
            if (checkAllBackCornerCovered()) return new StarterCard(this);
            else throw new IllegalBuildException("Not all Corners Covered");
        }
    }
}
