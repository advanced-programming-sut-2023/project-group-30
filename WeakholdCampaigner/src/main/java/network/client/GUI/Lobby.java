package network.client.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.client.controller.menu_controllers.LobbyController;

import java.util.ArrayList;
import java.util.HashMap;

public class Lobby extends AbstractMenu{
    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Lobby", getCreateGameHBox(),
                getMainMenuButton());
        borderPane.setCenter(vBox);
    }

    private HBox getCreateGameHBox() {
        TextField capacityTextField = new TextField("capacity");
        Button createGameButton = new Button("New Game");
        createGameButton.setOnMouseClicked((mouseEvent) -> {
            String capacity = capacityTextField.getText();
            try {
                Integer.parseInt(capacity);
            } catch (NumberFormatException e) {
                showErrorAndWait("Please enter a valid capacity. (A number.)");
                return;
            }

            String result = LobbyController.createGame(capacity);
            showInformationAlertAndWait(result);

            initialize();
        });

        return SceneBuilder.getHBox(capacityTextField, createGameButton);
    }

    private HBox getGames() {
        HBox hBox = SceneBuilder.getHBox(new Label("Games"));
        ArrayList<String> players = new ArrayList<>();
        String capacity = "NA";

        HashMap<String, String> games = LobbyController.getGames();
        for (String key :
                games.keySet()) {
            if (key.equals("capacity"))
                capacity = games.get(key);
            else {
                players.add(games.get(key));
            }
        }

        hBox.getChildren().add(new Label("Capacity: " + capacity));
        for (String player :
                players) {
            hBox.getChildren().add(new Label(player));
        }

        return hBox;
    }

    private Button getMainMenuButton() {
        Button button = new Button("Main Menu");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new MainMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }
}
