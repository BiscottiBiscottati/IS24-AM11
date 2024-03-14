package it.polimi.ingsw.am11.Players;

public final class Player {

    private final String nickname;
    private final PlayerColor color;
    private final PersonalSpace space;
    private final PlayerField field;

    public Player(String nickname, PlayerColor color, PersonalSpace space, PlayerField field) {
        this.nickname = nickname;
        this.color = color;
        this.space = space;
        this.field = field;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PersonalSpace getSpace() {
        return space;
    }

    public PlayerField getField() {
        return field;
    }
}
