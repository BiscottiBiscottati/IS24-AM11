package it.polimi.ingsw.am11.players.utils;

import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.cards.utils.FieldCard;
import it.polimi.ingsw.am11.cards.utils.Item;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.helpers.EnumMapUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * The <code>CardContainer</code> class represents a container for a {@link FieldCard} in the game.
 * <p>
 * This class is used to store a <code>FieldCard</code> and keep track of which corners of the card
 * are covered. It provides methods to get the card, get the covered corners, cover a corner, check
 * if a corner is covered, and check if the card's color equals a specified color.
 * <p>
 * If a corner is covered, the corresponding Boolean value in the EnumMap is true; otherwise, it is
 * false.
 * <p>
 * The <code>CardContainer</code> class provides a static factory method
 * <code>of</code> for creating a new <code>CardContainer</code> object.
 * This method takes a <code>FieldCard</code> object as a parameter and returns a new
 * <code>CardContainer</code> object with the specified card and all corners uncovered.
 * <p>
 * The <code>CardContainer</code> class also provides methods to get the card, get the covered
 * corners, cover a corner, check if a corner is covered, and check if the card's color equals a
 * specified color. These methods are used to interact with the card and the covered corners in the
 * container.
 */
public class CardContainer {

    private final FieldCard card;
    private final EnumMap<Corner, Boolean> coveredCorners;
    private final boolean isRetro;

    /**
     * Constructs a new instance of <code>CardContainer</code> with the specified
     * {@link FieldCard}.
     * <p>
     * This constructor takes a <code>FieldCard</code> object as a parameter and initializes the
     * card field with the specified card. It also initializes all corners as uncovered.
     *
     * @param card The <code>FieldCard</code> to be stored in the <code>CardContainer</code>.
     */
    public CardContainer(@NotNull FieldCard card) {
        this.card = card;
        coveredCorners = EnumMapUtils.Init(Corner.class, false);
        isRetro = false;
    }

    public CardContainer(@NotNull FieldCard card, boolean isRetro) {
        this.card = card;
        coveredCorners = EnumMapUtils.Init(Corner.class, false);
        this.isRetro = isRetro;
    }

    // TODO this may be redundant but can be used if preferred to constructor
    @Contract("_, _ -> new")
    public static @NotNull CardContainer of(@NotNull FieldCard card, boolean isRetro) {
        return new CardContainer(card, isRetro);
    }

    /**
     * Returns the <code>FieldCard</code> object stored in this <code>CardContainer</code>.
     *
     * @return The <code>FieldCard</code> object stored in this <code>CardContainer</code>.
     */
    public FieldCard getCard() {
        return card;
    }

    public Map<Corner, Boolean> getCoveredCorners() {
        return Maps.immutableEnumMap(this.coveredCorners);
    }

    public boolean isCornerCovered(@NotNull Corner corner) {
        return this.coveredCorners.get(corner);
    }

    public Optional<Item> cover(@NotNull Corner corner) {
        this.coveredCorners.put(corner, true);
        return this.card.getItemCorner(corner, isRetro).getItem();
    }

    public boolean isColorEquals(Color color) {
        if (color != null) return this.card.isColorEqual(color);
        return false;
    }

    public boolean isCardEquals(FieldCard card) {
        if (card != null) return this.card.equals(card);
        return false;
    }

    public boolean isRetro() {
        return isRetro;
    }

    public Optional<Item> getItemOn(Corner corner) {
        return this.card.getItemCorner(corner, isRetro).getItem();
    }
}
