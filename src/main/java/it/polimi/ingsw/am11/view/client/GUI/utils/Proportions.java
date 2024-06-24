package it.polimi.ingsw.am11.view.client.GUI.utils;

import javafx.stage.Screen;

public enum Proportions {
    SQUARE_SIZE((int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                Screen.getPrimary().getBounds().getWidth()) * 0.7)),
    HALF_BUTTON_SIZE(Proportions.SQUARE_SIZE.value / 48),
    DISTANCE_TO_BORDER(Proportions.HALF_BUTTON_SIZE.value >> 2);

    private final int value;

    Proportions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
