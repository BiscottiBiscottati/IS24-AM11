package it.polimi.ingsw.am11.view.client.GUI.utils;

import javafx.scene.text.Font;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<FontsEnum, Map<Integer, Font>> fontCache =
            new EnumMap<>(FontsEnum.class);


    public static Font getFont(FontsEnum font, int size) {
        // Check if the font is already cached
        if (! fontCache.containsKey(font)) {
            fontCache.put(font, new HashMap<>(2));
        }
        if (! fontCache.get(font).containsKey(size)) {
            // Load and cache the font if not already cached
            Font newFont = Font.loadFont(GuiResources.getUrlString(font.getFontRes()),
                                         size);
            fontCache.get(font).put(size, newFont);
        }

        // Return the cached font
        return fontCache.get(font).get(size);
    }
}
