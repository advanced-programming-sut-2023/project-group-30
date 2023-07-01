package view.GUI;

import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.LoginMenuController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.GameMenu;
import view.menus.AppMenu;
import view.menus.AppMenu;

import java.util.ArrayList;


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
            String mapID = AbstractMenu.showTextInputDialogAndWait("Please enter a Map ID.");

            AbstractMenu.showInformationAlertAndWait("Good!",
                    "Now please enter the players' usernames or type !q when you are done.");
            ArrayList<String > usernames = new ArrayList<>();
            int playerIndex = 2;
            while (true) {
                String username = AbstractMenu.showTextInputDialogAndWait("player" + (playerIndex++) + ": ");
                if (username.equals("!q")) break;
                usernames.add(username);
            }

            switch (GameMenuController.createGame(Integer.parseInt(mapID), usernames)) {
                case MAP_DOES_NOT_EXIST:
                    AppMenu.show("Error: No map with this id exists.\n Your chosen map id should be between 1 to 5");
                    break;
                case INVALID_NUMBER_OF_PLAYERS:
                    AppMenu.show("Error: Each game can have 2 to 8 players, including you.");
                    break;
                case USERNAME_DOES_NOT_EXIST:
                    AppMenu.show("Error: At least one of the usernames does not exist.");
                    break;
                case SUCCESS:
                    AppMenu.show("Game created successfully. Use the above id to enter it.");
                    break;
            }
        });

        return button;
    }

    private static Button getEnterGameButton() {
        Button button = new Button("Enter a game");
        button.setOnMouseClicked((mouseEvent) -> {
            String id = AppMenu.getOneLine("Enter game id");
            if (!GameMenuController.loadGame(Integer.parseInt(id))) {
                AppMenu.show("No game with this id exists.");
            }
            else {
                try {
                    stage.close();
                    new GameMenu().start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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
