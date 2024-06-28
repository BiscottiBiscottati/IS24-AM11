package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.SQUARE_SIZE;

/**
 * The LoadingScreen class is responsible for creating and managing the Loading Screen in the GUI.
 */
public class LoadingScreen {
    private static final Duration FADE_TIME = Duration.millis(300);
    private static final Duration ROTATION_TIME = Duration.millis(800);
    private static ImageView lDBackground, lDSquare, lDWritings, lDDisks, wolf, butterfly, mushroom,
            leaf;
    private static SequentialTransition sqT;
    private static ParallelTransition prT;
    private static int size;

    /**
     * This static method is used to create the Loading Screen in the GUI. It initializes the
     * necessary components and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the Loading Screen is a part of.
     */
    public static void createLoadingScreen(CodexNaturalis codexNaturalis) {

        StackPane root = codexNaturalis.getSmallRoot();
        size = SQUARE_SIZE.getValue();

        lDBackground = GuiResources.getTheImageView(GuiResEnum.LGIN_BACKGROUND);
        lDSquare = GuiResources.getTheImageView(GuiResEnum.LGIN_SQUARE);
        lDWritings = GuiResources.getTheImageView(GuiResEnum.LGIN_WRITINGS);
        lDDisks = GuiResources.getTheImageView(GuiResEnum.LGIN_DISK);

        wolf = GuiResources.getTheImageView(GuiResEnum.WOLF_ICON);
        butterfly = GuiResources.getTheImageView(GuiResEnum.BUTTERLFY_ICON);
        mushroom = GuiResources.getTheImageView(GuiResEnum.MUSHROOM_ICON);
        leaf = GuiResources.getTheImageView(GuiResEnum.LEAF_ICON);
        sqT = new SequentialTransition();
        prT = new ParallelTransition();
        root.getChildren().addAll(lDBackground, lDSquare, lDWritings, lDDisks,
                                  wolf, butterfly, mushroom, leaf);
    }

    /**
     * This static method is used to animate the Loading Screen in the GUI. It sets up the
     * animations for the symbols and the background and plays them.
     */
    public static void animateLoadingScreen() {
        // setup for loading screen
        int symbolSize = size / 8;
        lDBackground.setFitHeight(size);
        lDBackground.setPreserveRatio(true);
        lDSquare.setFitHeight(size);
        lDSquare.setPreserveRatio(true);
        lDWritings.setFitHeight(size);
        lDWritings.setPreserveRatio(true);
        lDDisks.setFitHeight(size);
        lDDisks.setPreserveRatio(true);
        wolf.setFitHeight(symbolSize);
        wolf.setPreserveRatio(true);
        wolf.setTranslateX(117.0 * size / 512.0);
        wolf.setTranslateY(- 121.0 * size / 512.0);
        butterfly.setFitHeight(symbolSize);
        butterfly.setPreserveRatio(true);
        butterfly.setTranslateX(- 122.0 * size / 512.0);
        butterfly.setTranslateY(118.0 * size / 512.0);
        mushroom.setFitHeight(symbolSize);
        mushroom.setPreserveRatio(true);
        mushroom.setTranslateX(119.0 * size / 512.0);
        mushroom.setTranslateY(117.0 * size / 512.0);
        leaf.setFitHeight(symbolSize);
        leaf.setPreserveRatio(true);
        leaf.setTranslateX(- 123.0 * size / 512.0);
        leaf.setTranslateY(- 121.0 * size / 512.0);

        FadeTransition WritingsFt = new FadeTransition(FADE_TIME, lDWritings);
        WritingsFt.setToValue(0.0);

        Rotate lbSelfRotation = new Rotate();
        lbSelfRotation.setPivotX((double) size / 2);
        lbSelfRotation.setPivotY((double) size / 2);

        Timeline lbRotationTl = getTl(lbSelfRotation, 45);
        lDDisks.getTransforms().add(lbSelfRotation);
        lDSquare.getTransforms().add(lbSelfRotation);

        // Make symbols rotate around their centers

        Rotate rotate = new Rotate();

        Timeline symbolsSelfRotate = getTl(rotate, - 45);

        // Make symbols rotate around a stage center

        Rotate wolfCenterRt = new Rotate();
        Rotate leafCenterRt = new Rotate();
        Rotate mushroomCenterRt = new Rotate();
        Rotate butterflyCenterRt = new Rotate();

        leafCenterRt.setPivotX(123.0 * size / 512.0);
        leafCenterRt.setPivotY(121.0 * size / 512.0);
        leaf.getTransforms().add(leafCenterRt);

        mushroomCenterRt.setPivotX(- 119.0 * size / 512.0);
        mushroomCenterRt.setPivotY(- 117.0 * size / 512.0);
        mushroom.getTransforms().add(mushroomCenterRt);

        wolfCenterRt.setPivotX(- 117.0 * size / 512.0);
        wolfCenterRt.setPivotY(121.0 * size / 512.0);
        wolf.getTransforms().add(wolfCenterRt);

        butterflyCenterRt.setPivotX(122.0 * size / 512.0);
        butterflyCenterRt.setPivotY(- 118.0 * size / 512.0);
        butterfly.getTransforms().add(butterflyCenterRt);


        Timeline leafTl = getTl(leafCenterRt, 45);
        Timeline mushroomTl = getTl(mushroomCenterRt, 45);
        Timeline butterflyTl = getTl(butterflyCenterRt, 45);
        Timeline wolfTl = getTl(wolfCenterRt, 45);

        mushroom.getTransforms().add(rotate);
        wolf.getTransforms().add(rotate);
        leaf.getTransforms().add(rotate);
        butterfly.getTransforms().add(rotate);

        prT.getChildren().addAll(lbRotationTl, symbolsSelfRotate, leafTl, mushroomTl,
                                 butterflyTl, wolfTl);
        sqT.getChildren().addAll(WritingsFt, prT);

        lDSquare.setOnMouseClicked(event -> {
            sqT.play();
            lDBackground.setOnMouseClicked(e -> {
                sqT.jumpTo("end");
            });
            lDSquare.setDisable(true);
            lDWritings.setDisable(true);

        });
        lDWritings.setOnMouseClicked(event -> {
            sqT.play();
            lDBackground.setOnMouseClicked(e -> {
                sqT.jumpTo("end");
            });
            lDWritings.setDisable(true);
            lDSquare.setDisable(true);
            mushroom.setDisable(true);
            wolf.setDisable(true);
            leaf.setDisable(true);
            butterfly.setDisable(true);
        });

        //no more necessary
        sqT.onFinishedProperty().set(event -> {
            NetworkPage.showNetworkPage();
        });


    }


    /**
     * This static method is used to get the timeline of the rotation of the symbols.
     * @param leafCenterRt the rotation of the leaf
     * @param x the angle of the rotation
     * @return the timeline of the rotation
     */
    private static @NotNull Timeline getTl(@NotNull Rotate leafCenterRt,
                                           int x) {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                             new KeyValue(leafCenterRt.angleProperty(), 0)),
                new KeyFrame(LoadingScreen.ROTATION_TIME,
                             new KeyValue(leafCenterRt.angleProperty(), x)));
    }

}
