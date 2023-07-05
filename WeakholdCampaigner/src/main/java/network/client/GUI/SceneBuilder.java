package network.client.GUI;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;

import static network.client.GUI.AbstractMenu.*;

public class SceneBuilder {
    public static Scene getScene(BorderPane borderPane) {
        return new Scene(borderPane, AbstractMenu.X_Ratio * PIXEL_UNIT, AbstractMenu.Y_Ratio * PIXEL_UNIT);
    }

    public static BorderPane getBorderPane() {
        BorderPane borderPane = new BorderPane();
        //borderPane.getStyleClass().add("backGround"); //does this actually work?

        //css styling:
        URL defaultCSSURL = SceneBuilder.class.getResource("/CSS/defaultCSS.css");
        if (defaultCSSURL != null)
            borderPane.getStylesheets().add(defaultCSSURL.toExternalForm());

        //background:
        URL backgroundURL = SceneBuilder.class.getResource("/Backgrounds/default.jpg");
        if (backgroundURL != null)
            borderPane.setBackground(new Background(new BackgroundImage(new Image(
                    backgroundURL.toExternalForm(),
                    PIXEL_UNIT * X_Ratio, PIXEL_UNIT * Y_Ratio, false, false
            ),
                    null, null, null, null)
            ));

        return borderPane;
    }

    public static VBox getLabeledVBox(String title, Node... elements) {
        VBox vBox = new VBox(SPACING);
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label(title);
        label.getStyleClass().add("title"); // resources/CSS/defaultCSS -> .title
        vBox.getChildren().add(label);

        vBox.getChildren().addAll(elements);

        return vBox;
    }

    public static VBox getVBox(Node... elements) {
        VBox vBox = new VBox(SPACING);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(elements);

        return vBox;
    }

    public static HBox getHBox(Node... elements) {
        HBox hBox = new HBox(SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(elements);

        return hBox;
    }
}
