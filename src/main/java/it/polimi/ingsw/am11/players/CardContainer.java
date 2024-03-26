package it.polimi.ingsw.am11.players;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class CardContainer {

    private final FieldCard card;
    private final EnumMap<Corner, Boolean> coveredCorners;

    public CardContainer(FieldCard card) {
        this.card = card;
        coveredCorners = EnumMapUtils.Init(Corner.class, false);
    }

    @Contract("_ -> new")
    public static @NotNull CardContainer of(FieldCard card) {
        return new CardContainer(card);
    }

    public FieldCard getCard() {
        return card;
    }

    public ImmutableMap<Corner, Boolean> getCoveredCorners() {
        return Maps.immutableEnumMap(this.coveredCorners);
    }

    public void cover(@NotNull Corner corner) {
        this.coveredCorners.put(corner, true);
    }

    public boolean colorEquals(Color color) {
        if (color != null) return this.card.isColorEqual(color);
        return false;
    }

    public boolean isCornerCovered(@NotNull Corner corner) {
        return this.coveredCorners.get(corner);
    }

}
