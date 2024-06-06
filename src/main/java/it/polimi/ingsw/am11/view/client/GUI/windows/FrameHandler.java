package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
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
    private final GuiResources guiResources;
    private final Stage stage;

    private final StackPane root;

    public FrameHandler(GuiResources guiResources, Stage stage, StackPane root) {
        this.guiResources = guiResources;
        this.stage = stage;
        this.root = root;

        setIcons();
    }

    private void setIcons() {
        //Proportions
        int size = (int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                   Screen.getPrimary().getBounds().getWidth()) * 0.7);

        int halfButtonSize = size / 48;
        int distanceToBorder = halfButtonSize >> 2;

        //Set title
        stage.setTitle("CodexNaturalis");

        //Set icon on the application bar
        stage.getIcons().add(guiResources.getTheImage(GuiResEnum.ICON));

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(guiResources.getTheUrl(GuiResEnum.ICON));
                taskbar.setIconImage(dockIcon);
            }
        }

        // Creating title bar buttons

        // Draggable Area
        javafx.scene.shape.Rectangle draggableRect = new Rectangle(size, halfButtonSize << 2);
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

        ImageView closeCross = guiResources.getTheImageView(GuiResEnum.CLOSE_CROSS);
        closeCross.setFitHeight(2 * halfButtonSize);
        closeCross.setPreserveRatio(true);
        closeCross.setOpacity(0.5);

        javafx.scene.control.Button closeButton = new javafx.scene.control.Button("_Cancel");
        closeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
        closeButton.setGraphic(closeCross);
        closeButton.setTranslateX(size / 2 - halfButtonSize);
        closeButton.setTranslateY(- size / 2 + halfButtonSize + 2 * distanceToBorder);
        closeButton.setBackground(Background.EMPTY);
        closeButton.setOnMouseEntered(event -> closeCross.setOpacity(0.7));
        closeButton.setOnMouseExited(event -> closeCross.setOpacity(0.5));
        closeButton.setOnMousePressed(event -> closeCross.setOpacity(1));

        closeButton.setOnAction(event -> {Platform.exit();});
        root.getChildren().add(closeButton);

        //Minimize Button

        ImageView minimizeBar = guiResources.getTheImageView(GuiResEnum.MINIMIZE_BAR);
        minimizeBar.setFitHeight(2 * halfButtonSize);
        minimizeBar.setPreserveRatio(true);
        minimizeBar.setOpacity(0.5);

        javafx.scene.control.Button minimizeButton = new Button("_Cancel");
        minimizeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
        minimizeButton.setGraphic(minimizeBar);
        minimizeButton.setTranslateX(size / 2 - 3 * halfButtonSize - 4 * distanceToBorder);
        minimizeButton.setTranslateY(- size / 2 + halfButtonSize + 2 * distanceToBorder);
        minimizeButton.setBackground(Background.EMPTY);
        minimizeButton.setOnMouseEntered(event -> minimizeBar.setOpacity(0.7));
        minimizeButton.setOnMouseExited(event -> minimizeBar.setOpacity(0.5));
        minimizeButton.setOnMousePressed(event -> minimizeBar.setOpacity(1));

        minimizeButton.setOnAction(event -> {stage.setIconified(true);});
        root.getChildren().add(minimizeButton);
        stage.setOnShowing(event -> {stage.setIconified(false);});
    }


}
