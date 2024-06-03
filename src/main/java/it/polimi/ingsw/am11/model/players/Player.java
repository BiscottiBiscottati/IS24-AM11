package it.polimi.ingsw.am11.model.players;

import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import org.jetbrains.annotations.NotNull;

/**
 * This is a record class representing a Player in the game. A <code>Player</code> has a nickname, a
 * color, a {@link PersonalSpace}, and a {@link PlayerField}.
 */
public record Player(@NotNull String nickname,
                     @NotNull PlayerColor color,
                     @NotNull PersonalSpace space,
                     @NotNull PlayerField field) {

    /**
     * This is a constructor for the Player record. It creates a new <code>Player</code> with the
     * given nickname and color. The personal space and player field are initialized with new
     * instances.
     *
     * @param nickname The nickname of the player.
     * @param color    The color of the player.
     */
    public Player(String nickname, PlayerColor color) {
        this(nickname, color, new PersonalSpace(), new PlayerField());
    }

    /**
     * This is a constructor for the Player record. It creates a new <code>Player</code> with the
     * given nickname, color, and personal space. The player field is initialized with a new
     * instance.
     *
     * @param nickname The nickname of the player.
     * @param color    The color of the player.
     * @param space    The personal space of the player.
     */
    public Player(String nickname, PlayerColor color, PersonalSpace space) {
        this(nickname, color, space, new PlayerField());
    }
}
