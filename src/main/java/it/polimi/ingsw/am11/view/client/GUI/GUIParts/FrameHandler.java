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

/**
 * This class is responsible for handling the frame of the application. It includes methods for
 * setting the icons of the application, creating title bar buttons, and making the application
 * frame draggable.
 */
public class FrameHandler {


    /**
     * This method is used to set the icons of the application and create the title bar buttons. It
     * sets the icon on the application bar and the icon on the taskbar/dock. It also creates the
     * title bar buttons for the application. The close button closes the application, and the
     * minimized button minimizes the application.
     *
     * @param stage The stage of the application.
     * @param root  The root of the application.
     */
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

    /**
     * This method is used to create a draggable rectangle for the application frame. The rectangle
     * is transparent and its size and position are determined by the provided parameters. The
     * rectangle also has a mouse event handler that allows the user to drag the application frame
     * around the screen.
     *
     * @param stage          The stage of the application.
     * @param size           The size of the rectangle.
     * @param halfButtonSize Half the size of the title bar button.
     * @return The draggable rectangle.
     */
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

    /**
     * This method is used to set up a tile bar button for the application. It creates a new button,
     * sets its size, graphic, and position, and adds mouse event handlers. The button's opacity
     * changes when the mouse enters, exits, or presses the button.
     *
     * @param size           The size of the button.
     * @param halfButtonSize Half the size of the title bar button.
     * @param imageView      The ImageView object to be set as the button's graphic.
     * @return The set-up title bar button.
     */
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
