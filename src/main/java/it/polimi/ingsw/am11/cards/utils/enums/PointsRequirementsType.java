package it.polimi.ingsw.am11.cards.utils.enums;

/**
 * Ways to score points if placed.
 * <p>
 * Can be <code>CLASSIC</code>, <code>COVERING_CORNERS</code> or <code>SYMBOLS</code>.
 * <p>
 * <code>CLASSIC</code> means that the points are given as you place the card.
 * <p>
 * <code>COVERING_CORNERS</code> means that the points depend on how many corners the card covers.
 * <p>
 * <code>SYMBOLS</code> means that the points are given based on the symbols on the field and the card.
 * <p>
 * <code>ResourceCard</code> always have <code>CLASSIC</code> as a point requirements.
 */
public enum PointsRequirementsType {
    CLASSIC,
    COVERING_CORNERS,
    SYMBOLS

}
