package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CliField {
    private final @NotNull Map<Position, MiniCardContainer> cardsPositioned;

    public CliField() {
        cardsPositioned = new HashMap<>(20);
    }

    public void place(Position pos, int cardId, boolean isRetro) {

        MiniCardContainer cardContainer = new MiniCardContainer(cardId, isRetro);
        Stream.of(Corner.values())
              .filter(corner -> cardsPositioned.containsKey(PositionManager.getPositionIn(pos,
                                                                                          corner)))
              .forEach(cardContainer::cover);

        cardsPositioned.put(pos, cardContainer);
    }

    public void remove(Position pos) {
        cardsPositioned.remove(pos);
    }

    @Override
    public String toString() {
        String result;
        result = "";
        for (MiniCardContainer miniCardContainer : cardsPositioned.values()) {
            result = result + "\n" + miniCardContainer.toString();
        }
        return result;
    }

}
