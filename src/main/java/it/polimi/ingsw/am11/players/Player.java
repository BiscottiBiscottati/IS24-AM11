package it.polimi.ingsw.am11.players;

import it.polimi.ingsw.am11.players.field.PlayerField;

public record Player(String nickname,
                     PlayerColor color,
                     PersonalSpace space,
                     PlayerField field) {
}
