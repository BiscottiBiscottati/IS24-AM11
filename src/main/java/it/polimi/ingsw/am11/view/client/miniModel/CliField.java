package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.FieldCard;
import it.polimi.ingsw.am11.model.decks.utils.CardDecoder;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.memento.PositionManagerMemento;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class CliField {
    private final @NotNull PositionManager positionManager;

    public CliField() {
        positionManager = new PositionManager();
    }

    public void loadField(@NotNull PositionManagerMemento memento) {
        positionManager.load(memento);
    }

    public void place(@NotNull Position pos, int cardId, boolean isRetro) {

        FieldCard card = CardDecoder.decodeFieldCard(cardId).orElseThrow();

        try {
            positionManager.placeCard(card, pos, isRetro);
        } catch (IllegalCardPlacingException e) {
            throw new RuntimeException(e);
            //TODO maybe we can ask for the server memento to
            // rsync with the server
        }
    }

    public @NotNull Set<Position> getAvailablePositions() {
        return positionManager.getAvailablePositions();
    }

    public Map<Position, CardContainer> getCardsPositioned() {
        return positionManager.getCardsPositioned();
    }

    @Override
    public String toString() {
        StringBuilder result;
        result = new StringBuilder(16);
        for (CardContainer cardContainer : positionManager.getCardsPositioned().values()) {
            result.append("\n").append(cardContainer.toString());
        }
        return result.toString();
    }

}
