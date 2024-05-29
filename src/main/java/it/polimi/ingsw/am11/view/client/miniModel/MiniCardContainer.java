package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class MiniCardContainer {
    private final int cardId;
    private final EnumMap<Corner, Boolean> coveredCorners;
    private final boolean isRetro;

    public MiniCardContainer(int cardId, boolean isRetro) {
        this.cardId = cardId;
        coveredCorners = EnumMapUtils.Init(Corner.class, false);
        this.isRetro = isRetro;
    }

    public void cover(@NotNull Corner corner) {
        this.coveredCorners.put(corner, true);
    }

    public int getCardId() {
        return cardId;
    }

    public EnumMap<Corner, Boolean> getCoveredCorners() {
        return coveredCorners;
    }

    public boolean isCovered(Corner corner) {
        return coveredCorners.get(corner);
    }

    public boolean isRetro() {
        return isRetro;
    }

    public CornerContainer getContainerOn(Corner corner) {
        FieldCard fieldCard = CardDecoder.decodeFieldCard(cardId).orElseThrow();
        return fieldCard.getItemCorner(corner, isRetro);
    }

    @Override
    public String toString() {
        return "ID: " + cardId + " isRtr: " + isRetro + " covered TL: " +
               coveredCorners.get(Corner.TOP_LX).toString() +
               " TR: " + coveredCorners.get(Corner.TOP_RX).toString() + " BL: " +
               coveredCorners.get(Corner.DOWN_LX) +
               " BR: " + coveredCorners.get(Corner.DOWN_RX);

    }
}
