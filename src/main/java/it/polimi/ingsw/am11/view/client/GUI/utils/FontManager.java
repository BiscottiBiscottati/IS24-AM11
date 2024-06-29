package it.polimi.ingsw.am11.view.client.GUI.utils;

import javafx.scene.text.Font;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for managing fonts used in the GUI. It provides a method to get a font
 * of a specific size and caches the fonts to improve performance. The fonts are stored in a map,
 * where the key is a FontsEnum value representing the font, and the value is another map. The inner
 * map's key is the size of the font, and the value is the Font object.
 */
public class FontManager {
    private static final Map<FontsEnum, Map<Integer, Font>> fontCache =
            new EnumMap<>(FontsEnum.class);

    /**
     * This method is used to get a Font object of a specific size for a given font type. It first
     * checks if the font is already cached. If not, it loads and caches the font. It then checks if
     * the font of the specific size is cached. If not, it loads and caches the font of the specific
     * size. Finally, it returns the cached font of the specific size.
     *
     * @param font The type of the font as a FontsEnum value.
     * @param size The size of the font.
     * @return The Font object of the specific size for the given font type.
     */
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
