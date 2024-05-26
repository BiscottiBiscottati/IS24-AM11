package it.polimi.ingsw.am11.view.client.GUI.utils;

public enum GuiResEnum {
    ICON("Icon.png"),
    LGIN_BACKGROUND("LDBackGround.png"),
    LGIN_SQUARE("LDSquare.png"),
    LGIN_WRITINGS("LDWritings.png"),
    LGIN_DISK("LDDisks.png"),
    WOLF_ICON("Wolf.png"),
    BUTTERLFY_ICON("Butterfly.png"),
    MUSHROOM_ICON("Mushroom.png"),
    LEAF_ICON("Leaf.png"),
    CLOSE_CROSS("close.png"),
    MINIMIZE_BAR("minus.png"),
    CLOISTER_BLACK("CloisterBlack.ttf"),
    BLU_GLD_RTR("blu_gold.png"),
    BLU_RES_RTR("blu_res.png"),
    GREEN_RES_RTR("green_res.png"),
    RED_RES_RTR("red_res.png"),
    PURPLE_RES_RTR("purple_res.png"),
    GREEN_GLD_RTR("green_gold.png"),
    PURPLE_GLD_RTR("purple_gold.png"),
    RED_GLD_RTR("red_gold.png"),
    OBJ_CARD("objective.png");


    private final String fileName;

    GuiResEnum(String fileName) {
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }
}
