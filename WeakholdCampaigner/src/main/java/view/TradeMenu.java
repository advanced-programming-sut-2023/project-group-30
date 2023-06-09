package view;

import controller.menu_controllers.GameMenuController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.game.Government;

import java.util.ArrayList;

public class TradeMenu {
    public void run() {
        mainMenu();

    }

    private void mainMenu() {
        Stage tradeMenu = new Stage();
        tradeMenu.initModality(Modality.APPLICATION_MODAL);
        tradeMenu.setTitle("Trade Menu");
        BorderPane tradePane = new BorderPane();
        Button createNewTrade = new Button("Create New Trade");
        Button showPreviousTrade = new Button("Previous Trade");
        createNewTrade.setOnAction(event -> newTrade(tradeMenu));
        showPreviousTrade.setOnAction(event -> previousTrade());
        VBox vBox = new VBox(createNewTrade, showPreviousTrade);
        vBox.setSpacing(50);
        tradePane.setCenter(vBox);
        Scene popupScene1 = new Scene(tradePane, 200, 150);
        tradeMenu.setScene(popupScene1);
        tradeMenu.showAndWait();

    }

    private void previousTrade() {
    }

    private void newTrade(Stage stage) {
        VBox vBox = new VBox();
        ArrayList<Government> governments = GameMenuController.getCurrentGame().getGovernments();
        for (Government i : governments) {
            Label label = new Label(i.getOwner().getUsername());
            label.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
            label.getStyleClass().add("old-text");
            Button button = new Button();
            button.setGraphic(label);
            button.setStyle("-fx-background-color: transparent;");
            vBox.getChildren().add(button);
            button.setOnAction(event -> individualTrade(i, stage));
        }
        //Scene newTrade = new Scene();
    }

    private void individualTrade(Government i, Stage stage) {
        //HBox hBox = new
    }

}
