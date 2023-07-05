package network.client.GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import network.client.controller.menu_controllers.LeaderboardMenuController;

import java.util.HashMap;

public class LeaderboardMenu extends AbstractMenu{
    static Timeline autoRefresher = null;
    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Leaderboard", getLeaderboardVBox(),
                getRandomizeUserScoresButton(), getMainMenuButton());
        borderPane.setCenter(vBox);

        if (autoRefresher == null) {
            autoRefresher = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new EventHandler<ActionEvent>() {
                                //this is called every second
                                @Override
                                public void handle(ActionEvent event) {
                                    initialize();
                                }
                            })
            );
            autoRefresher.setCycleCount(Timeline.INDEFINITE);
            autoRefresher.play();
            //remember to call autoRefresher.stop() and set it to null
        }
    }

    private VBox getLeaderboardVBox() {
        VBox vBox = SceneBuilder.getVBox();

        Integer rank = 1;
        for (HashMap<String, String> arguments:
            LeaderboardMenuController.getLeaderboard()){

            String lastSeen = arguments.get("lastSeen");
            if (!lastSeen.equals("Online"))
                lastSeen = "Last seen: " + lastSeen;

            vBox.getChildren().add(SceneBuilder.getHBox(
                    new Label((rank++).toString()),
                    getAvatarRectangle(1.0, arguments.get("avatarURL")),
                    new Label("User: " + arguments.get("username")),
                    new Label("Score: " + arguments.get("score")),
                    new Label(lastSeen)
            ));
        }

        return vBox;
    }

    private Rectangle getAvatarRectangle(Double size, String avatarURL) {
        Rectangle avatar = new Rectangle(size * PIXEL_UNIT, size * PIXEL_UNIT);
        try {
            Image image = new Image(avatarURL);
            ImagePattern imagePattern = new ImagePattern(image);
            avatar.setFill(imagePattern);
        } catch (Exception exception) {
            avatar.setFill(new Color(0.69, 0.69, 0.69, 1));
        }

        return avatar;
    }

    private Button getMainMenuButton() {
        Button button = new Button("Main Menu");
        button.setOnMouseClicked((mouseEvent) -> {
            autoRefresher.stop();
            autoRefresher = null;

            try {
                new MainMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    private Button getRandomizeUserScoresButton() {
        Button button = new Button("Randomize users' scores");
        button.setOnMouseClicked((mouseEvent) -> {
            LeaderboardMenuController.randomizeUserScores();
        });

        return button;
    }
}
