package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CliField {
    private final @NotNull Map<Position, MiniCardContainer> cardsPositioned;

    public CliField() {
        cardsPositioned = new HashMap<>();
    }

    public void place(Position pos, int cardId, boolean isRetro) {
        cardsPositioned.put(pos, new MiniCardContainer(cardId, isRetro));
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
