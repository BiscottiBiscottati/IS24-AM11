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
    CLOISTER_BLACK("CloisterBlack.ttf");


    private final String fileName;

    GuiResEnum(String fileName) {
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }
}
