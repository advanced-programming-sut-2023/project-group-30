package view.GUI;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static view.GUI.AbstractMenu.PIXEL_UNIT;
import static view.GUI.AbstractMenu.SPACING;

public class SceneBuilder {
    public static Scene getScene(BorderPane borderPane) {
        return new Scene(borderPane, AbstractMenu.X_Ratio * PIXEL_UNIT, AbstractMenu.Y_Ratio * PIXEL_UNIT);
    }

    public static BorderPane getBorderPane() {
        BorderPane borderPane = new BorderPane();
        //borderPane.getStylesheets().add(SceneBuilder.class.getResource("/CSS/menuStyle.css").toExternalForm());
        //borderPane.getStyleClass().add("BackGround"); //does this actually work?
        //todo background
        //stylesheets="@../CSS/menuStyle.css"

        return borderPane;
    }

    public static VBox getLabeledVBox(String title, Node... elements) {
        VBox vBox = new VBox(SPACING);
        vBox.setAlignment(Pos.CENTER);

        Label label =  new Label(title);
        //label.getStyleClass().add("title");
        //todo
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
