package it.polimi.ingsw.am11.cards.starter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.polimi.ingsw.am11.cards.utils.CardIdentity;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class StarterCard implements FieldCard, CardIdentity {

    private final int id;
    private final ImmutableMap<Corner, CornerContainer> availableCornersFront;
    private final ImmutableMap<Corner, Color> availableColorCornerBack;
    private final ImmutableSet<Color> centerColorsFront;

    private StarterCard(@NotNull Builder builder) {
        this.id = builder.id;
        this.availableCornersFront = Maps.immutableEnumMap(builder.availableCornersFront);
        this.availableColorCornerBack = Maps.immutableEnumMap(builder.availableColorCornerBack);
        this.centerColorsFront = Sets.immutableEnumSet(builder.centerColors);
    }

    public boolean isFrontAvail(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornersFront.get(corner)).isAvailable();
    }

    public CornerContainer getFront(@NotNull Corner corner) {
        return Objects.requireNonNull(availableCornersFront.get(corner));
    }

    @NotNull
    public Color getRetroColorIn(@NotNull Corner corner) {
        return Objects.requireNonNull(availableColorCornerBack.get(corner));
    }

    @NotNull
    public Set<Color> getCenterColorsFront() {
        return centerColorsFront;
    }

    @Override
    public boolean isColorEqual(Color color) {
        return false;
    }

    @Contract(pure = true)
    @Override
    public int getId() {
        return this.id;
    }

    public static class Builder {
        private final int id;
        private final EnumMap<Corner, CornerContainer> availableCornersFront;
        private final EnumMap<Corner, Color> availableColorCornerBack;
        private final EnumSet<Color> centerColors;

        public Builder(int id) {
            this.id = id;
            this.availableCornersFront = EnumMapUtils.Init(Corner.class, Availability.NOT_USABLE);
            this.availableColorCornerBack = new EnumMap<>(Corner.class);
            this.centerColors = EnumSet.noneOf(Color.class);
        }

        private boolean checkAllBackCornerCovered() {
            return Arrays.stream(Corner.values())
                         .filter(key -> !availableColorCornerBack.containsKey(key))
                         .noneMatch(e -> true);
        }

        public @NotNull Builder hasItemFrontIn(@NotNull Corner corner, @NotNull CornerContainer item)
                throws IllegalCardBuildException {
            switch (item) {
                case Availability availability -> availableCornersFront.put(corner, availability);
                case Color color -> availableCornersFront.put(corner, color);
                default -> throw new IllegalCardBuildException("Illegal Item for starter:" + item);
            }
            return this;
        }

        public @NotNull Builder hasColorRetroIn(@NotNull Corner corner, @NotNull Color color) {
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
        public StarterCard build() throws IllegalCardBuildException {
            if (checkAllBackCornerCovered()) return new StarterCard(this);
            else throw new IllegalCardBuildException("Not all Corners Covered");
        }
    }
}
