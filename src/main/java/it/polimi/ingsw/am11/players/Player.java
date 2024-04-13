package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.players.field.PlayerField;
import org.jetbrains.annotations.TestOnly;

public record Player(String nickname,
                     PlayerColor color,
                     PersonalSpace space,
                     PlayerField field) {
    @TestOnly
    public Player(String nickname, PlayerColor color) {
        this(nickname, color, new PersonalSpace(3, 2), new PlayerField());
    }

    public Player(String nickname, PlayerColor color, PersonalSpace space) {
        this(nickname, color, space, new PlayerField());
    }
}
