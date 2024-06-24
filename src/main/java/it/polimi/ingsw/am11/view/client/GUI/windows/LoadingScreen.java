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

public class LoadingScreen {
    private static final Duration FADE_TIME = Duration.millis(300);
    private static final Duration ROTATION_TIME = Duration.millis(800);
    private static ImageView lDBackground, lDSquare, lDWritings, lDDisks, wolf, butterfly, mushroom,
            leaf;
    private static SequentialTransition sqT;
    private static ParallelTransition prT;
    private static int size;

    private static CodexNaturalis codexNaturalis;

    public static void createLoadingScreen(@NotNull CodexNaturalis codexNaturalis) {
        LoadingScreen.codexNaturalis = codexNaturalis;

        GuiResources guiResources = codexNaturalis.getGuiResources();
        StackPane root = codexNaturalis.getInitialRoot();
        size = codexNaturalis.getWindowSize();

        lDBackground = guiResources.getTheImageView(GuiResEnum.LGIN_BACKGROUND);
        lDSquare = guiResources.getTheImageView(GuiResEnum.LGIN_SQUARE);
        lDWritings = guiResources.getTheImageView(GuiResEnum.LGIN_WRITINGS);
        lDDisks = guiResources.getTheImageView(GuiResEnum.LGIN_DISK);

        wolf = guiResources.getTheImageView(GuiResEnum.WOLF_ICON);
        butterfly = guiResources.getTheImageView(GuiResEnum.BUTTERLFY_ICON);
        mushroom = guiResources.getTheImageView(GuiResEnum.MUSHROOM_ICON);
        leaf = guiResources.getTheImageView(GuiResEnum.LEAF_ICON);
        sqT = new SequentialTransition();
        prT = new ParallelTransition();
        root.getChildren().addAll(lDBackground, lDSquare, lDWritings, lDDisks,
                                  wolf, butterfly, mushroom, leaf);
    }

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
            CodexNaturalis.showNetworkPage();
        });


    }

    private static @NotNull Timeline getTl(@NotNull Rotate leafCenterRt,
                                           int x) {
        return new Timeline(
                new KeyFrame(Duration.ZERO,
                             new KeyValue(leafCenterRt.angleProperty(), 0)),
                new KeyFrame(LoadingScreen.ROTATION_TIME,
                             new KeyValue(leafCenterRt.angleProperty(), x)));
    }

}
