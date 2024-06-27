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

public class GuiResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiResources.class);

    //Contains an enum map that links resources URLs to an enum
    //It has also the methods to get the resources like Images, ImageViews ...
    private static final EnumMap<GuiResEnum, URL> urlMap = new EnumMap<>(GuiResEnum.class);
    private static final String RETRO_CARD_PATH = "windows/cards/retro/%s_%s.png";
    private static final String FRONT_CARD_PATH = "windows/cards/front/%d.png";

    public static @Nullable Image getTopDeck(@NotNull PlayableCardType type,
                                             @NotNull GameColor color) {
        return getImageOf(String.format(RETRO_CARD_PATH,
                                        color.getColumnName(),
                                        type.getColumnName()));
    }

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

    public static @NotNull ImageView getTheImageView(GuiResEnum res) {
        Image image = new Image(String.valueOf(GuiResources.getTheUrl(res)));
        return new ImageView(image);
    }

    public static URL getTheUrl(GuiResEnum name) {
        if (urlMap.get(name) == null) {
            urlMap.put(name, LoadingScreen.class.getResource(name.getFileName()));
        }
        return urlMap.get(name);
    }

    @Contract("_ -> new")
    public static @NotNull Image getTheImage(GuiResEnum res) {
        return new Image(String.valueOf(GuiResources.getTheUrl(res)));
    }

    public static String getUrlString(GuiResEnum name) {
        return String.valueOf(getTheUrl(name));
    }

    public static @Nullable Image getCardImage(int cardId) {
        return getImageOf(String.format(FRONT_CARD_PATH, cardId)
        );
    }

    public static @NotNull ImageView getImageView(int cardId) {
        return new ImageView(getImageOf(String.format(FRONT_CARD_PATH, cardId)));
    }

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

    public static @Nullable Image getRetro(@NotNull PlayableCardType typeName,
                                           @NotNull GameColor colorName) {
        return getImageOf(String.format(RETRO_CARD_PATH,
                                        colorName.getColumnName(),
                                        typeName.getColumnName()));
    }

}
