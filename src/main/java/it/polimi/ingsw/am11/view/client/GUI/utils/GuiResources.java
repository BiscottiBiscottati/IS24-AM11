package it.polimi.ingsw.am11.view.client.GUI.utils;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.windows.LoadingScreen;
import it.polimi.ingsw.am11.view.client.miniModel.utils.CardInfo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * The GuiResources class is a utility class that manages the graphical resources of the
 * application. It contains methods to retrieve images and image views for various elements of the
 * GUI. It also maintains a map that links resource URLs to an enum for easy retrieval.
 */
public class GuiResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiResources.class);

    //Contains an enum map that links resources URLs to an enum
    //It has also the methods to get the resources like Images, ImageViews ...
    private static final EnumMap<GuiResEnum, URL> urlMap = new EnumMap<>(GuiResEnum.class);
    private static final String RETRO_CARD_PATH = "windows/cards/retro/%s_%s.png";
    private static final String FRONT_CARD_PATH = "windows/cards/front/%d.png";

    /**
     * This method is used to get the top card of the deck as an image. It formats the
     * RETRO_CARD_PATH with the column name of the color and the card type to get the path of the
     * image. Then, it retrieves the image from the path.
     *
     * @param type  The type of the playable card.
     * @param color The color of the playable card.
     * @return The image of the top card of the deck. If the image cannot be loaded, it returns
     * null.
     */
    public static @Nullable Image getTopDeck(@NotNull PlayableCardType type,
                                             @NotNull GameColor color) {
        return getImageOf(String.format(RETRO_CARD_PATH,
                                        color.getColumnName(),
                                        type.getColumnName()));
    }

    /**
     * This method is used to retrieve an image from a given path. It attempts to load the image
     * from the provided path and returns the image if successful. If the image cannot be loaded, it
     * logs an error and returns null.
     *
     * @param completePath The complete path to the image resource.
     * @return The loaded image, or null if the image cannot be loaded.
     */
    private static @Nullable Image getImageOf(@NotNull String completePath) {
        String urlString = "";
        try {
            URL url = CodexNaturalis.class.getResource(completePath);
            urlString = String.valueOf(url);
            LOGGER.trace(urlString);
            return new Image(urlString);
        } catch (Exception e) {
            LOGGER.error("Error loading card image at Url: {}", urlString);
            return null;
        }
    }

    /**
     * This method is used to get an ImageView of the resource specified by the provided enum. It
     * retrieves the URL of the resource from the enum, loads the image from the URL, and then
     * creates an ImageView with the loaded image.
     *
     * @param res The enum value that specifies the resource.
     * @return An ImageView of the resource specified by the provided enum.
     */
    public static @NotNull ImageView getTheImageView(GuiResEnum res) {
        Image image = new Image(String.valueOf(GuiResources.getTheUrl(res)));
        return new ImageView(image);
    }

    /**
     * This method is used to get the URL associated with the provided enum value. It checks if the
     * URL is already present in the map. If not, it adds the URL to the map. The URL is retrieved
     * from the resource file specified by the enum value.
     *
     * @param name The enum value that specifies the resource.
     * @return The URL associated with the provided enum value.
     */
    public static URL getTheUrl(GuiResEnum name) {
        if (urlMap.get(name) == null) {
            urlMap.put(name, LoadingScreen.class.getResource(name.getFileName()));
        }
        return urlMap.get(name);
    }

    /**
     * This method is used to get the image associated with the provided enum value. It retrieves
     * the URL associated with the enum value and loads the image from the URL.
     *
     * @param res The enum value that specifies the resource.
     * @return The image associated with the provided enum value.
     */
    @Contract("_ -> new")
    public static @NotNull Image getTheImage(GuiResEnum res) {
        return new Image(String.valueOf(GuiResources.getTheUrl(res)));
    }

    /**
     * This method is used to get the URL associated with the provided enum value as a String. It
     * retrieves the URL associated with the enum value and converts it to a String.
     *
     * @param name The enum value that specifies the resource.
     * @return The URL associated with the provided enum value as a String.
     */
    public static String getUrlString(GuiResEnum name) {
        return String.valueOf(getTheUrl(name));
    }

    /**
     * This method is used to get the image associated with the provided card ID. It formats the
     * FRONT_CARD_PATH with the card ID to get the path of the image. Then, it retrieves the image
     * from the path.
     *
     * @param cardId The ID of the card.
     * @return The image of the card. If the image cannot be loaded, it returns null.
     */
    public static @Nullable Image getCardImage(int cardId) {
        return getImageOf(String.format(FRONT_CARD_PATH, cardId)
        );
    }

    /**
     * This method is used to get an ImageView of the card specified by the provided card ID. It
     * formats the FRONT_CARD_PATH with the card ID to get the path of the image. Then, it retrieves
     * the image from the path and creates an ImageView with the loaded image.
     *
     * @param cardId The ID of the card.
     * @return An ImageView of the card. If the image cannot be loaded, it returns an ImageView with
     * a null image.
     */
    public static @NotNull ImageView getImageView(int cardId) {
        return new ImageView(getImageOf(String.format(FRONT_CARD_PATH, cardId)));
    }

    /**
     * This method is used to get an ImageView of the card specified by the provided card ID. It
     * checks if the card ID is in the list of invalid IDs. If it is, it retrieves the image from a
     * specific path. If the card ID is not in the list of invalid IDs, it retrieves the card type
     * and color associated with the card ID, and retrieves the image from the path formatted with
     * the card type and color. Then, it creates an ImageView with the loaded image.
     *
     * @param cardId The ID of the card.
     * @return An ImageView of the card. If the image cannot be loaded, it throws a
     * RuntimeException.
     */
    public static @NotNull ImageView getCardImageRetro(int cardId) {
        List<Integer> invalidIds = Arrays.asList(97, 98, 99, 100, 101, 102);
        if (invalidIds.contains(cardId)) {
            return new ImageView(getImageOf("windows/cards/retro/" + cardId +
                                            "_Retro.png"));
        } else {
            PlayableCardType typeName;
            GameColor colorName;
            try {
                typeName = CardInfo.getPlayableCardType(cardId);
                colorName = CardInfo.getPlayabelCardColor(cardId);
            } catch (IllegalCardBuildException e) {
                throw new RuntimeException(e);
            }
            return new ImageView(getImageOf(String.format(RETRO_CARD_PATH,
                                                          colorName.getColumnName(),
                                                          typeName.getColumnName())));
        }
    }

    /**
     * This method is used to get the retro image associated with the provided card type and color.
     * It formats the RETRO_CARD_PATH with the column name of the color and the card type to get the
     * path of the image. Then, it retrieves the image from the path.
     *
     * @param typeName  The type of the playable card.
     * @param colorName The color of the playable card.
     * @return The retro image of the card. If the image cannot be loaded, it returns null.
     */
    public static @Nullable Image getRetro(@NotNull PlayableCardType typeName,
                                           @NotNull GameColor colorName) {
        return getImageOf(String.format(RETRO_CARD_PATH,
                                        colorName.getColumnName(),
                                        typeName.getColumnName()));
    }

}
