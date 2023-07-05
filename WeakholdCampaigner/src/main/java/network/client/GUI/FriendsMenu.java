package network.client.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import network.client.controller.menu_controllers.ProfileMenuController;

public class FriendsMenu extends AbstractMenu {

    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Friends", sendFriendRequestField(),
                SceneBuilder.getHBox(getFriendsVBox(), getFriendRequestsVBox()), getMainMenuButton());
        borderPane.setCenter(vBox);
    }

    private HBox sendFriendRequestField() {
        TextField textField = new TextField("username");
        Button button = new Button("Send friend request");
        button.setOnMouseClicked((mouseEvent) -> {
            String username = textField.getText();
            String result = ProfileMenuController.sendFriendRequest(username);

            showInformationAlertAndWait(result);
        });

        return SceneBuilder.getHBox(textField, button);
    }

    private VBox getFriendsVBox() {
        VBox vBox = SceneBuilder.getLabeledVBox("Your friends");

        for (String username :
                ProfileMenuController.getFriends()) {
            Text text = new Text(username);
            text.setFill(new Color(1, 0, 0, 1));
            vBox.getChildren().add(
                    text
            );
        }

        return vBox;
    }

    private VBox getFriendRequestsVBox() {
        VBox vBox = SceneBuilder.getLabeledVBox("Friend requests");

        for (String username :
                ProfileMenuController.getFriendRequests()) {
            Text text = new Text(username);
            text.setFill(new Color(1, 0, 0, 1));

            Button accept = new Button("Accept");
            accept.setOnMouseClicked((mouseEvent) -> {
                String result = ProfileMenuController.resolveFriendRequest(true, username);
                showInformationAlertAndWait(result);

                initialize();
            });
            Button deny = new Button("Deny");
            deny.setOnMouseClicked((mouseEvent) -> {
                String result = ProfileMenuController.resolveFriendRequest(false, username);
                showInformationAlertAndWait(result);

                initialize();
            });

            vBox.getChildren().add(
                    SceneBuilder.getHBox(text, accept, deny)
            );
        }

        return vBox;
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
