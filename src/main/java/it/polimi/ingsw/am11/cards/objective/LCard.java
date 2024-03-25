package it.polimi.ingsw.am11.cards.objective;

import it.polimi.ingsw.am11.cards.utils.Color;
import it.polimi.ingsw.am11.cards.utils.EnumMapUtils;
import it.polimi.ingsw.am11.cards.utils.ObjectiveCardType;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class LCard extends PositioningCard {

    private final boolean isFlippedFlag;
    private final boolean isRotatedFlag;
    private final Color primaryColor;
    private final Color secondaryColor;

    private LCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.isFlippedFlag = builder.isFlippedFlag;
        this.isRotatedFlag = builder.isRotatedFlag;
        this.primaryColor = builder.primaryColor;
        this.secondaryColor = builder.secondaryColor;
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.L_SHAPE;
    }

    @Override
    public int countPoints(PlayerField playerField) {
        return 0;
    }

    // TODO: need pattern creation logic


    public static class Builder extends PositioningCard.Builder<LCard> {

        private boolean isFlippedFlag;
        private boolean isRotatedFlag;
        private Color primaryColor;
        private Color secondaryColor;

        private EnumMap<Color, Integer> colorRequirements;

        public Builder(int id, int points) {
            super(id, points);
        }

        public @NotNull Builder isFlipped(boolean flippedFlag) {
            this.isFlippedFlag = flippedFlag;
            this.primaryColor = null;
            this.secondaryColor = null;
            this.colorRequirements = EnumMapUtils.Init(Color.class, 0);
            return this;
        }

        public @NotNull Builder isRotated(boolean rotatedFlag) {
            this.isRotatedFlag = rotatedFlag;
            return this;
        }

        public @NotNull Builder hasPrimaryColor(@NotNull Color color) {
            if (this.primaryColor != null) this.colorRequirements.put(this.primaryColor, 0);
            this.primaryColor = color;
            this.colorRequirements.put(color, 2);
            return this;
        }

        public Builder hasSecondaryColor(@NotNull Color color) {
            if (this.secondaryColor != null) this.colorRequirements.put(this.secondaryColor, 0);
            this.secondaryColor = color;
            this.colorRequirements.put(color, 1);
            return this;
        }

        @Override
        public @NotNull LCard build() {
            return new LCard(this);
        }
    }
}
