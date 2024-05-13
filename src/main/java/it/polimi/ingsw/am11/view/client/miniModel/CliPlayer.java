package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;

public class CliPlayer {

    private final String nickname;
    private final PlayerColor color;
    private CliSpace space;
    private CliField field;
    private int points;


    public CliPlayer(String nickname, PlayerColor color) {
        this.nickname = nickname;
        this.color = color;
        this.space = new CliSpace();
        this.field = new CliField();
        points = 0;
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public CliSpace getSpace() {
        return space;
    }

    public void setSpace(CliSpace space) {
        this.space = space;
    }

    public CliField getField() {
        return field;
    }

    public void setField(CliField field) {
        this.field = field;
    }

    public void addPoints(int i) {
        points += i;
    }
}
