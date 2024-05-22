package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class LoadingScreen {

    public void animateLoadingScreen(int size, ImageView wolf, ImageView butterfly,
                                     ImageView mushroom, ImageView leaf, ImageView lDWritings,
                                     int symbolSize, ImageView lDDisks, ImageView lDSquare,
                                     ImageView lDBackground, ParallelTransition prT,
                                     SequentialTransition sqT) {
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

        // Make the writings fade away
        Duration FadeTime = Duration.millis(3000);

        FadeTransition WritingsFt = new FadeTransition(FadeTime, lDWritings);
        WritingsFt.setToValue(0.0);

        Duration RotationTime = Duration.millis(5000);

        Rotate lbSelfRotation = new Rotate();
        lbSelfRotation.setPivotX(size / 2);
        lbSelfRotation.setPivotY(size / 2);

        Timeline lbRotationTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                          new KeyValue(
                                                                  lbSelfRotation.angleProperty(),
                                                                  0)),
                                             new KeyFrame(RotationTime,
                                                          new KeyValue(
                                                                  lbSelfRotation.angleProperty(),
                                                                  45)));
        lDDisks.getTransforms().add(lbSelfRotation);
        lDSquare.getTransforms().add(lbSelfRotation);

        // Make symbols rotate around their centers

        Rotate rotate = new Rotate();

        Timeline symbolsSelfRotate = new Timeline(new KeyFrame(Duration.ZERO,
                                                               new KeyValue(rotate.angleProperty(),
                                                                            0)),
                                                  new KeyFrame(RotationTime,
                                                               new KeyValue(rotate.angleProperty(),
                                                                            - 45)));

        // Make symbols rotate around stage center

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


        Timeline leafTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                    new KeyValue(leafCenterRt.angleProperty(), 0)),
                                       new KeyFrame(RotationTime,
                                                    new KeyValue(leafCenterRt.angleProperty(),
                                                                 45)));
        Timeline mushroomTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                        new KeyValue(
                                                                mushroomCenterRt.angleProperty(),
                                                                0)),
                                           new KeyFrame(RotationTime,
                                                        new KeyValue(
                                                                mushroomCenterRt.angleProperty(),
                                                                45)));
        Timeline butterflyTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                         new KeyValue(
                                                                 butterflyCenterRt.angleProperty(),
                                                                 0)),
                                            new KeyFrame(RotationTime,
                                                         new KeyValue(
                                                                 butterflyCenterRt.angleProperty(),
                                                                 45)));
        Timeline wolfTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                    new KeyValue(wolfCenterRt.angleProperty(), 0)),
                                       new KeyFrame(RotationTime,
                                                    new KeyValue(wolfCenterRt.angleProperty(),
                                                                 45)));

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
    }
}
