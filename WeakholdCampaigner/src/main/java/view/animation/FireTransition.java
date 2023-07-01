package view.animation;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

public class FireTransition extends Transition {
    private final ImageView imageView;
    private final int numFrames;
    private final int width;
    private final int height;
    private int currentFrame = 0;
    private int turn = 0;
    private Pane pane;

    public FireTransition(Pane pane, int numFrames, int width, int height) {
        this.numFrames = numFrames;
        this.width = width;
        this.height = height;
        this.setCycleDuration(Duration.millis(2000));
        this.setCycleCount(Animation.INDEFINITE);
        this.play();

        // Create ImageView and add it to the BorderPane
        imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        pane.getChildren().add(imageView);
        this.pane = pane;
    }

    @Override
    protected void interpolate(double fraction) {
        if (fraction >= (currentFrame + 1.0) / numFrames) {
            currentFrame = (currentFrame + 1) % numFrames;
            Image image = new Image(FireTransition.class.getResource("/fire/" + (currentFrame + 1) + ".png")
                    .toExternalForm(), width, height, true, false);
            imageView.setImage(image);
        }
    }
    public void addTurn() {
        if (turn == 3) {
            this.stop();
            pane.getChildren().remove(imageView);

        }
        turn++;
    }
}