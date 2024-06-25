package it.polimi.ingsw.am11.model.players.utils;

import com.google.common.collect.Maps;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.helpers.EnumMapUtils;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.utils.memento.CardContainerMemento;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private final @NotNull FieldCard card;
    private final @NotNull EnumMap<Corner, Boolean> coveredCorners;
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
        coveredCorners = EnumMapUtils.init(Corner.class, false);
        isRetro = false;
    }

    public CardContainer(@NotNull FieldCard card, boolean isRetro) {
        this.card = card;
        coveredCorners = EnumMapUtils.init(Corner.class, false);
        this.isRetro = isRetro;
    }

    public CardContainer(@NotNull FieldCard card, @NotNull Map<Corner, Boolean> coveredCorners,
                         boolean isRetro) {
        this.card = card;
        this.coveredCorners = new EnumMap<>(coveredCorners);
        this.isRetro = isRetro;
    }

    public static @NotNull CardContainer load(@NotNull CardContainerMemento memento) {
        FieldCard card = CardDecoder.decodeFieldCard(memento.card()).orElseThrow();
        return new CardContainer(card, memento.coveredCorners(), memento.isRetro());
    }


    /**
     * Factory method to create a new CardContainer instance.
     * <p>
     * This method takes a {@link FieldCard} object and a boolean value as parameters, and returns a
     * new CardContainer instance with the specified card and the specified boolean value indicating
     * whether the card is retro.
     *
     * @param card    The FieldCard to be stored in the CardContainer.
     * @param isRetro The boolean value indicating whether the card is retro.
     * @return a new CardContainer instance with the provided FieldCard and the boolean value.
     */
    @Contract("_, _ -> new")
    public static @NotNull CardContainer of(@NotNull FieldCard card, boolean isRetro) {
        return new CardContainer(card, isRetro);
    }

    /**
     * Returns the <code>FieldCard</code> object stored in this <code>CardContainer</code>.
     *
     * @return The <code>FieldCard</code> object stored in this <code>CardContainer</code>.
     */
    public @NotNull FieldCard getCard() {
        return card;
    }

    /**
     * This method returns an immutable <code>EnumMap</code> that maps each {@link Corner} of the
     * card to a Boolean value indicating whether that corner is covered. The returned map is a
     * snapshot of the current state and will not reflect any future changes to the covered
     * corners.
     *
     * @return An immutable <code>Map</code> of the covered corners of the card.
     */
    public @NotNull Map<Corner, Boolean> getCoveredCorners() {
        return Maps.immutableEnumMap(this.coveredCorners);
    }

    /**
     * Checks if a specified corner of the card is covered.
     *
     * @param corner The corner of the card to check.
     * @return true if the specified corner is covered, false otherwise.
     */
    public boolean isCornerCovered(@NotNull Corner corner) {
        return this.coveredCorners.get(corner);
    }

    /**
     * Covers a specified corner of the card and returns the item on that corner.
     * <p>
     * This method takes a {@link Corner} object as a parameter, sets the corresponding
     * <code>Corner</code> as covered,
     * and returns the item on the specified corner of the card. If the card is retro, the item on
     * the retro side of the card is returned.
     * <p>
     * If there is no item on the specified corner, this method returns an empty
     * <code>Optional</code>.
     *
     * @param corner The corner of the card to cover.
     * @return An <code>Optional</code> containing the item on the specified corner of the card, or
     * an empty
     * <code>Optional</code> if there is no item on that corner.
     */
    public Optional<Item> cover(@NotNull Corner corner) {
        this.coveredCorners.put(corner, true);
        return this.card.getItemCorner(corner, isRetro).getItem();
    }

    /**
     * Checks if the color of the card in the container equals the specified color.
     * <p>
     * This method takes a {@link Color} object as a parameter and compares it with the color of the
     * card in the container. If the specified color is not null and equals the color of the card,
     * this method returns true; otherwise, it returns false.
     *
     * @param color The color to compare with the color of the card.
     * @return true if the specified color is not null and equals the color of the card, false
     * otherwise.
     */
    public boolean isColorEquals(@Nullable Color color) {
        if (color != null) return this.card.isColorEqual(color);
        return false;
    }

    /**
     * Checks if the card in the container equals the specified card.
     * <p>
     * This method takes a {@link FieldCard} object as a parameter and compares it with the card in
     * the container. If the specified card is not null and equals the card in the container, this
     * method returns true; otherwise, it returns false.
     *
     * @param card The card to compare with the card in the container.
     * @return true if the specified card is not null and equals the card in the container, false
     * otherwise.
     */
    public boolean isCardEquals(@Nullable FieldCard card) {
        if (card != null) return this.card.equals(card);
        return false;
    }

    /**
     * Checks if the card in the container is retro.
     * <p>
     * This method returns the boolean value indicating whether the card in the container is retro.
     *
     * @return true if the card in the container is retro, false otherwise.
     */
    public boolean isRetro() {
        return isRetro;
    }

    /**
     * Returns the item on the specified corner of the card.
     * <p>
     * This method takes a {@link Corner} object as a parameter and returns the item on the
     * specified corner of the card. If the card is retro, the item on the retro side of the card is
     * returned.
     * <p>
     * If there is no item on the specified corner, this method returns an empty {@link Optional}.
     *
     * @param corner The corner of the card to get the item from.
     * @return An {@link Optional} containing the item on the specified corner of the card, or an
     * empty {@link Optional} if there is no item on that corner.
     */
    public Optional<Item> getItemOn(@NotNull Corner corner) {
        return card.getItemCorner(corner, isRetro).getItem();
    }

    public CornerContainer getContainerOn(@NotNull Corner corner) {
        return card.getItemCorner(corner, isRetro);
    }


    public @NotNull CardContainerMemento save() {
        return new CardContainerMemento(card.getId(), Map.copyOf(coveredCorners), isRetro);
    }

    @Override
    public String toString() {
        return "ID: " + card.getId() + " isRtr: " + isRetro +
               " covered TL: " + coveredCorners.get(Corner.TOP_LX) +
               " TR: " + coveredCorners.get(Corner.TOP_RX) +
               " BL: " + coveredCorners.get(Corner.DOWN_LX) +
               " BR: " + coveredCorners.get(Corner.DOWN_RX);
    }
}
