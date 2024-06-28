package it.polimi.ingsw.am11.view.client.GUI.utils;

/**
 * This enum is used to map different fonts used in the GUI to their file paths. Each enum value
 * represents a different font, and the associated GuiResEnum value is the file path of the font.
 * The enum provides methods to get the file name associated with a font and to get the GuiResEnum
 * value associated with a font.
 */
public enum FontsEnum {
    CLOISTER_BLACK(GuiResEnum.CLOISTER_BLACK),
    VINQUE(GuiResEnum.VINQUE);

    private final GuiResEnum font;

    /**
     * Constructor for the FontsEnum. It initializes the enum value with the provided GuiResEnum
     * value, which represents the file path of the font.
     *
     * @param font The GuiResEnum value associated with the font.
     */
    FontsEnum(GuiResEnum font) {
        this.font = font;
    }

    /**
     * This method is used to get the GuiResEnum value associated with the font. It returns the
     * GuiResEnum value that was set during the initialization of the enum value.
     *
     * @return The GuiResEnum value associated with the font.
     */
    public GuiResEnum getFontRes() {
        return font;
    }

}
