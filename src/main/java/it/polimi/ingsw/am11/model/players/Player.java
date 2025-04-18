package it.polimi.ingsw.am11.model.players;

import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.memento.PlayerMemento;
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
    public Player(@NotNull String nickname, @NotNull PlayerColor color) {
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
    public Player(@NotNull String nickname, @NotNull PlayerColor color,
                  @NotNull PersonalSpace space) {
        this(nickname, color, space, new PlayerField());
    }

    /**
     * This method is used to reload an old status of player from a memento.
     *
     * @param memento the memento to reload
     * @return the player with the status reloaded from the memento
     */
    public static @NotNull Player load(@NotNull PlayerMemento memento) {
        PersonalSpace space = new PersonalSpace();
        space.load(memento.space());
        PlayerField field = new PlayerField();
        field.load(memento.field());
        return new Player(memento.nickname(), memento.color(), space, field);
    }

    /**
     * This method creates a new <code>PlayerMemento</code> with the complete current state of the
     * player.
     *
     * @return the memento with the current state of the player
     */
    public @NotNull PlayerMemento save() {
        return new PlayerMemento(nickname, color, space.save(), field.save());
    }

    /**
     * This method creates a new <code>PlayerMemento</code> with the state of the player that can be
     * shared with other players.
     *
     * @return the memento with the public state of the player
     */
    public @NotNull PlayerMemento savePublic() {
        return new PlayerMemento(nickname, color, new PersonalSpace().save(), field.save());
    }
}
