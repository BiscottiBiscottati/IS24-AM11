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
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FrameHandler {


    //This class is responsible for setting the frame of the application, the frame is the top
    // bar of the application, and the icons of the application.

    public static void setIcons(@NotNull Stage stage, StackPane root) {

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
        Rectangle draggableRect = getDraggableRect(stage, size, halfButtonSize);

        root.getChildren().add(draggableRect);

        //Close Button

        ImageView closeCross = GuiResources.getTheImageView(GuiResEnum.CLOSE_CROSS);
        Button closeButton = setUpTilebarButton(size, halfButtonSize, distanceToBorder, closeCross);
        closeButton.setTranslateX((double) size / 2 - halfButtonSize);

        closeButton.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
        root.getChildren().add(closeButton);

        //Minimize Button

        ImageView minimizeBar = GuiResources.getTheImageView(GuiResEnum.MINIMIZE_BAR);
        Button minimizeButton = setUpTilebarButton(size, halfButtonSize, distanceToBorder,
                                                   minimizeBar);
        minimizeButton.setTranslateX((double) size / 2 - 3 * halfButtonSize - 4 * distanceToBorder);

        minimizeButton.setOnAction(event -> stage.setIconified(true));
        root.getChildren().add(minimizeButton);
        stage.setOnShowing(event -> stage.setIconified(false));
    }

    private static @NotNull Rectangle getDraggableRect(@NotNull Stage stage, int size,
                                                       int halfButtonSize) {
        Rectangle draggableRect = new Rectangle(size, halfButtonSize << 2);
        draggableRect.setFill(Color.TRANSPARENT);
        draggableRect.setTranslateY((double) - size / 2 + halfButtonSize);
        draggableRect.setOnMousePressed(pressEvent -> draggableRect.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
        return draggableRect;
    }

    private static @NotNull Button setUpTilebarButton(int size, int halfButtonSize,
                                                      int distanceToBorder,
                                                      @NotNull ImageView imageView) {
        imageView.setFitHeight(2 * halfButtonSize);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(0.5);

        Button newButton = new Button("_Cancel");
        newButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
        newButton.setGraphic(imageView);
        newButton.setTranslateY((double) - size / 2 + halfButtonSize + 2 * distanceToBorder);
        newButton.setBackground(Background.EMPTY);
        newButton.setOnMouseEntered(event -> imageView.setOpacity(0.7));
        newButton.setOnMouseExited(event -> imageView.setOpacity(0.5));
        newButton.setOnMousePressed(event -> imageView.setOpacity(1));
        return newButton;
    }


}
