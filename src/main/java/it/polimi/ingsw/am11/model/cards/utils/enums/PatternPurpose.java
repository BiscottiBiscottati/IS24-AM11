package it.polimi.ingsw.am11.model.cards.utils.enums;

/**
 * Represents the purpose of a pattern when doing pattern matching.
 * <p>
 * <code>NEXT_CHECK</code> and <code>PREVIOUS_CHECK</code> are used to traverse
 * the <code>PlayerField</code> in a specific direction looking for a pattern.
 * <p>
 * <code>TO_COMPLETE</code> is used to check if a pattern is complete.
 * <p>
 * <code>ADJACENT_LX</code> and <code>ADJACENT_RX</code> are used to traverse adjacent cells
 * without pattern matching.
 */
public enum PatternPurpose {
    ADJACENT_LX,
    ADJACENT_RX,
    NEXT_CHECK,
    PREVIOUS_CHECK,
    TO_COMPLETE
}