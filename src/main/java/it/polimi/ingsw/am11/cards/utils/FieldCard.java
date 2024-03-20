package it.polimi.ingsw.am11.cards.utils;

import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;

public sealed interface FieldCard permits PlayableCard, StarterCard {
    boolean isColorEqual(Color color);
    

    boolean equals(Color color);
}
