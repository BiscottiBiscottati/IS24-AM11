package it.polimi.ingsw.am11.view.client.GUI.GUIParts;

import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;

public class FrameHandler {


    //This class is responsible for setting the frame of the application, the frame is the top
    // bar of the application, and the icons of the application.

    public static void setIcons(Stage stage, StackPane root) {

        //Proportions
        int size = Proportions.SQUARE_SIZE.getValue();

        int halfButtonSize = Proportions.HALF_BUTTON_SIZE.getValue();
        int distanceToBorder = Proportions.DISTANCE_TO_BORDER.getValue();

        //Set title
        stage.setTitle("CodexNaturalis");

        //Set icon on the application bar
        stage.getIcons().add(GuiResources.getTheImage(GuiResEnum.ICON));

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(GuiResources.getTheUrl(GuiResEnum.ICON));
                taskbar.setIconImage(dockIcon);
            }
        }

        // Creating title bar buttons

        // Draggable Area
        Rectangle draggableRect = new Rectangle(size, halfButtonSize << 2);
        draggableRect.setFill(Color.TRANSPARENT);
        draggableRect.setTranslateY(- size / 2 + halfButtonSize);
        draggableRect.setOnMousePressed(pressEvent -> {
            draggableRect.setOnMouseDragged(dragEvent -> {
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        root.getChildren().add(draggableRect);

        //Close Button

        ImageView closeCross = GuiResources.getTheImageView(GuiResEnum.CLOSE_CROSS);
        closeCross.setFitHeight(2 * halfButtonSize);
        closeCross.setPreserveRatio(true);
        closeCross.setOpacity(0.5);

        Button closeButton = new Button("_Cancel");
        closeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
        closeButton.setGraphic(closeCross);
        closeButton.setTranslateX(size / 2 - halfButtonSize);
        closeButton.setTranslateY(- size / 2 + halfButtonSize + 2 * distanceToBorder);
        closeButton.setBackground(Background.EMPTY);
        closeButton.setOnMouseEntered(event -> closeCross.setOpacity(0.7));
        closeButton.setOnMouseExited(event -> closeCross.setOpacity(0.5));
        closeButton.setOnMousePressed(event -> closeCross.setOpacity(1));

        closeButton.setOnAction(event -> Platform.exit());
        root.getChildren().add(closeButton);

        //Minimize Button

        ImageView minimizeBar = GuiResources.getTheImageView(GuiResEnum.MINIMIZE_BAR);
        minimizeBar.setFitHeight(2 * halfButtonSize);
        minimizeBar.setPreserveRatio(true);
        minimizeBar.setOpacity(0.5);

        Button minimizeButton = new Button("_Cancel");
        minimizeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
        minimizeButton.setGraphic(minimizeBar);
        minimizeButton.setTranslateX((double) size / 2 - 3 * halfButtonSize - 4 * distanceToBorder);
        minimizeButton.setTranslateY((double) - size / 2 + halfButtonSize + 2 * distanceToBorder);
        minimizeButton.setBackground(Background.EMPTY);
        minimizeButton.setOnMouseEntered(event -> minimizeBar.setOpacity(0.7));
        minimizeButton.setOnMouseExited(event -> minimizeBar.setOpacity(0.5));
        minimizeButton.setOnMousePressed(event -> minimizeBar.setOpacity(1));

        minimizeButton.setOnAction(event -> {stage.setIconified(true);});
        root.getChildren().add(minimizeButton);
        stage.setOnShowing(event -> {stage.setIconified(false);});
    }


}
