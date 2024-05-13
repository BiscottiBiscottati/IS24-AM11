package it.polimi.ingsw.am11.view.client.GUI.utils;

import it.polimi.ingsw.am11.view.client.GUI.window.CodexNaturalis;
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
            urlMap.put(name, CodexNaturalis.class.getResource(name.getFileName()));
        }
        return urlMap.get(name);
    }

    public Image getTheImage(GuiResEnum res) {
        return new Image(String.valueOf(this.getTheUrl(res)));
    }

    public String getUrlString(GuiResEnum name) {
        return String.valueOf(getTheUrl(name));
    }
}
