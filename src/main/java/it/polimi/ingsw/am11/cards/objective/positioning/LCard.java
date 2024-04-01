package it.polimi.ingsw.am11.cards.objective.positioning;

import it.polimi.ingsw.am11.cards.objective.PositioningCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.ObjectiveCardType;
import it.polimi.ingsw.am11.cards.utils.enums.PatternPurpose;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.players.PlayerField;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class LCard extends PositioningCard {

    private final boolean isFlippedFlag;
    private final boolean isRotatedFlag;
    private final Color primaryColor;
    private final Color secondaryColor;

    private final LPatternCounter counter;

    private LCard(@NotNull Builder builder) {
        super(builder, builder.colorRequirements);
        this.isFlippedFlag = builder.isFlippedFlag;
        this.isRotatedFlag = builder.isRotatedFlag;
        this.primaryColor = builder.primaryColor;
        this.secondaryColor = builder.secondaryColor;

        EnumMap<PatternPurpose, List<Corner>> cornersPurpose = new EnumMap<>(PatternPurpose.class);
        List<Corner> cornersToGetToTop = List.of(Corner.TOP_LX, Corner.TOP_RX);
        List<Corner> cornersToGetToBottom = List.of(Corner.DOWN_LX, Corner.DOWN_RX);

        for (PatternPurpose purpose : PatternPurpose.values()) {
            switch (purpose) {
                case NEXT_CHECK -> cornersPurpose.put(
                        purpose,
                        this.isRotatedFlag ? cornersToGetToTop : cornersToGetToBottom
                );
                case PREVIOUS_CHECK -> cornersPurpose.put(
                        purpose,
                        this.isRotatedFlag ? cornersToGetToBottom : cornersToGetToTop
                );
                case TO_COMPLETE -> {
                    Arrays.stream(Corner.values())
                          .filter(corner -> {
                              if (this.isRotatedFlag) {
                                  return corner == Corner.TOP_LX || corner == Corner.TOP_RX;
                              } else {
                                  return corner == Corner.DOWN_LX || corner == Corner.DOWN_RX;
                              }
                          })
                          .filter(corner -> {
                              if (this.isFlippedFlag) {
                                  return corner == Corner.TOP_LX || corner == Corner.DOWN_LX;
                              } else {
                                  return corner == Corner.TOP_RX || corner == Corner.DOWN_RX;
                              }
                          })
                          .forEach(corner -> cornersPurpose.put(purpose, List.of(corner)));
                }
                case ADJACENT_RX, ADJACENT_LX -> cornersPurpose.put(
                        purpose,
                        null
                );
            }
        }
        this.counter = new LPatternCounter(this.primaryColor, this.secondaryColor, cornersPurpose);
    }

    @Override
    @NotNull
    public ObjectiveCardType getType() {
        return ObjectiveCardType.L_SHAPE;
    }

    @Override
    public int countPoints(@NotNull PlayerField playerField) {
        if (playerField.getNumberOf(this.primaryColor) < 2) return 0;
        if (playerField.getNumberOf(this.secondaryColor) < 1) return 0;
        return this.counter.count(playerField) * this.getPoints();
    }


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
