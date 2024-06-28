package it.polimi.ingsw.am11.view.client.GUI.utils;

import javafx.stage.Screen;

/**
 * The Proportions enum is used to define various proportions used in the GUI. It includes
 * proportions like SQUARE_SIZE, HALF_BUTTON_SIZE, DISTANCE_TO_BORDER and RECTANGLE_SIZE. Each enum
 * value is associated with an integer value that represents the actual proportion. The proportions
 * are calculated based on the screen size.
 */
public enum Proportions {
    SQUARE_SIZE((int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                Screen.getPrimary().getBounds().getWidth()) * 0.7)),
    HALF_BUTTON_SIZE(Proportions.SQUARE_SIZE.value / 48),
    DISTANCE_TO_BORDER(Proportions.HALF_BUTTON_SIZE.value >> 2),
    RECTANGLE_SIZE((int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                   Screen.getPrimary().getBounds().getWidth()) * 0.6));

    private final int value;


    /**
     * Constructor for the Proportions enum. It initializes the enum value with the provided integer
     * value.
     *
     * @param value The integer value to be associated with the enum value.
     */
    Proportions(int value) {
        this.value = value;
    }

    /**
     * This method is used to get the integer value associated with the enum value. It returns the
     * integer value that was set during the initialization of the enum value.
     *
     * @return The integer value associated with the enum value.
     */
    public int getValue() {
        return value;
    }
}
