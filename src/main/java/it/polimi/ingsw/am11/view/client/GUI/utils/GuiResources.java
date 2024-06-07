package it.polimi.ingsw.am11.view.client.GUI.utils;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.windows.LoadingScreen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.EnumMap;

public class GuiResources {

    //Contains an enum map that links resources URLs to an enum
    //It has also the methods to get the resources like Images, ImageViews ...
    private final EnumMap<GuiResEnum, URL> urlMap;


    public GuiResources() {
        urlMap = new EnumMap<>(GuiResEnum.class);
    }

    public ImageView getTheImageView(GuiResEnum res) {
        Image image = new Image(String.valueOf(this.getTheUrl(res)));
        return new ImageView(image);
    }

    public URL getTheUrl(GuiResEnum name) {
        if (urlMap.get(name) == null) {
            urlMap.put(name, LoadingScreen.class.getResource(name.getFileName()));
        }
        return urlMap.get(name);
    }

    public Image getTheImage(GuiResEnum res) {
        return new Image(String.valueOf(this.getTheUrl(res)));
    }

    public String getUrlString(GuiResEnum name) {
        return String.valueOf(getTheUrl(name));
    }

    public ImageView getCardImage(int cardId) {
        String urlString = "";
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/front/" + cardId + ".png");
            urlString = String.valueOf(url);
            System.out.println(urlString);
            Image image = new Image(urlString);
            ImageView imageView = new ImageView(image);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading front card image at Url: " + urlString);
            return null;
        }
    }

    public ImageView getCardImageRetro(int cardId) {
        String urlString = "";
        try {
            URL url = CodexNaturalis.class.getResource("/it/polimi/ingsw/am11/view/client/GUI" +
                                                       "/windows/cards/retro/" + cardId + "_Retro" +
                                                       ".png");
            urlString = String.valueOf(url);
            System.out.println(urlString);
            Image image = new Image(urlString);
            ImageView imageView = new ImageView(image);
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading retro card image at Url: " + urlString);
            return null;
        }
    }
}
