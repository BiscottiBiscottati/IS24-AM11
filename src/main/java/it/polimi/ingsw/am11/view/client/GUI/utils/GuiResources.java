package it.polimi.ingsw.am11.view.client.GUI.utils;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.windows.LoadingScreen;
import it.polimi.ingsw.am11.view.client.miniModel.utils.CardInfo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class GuiResources {

    //Contains an enum map that links resources URLs to an enum
    //It has also the methods to get the resources like Images, ImageViews ...
    private static final EnumMap<GuiResEnum, URL> urlMap = new EnumMap<>(GuiResEnum.class);

    public static @Nullable Image getTopDeck(PlayableCardType type, Color color) {
        String urlString = "";
        String typeName = "";
        String colorName = "";

        if (type == PlayableCardType.GOLD) {
            typeName = "gold";
        } else if (type == PlayableCardType.RESOURCE) {
            typeName = "res";
        }

        if (color == Color.RED) {
            colorName = "red";
        } else if (color == Color.GREEN) {
            colorName = "green";
        } else if (color == Color.BLUE) {
            colorName = "blu";
        } else if (color == Color.PURPLE) {
            colorName = "purple";
        }
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/retro/" + colorName + "_" +
                                                       typeName + ".png");
            urlString = String.valueOf(url);
            System.out.println(urlString);
            return new Image(urlString);
        } catch (Exception e) {
            System.err.println("Error loading retro card image at Url: " + urlString);
            return null;
        }
    }

    public static ImageView getTheImageView(GuiResEnum res) {
        Image image = new Image(String.valueOf(GuiResources.getTheUrl(res)));
        return new ImageView(image);
    }

    public static URL getTheUrl(GuiResEnum name) {
        if (urlMap.get(name) == null) {
            urlMap.put(name, LoadingScreen.class.getResource(name.getFileName()));
        }
        return urlMap.get(name);
    }

    public static Image getTheImage(GuiResEnum res) {
        return new Image(String.valueOf(GuiResources.getTheUrl(res)));
    }

    public static String getUrlString(GuiResEnum name) {
        return String.valueOf(getTheUrl(name));
    }

    public static @Nullable Image getCardImage(int cardId) {
        String urlString = "";
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/front/" + cardId + ".png");
            urlString = String.valueOf(url);
            System.out.println(urlString);
            return new Image(urlString);
        } catch (Exception e) {
            System.err.println("Error loading front card image at Url: " + urlString);
            return null;
        }
    }

    public static @Nullable ImageView getImageView(int cardId) {
        String urlString = "";
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/front/" + cardId + ".png");
            urlString = String.valueOf(url);
            System.out.println(urlString);
            Image image = new Image(urlString);
            return new ImageView(image);
        } catch (Exception e) {
            System.err.println("Error loading front card image at Url: " + urlString);
            return null;
        }
    }

    public static @Nullable ImageView getCardImageRetro(int cardId) {
        List<Integer> invalidIds = Arrays.asList(97, 98, 99, 100, 101, 102);
        if (invalidIds.contains(cardId)) {
            String urlString = "";
            try {
                URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                           "/windows/cards/retro/" + cardId + "_Retro" +
                                                           ".png");
                urlString = String.valueOf(url);
                System.out.println(urlString);
                Image image = new Image(urlString);
                return new ImageView(image);
            } catch (Exception e) {
                System.err.println("Error loading retro card image at Url: " + urlString);
                return null;
            }
        } else {
            PlayableCardType typeName;
            Color colorName;
            try {
                typeName = CardInfo.getPlayableCardType(cardId);
                colorName = CardInfo.getPlayabelCardColor(cardId);
            } catch (IllegalCardBuildException e) {
                throw new RuntimeException(e);
            }
            String urlString = "";
            String type = "";
            String color = "";

            if (typeName == PlayableCardType.GOLD) {
                System.out.println("typeName: gold");
                type = "gold";
            } else if (typeName == PlayableCardType.RESOURCE) {
                System.out.println("typeName: res");
                type = "res";
            }

            if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.BLUE) {
                System.out.println("colorName: blue");
                color = "blu";
            } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.GREEN) {
                System.out.println("colorName: green");
                color = "green";
            } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.PURPLE) {
                System.out.println("colorName: purple");
                color = "purple";
            } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.RED) {
                System.out.println("colorName: red");
                color = "red";
            }
            try {
                URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                           "/windows/cards/retro/" + color + "_" +
                                                           type + ".png");
                urlString = String.valueOf(url);
                Image image = new Image(urlString);
                return new ImageView(image);
            } catch (Exception e) {
                System.err.println("Error loading retro card image at Url: " + urlString);
                return null;
            }
        }
    }

    public static @Nullable Image getRetro(PlayableCardType typeName, Color colorName) {
        String urlString = "";
        String type = "";
        String color = "";

        if (typeName == PlayableCardType.GOLD) {
            System.out.println("typeName: gold");
            type = "gold";
        } else if (typeName == PlayableCardType.RESOURCE) {
            System.out.println("typeName: res");
            type = "res";
        }

        if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.BLUE) {
            System.out.println("colorName: blue");
            color = "blu";
        } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.GREEN) {
            System.out.println("colorName: green");
            color = "green";
        } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.PURPLE) {
            System.out.println("colorName: purple");
            color = "purple";
        } else if (colorName == it.polimi.ingsw.am11.model.cards.utils.enums.Color.RED) {
            System.out.println("colorName: red");
            color = "red";
        }
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/retro/" + color + "_" +
                                                       type + ".png");
            System.out.println(url);
            urlString = String.valueOf(url);
            System.out.println(urlString);
            return new Image(urlString);
        } catch (Exception e) {
            System.err.println("Error loading retro card image at Url: " + urlString);
            return null;
        }
    }

}
