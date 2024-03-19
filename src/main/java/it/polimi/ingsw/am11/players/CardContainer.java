package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.utils.Corner;
import it.polimi.ingsw.am11.cards.utils.EnumMapUtils;

import java.util.EnumMap;

public class CardContainer {

    private final PlayableCard card;
    private final EnumMap<Corner, Boolean> coveredCorners;

    public CardContainer(PlayableCard card) {
        this.card = card;
        coveredCorners = EnumMapUtils.Init(Corner.class, false);
    }

    public PlayableCard getCard() {
        return card;
    }

    public EnumMap<Corner, Boolean> getCoveredCorners() {
        return coveredCorners;
    }

    public void cover(Corner corner) {

    }

}
