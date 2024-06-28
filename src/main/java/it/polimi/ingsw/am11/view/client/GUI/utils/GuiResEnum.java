package it.polimi.ingsw.am11.view.client.GUI.utils;

/**
 * This enum is used to map various resources used in the GUI to their file paths. Each enum value
 * represents a different resource, and the associated String is the file path of the resource. The
 * enum provides methods to get an enum value by a card ID, and to get the file path associated with
 * an enum value.
 */
public enum GuiResEnum {
    ICON("elements/Icon.png"),
    LGIN_BACKGROUND("elements/LDBackGround.png"),
    LGIN_SQUARE("elements/LDSquare.png"),
    LGIN_WRITINGS("elements/LDWritings.png"),
    LGIN_DISK("elements/LDDisks.png"),
    WOLF_ICON("elements/Wolf.png"),
    BUTTERLFY_ICON("elements/Butterfly.png"),
    MUSHROOM_ICON("elements/Mushroom.png"),
    LEAF_ICON("elements/Leaf.png"),
    CLOSE_CROSS("elements/close.png"),
    MINIMIZE_BAR("elements/minus.png"),
    CLOISTER_BLACK("elements/CloisterBlack.ttf"),
    VINQUE("elements/vinque_rg.otf"),
    BLU_GLD_RTR("elements/blue_gold.png"),
    BLU_RES_RTR("elements/blue_res.png"),
    GREEN_RES_RTR("elements/green_res.png"),
    RED_RES_RTR("elements/red_res.png"),
    PURPLE_RES_RTR("elements/purple_res.png"),
    GREEN_GLD_RTR("elements/green_gold.png"),
    PURPLE_GLD_RTR("elements/purple_gold.png"),
    RED_GLD_RTR("elements/red_gold.png"),
    GAME_BACKGROUND("elements/gamePageBG.png"),
    OBJ_CARD("elements/objective.png"),
    X_CARD("cards/retro/X.png"),
    BEEP("/beep-02.mp3");

    private final String fileName;

    /**
     * Constructor for the GuiResEnum. It initializes the enum value with the provided file name.
     *
     * @param fileName The file name associated with the enum value.
     */
    GuiResEnum(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This method is used to get the file name associated with the enum value. It returns the file
     * name that was set during the initialization of the enum value.
     *
     * @return The file name associated with the enum value.
     */
    public String getFileName() {
        return fileName;
    }
}
