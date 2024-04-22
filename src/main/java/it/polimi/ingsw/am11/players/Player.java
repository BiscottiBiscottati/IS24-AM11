package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.players.field.PlayerField;
import it.polimi.ingsw.am11.players.utils.PlayerColor;

public record Player(String nickname,
                     PlayerColor color,
                     PersonalSpace space,
                     PlayerField field) {

    public Player(String nickname, PlayerColor color) {
        this(nickname, color, new PersonalSpace(), new PlayerField());
    }

    public Player(String nickname, PlayerColor color, PersonalSpace space) {
        this(nickname, color, space, new PlayerField());
    }
}
