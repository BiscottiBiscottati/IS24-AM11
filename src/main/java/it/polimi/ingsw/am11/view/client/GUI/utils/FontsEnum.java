package it.polimi.ingsw.am11.view.client.GUI.utils;

public enum FontsEnum {
    CLOISTER_BLACK(GuiResEnum.CLOISTER_BLACK);

    private final GuiResEnum font;

    FontsEnum(GuiResEnum font) {
        this.font = font;
    }

    public String getFileName() {
        return font.getFileName();
    }

    public GuiResEnum getFontRes() {
        return font;
    }

}
