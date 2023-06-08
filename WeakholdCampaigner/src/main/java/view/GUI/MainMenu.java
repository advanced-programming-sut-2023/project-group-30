package view.GUI;

import controller.menu_controllers.LoginMenuController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu extends AbstractMenu{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Main Menu", getEnterGameButton(),
                getCreateGameButton(), getProfileMenuButton(), getLogoutButton());
        borderPane.setCenter(vBox);
    }

    private static Button getLogoutButton() {
        Button button = new Button("Logout");
        button.setOnMouseClicked((mouseEvent) -> {
            LoginMenuController.userLogOut();
            try {
                new LoginMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    private static Button getCreateGameButton() {
        Button button = new Button("Create a new game");
        button.setOnMouseClicked((mouseEvent) -> {
            //todo
        });

        return button;
    }

    private static Button getEnterGameButton() {
        Button button = new Button("Enter a game");
        button.setOnMouseClicked((mouseEvent) -> {
            //todo
        });

        return button;
    }

    private static Button getProfileMenuButton() {
        Button button = new Button("Profile Menu");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new ProfileMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }
}
