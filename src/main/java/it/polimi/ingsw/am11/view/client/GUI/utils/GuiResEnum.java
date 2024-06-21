package it.polimi.ingsw.am11.view.client.GUI.utils;

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
    BLU_GLD_RTR("elements/blu_gold.png"),
    BLU_RES_RTR("elements/blu_res.png"),
    GREEN_RES_RTR("elements/green_res.png"),
    RED_RES_RTR("elements/red_res.png"),
    PURPLE_RES_RTR("elements/purple_res.png"),
    GREEN_GLD_RTR("elements/green_gold.png"),
    PURPLE_GLD_RTR("elements/purple_gold.png"),
    RED_GLD_RTR("elements/red_gold.png"),
    GAME_BACKGROUND("elements/gamePageBG.png"),
    OBJ_CARD("elements/objective.png");

    private final String fileName;

    GuiResEnum(String fileName) {
        this.fileName = fileName;
    }

    public static GuiResEnum getEnumByCardId(int cardId) {
        String fileName = cardId + ".png";
        for (GuiResEnum value : GuiResEnum.values()) {
            if (value.fileName.equals(fileName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid cardId: " + cardId);
    }

    public static GuiResEnum getEnumByCardIdRetro(int cardId) {
        String fileName = cardId + "_Retro.png";
        for (GuiResEnum value : GuiResEnum.values()) {
            if (value.fileName.equals(fileName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid cardId: " + cardId);
    }

    public String getFileName() {
        return fileName;
    }
}
