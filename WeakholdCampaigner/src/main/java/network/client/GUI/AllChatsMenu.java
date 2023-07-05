package network.client.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.client.controller.menu_controllers.ChatMenuController;
import network.common.chat.Chat;

public class AllChatsMenu extends AbstractMenu {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox("Chat Menu", getAllChats(), getMainMenuButton());
        borderPane.setCenter(vBox);
    }

    private HBox getAllChats() {
        HBox hBox = SceneBuilder.getHBox(getPublicChats(), getPrivateChats(), getRooms());

        return hBox;
    }

    private VBox getPrivateChats() {
        VBox vBox = SceneBuilder.getLabeledVBox("Private Chats");
        for (String name :
                ChatMenuController.getPrivateChats()) {
            vBox.getChildren().add(getSingleChatMenuButton(Chat.Type.PRIVATE_CHAT.toString(), name));
        }

        TextField textField = new TextField("Enter the username of who you want to privately message.");
        Button newPrivateChatButton = new Button("New private chat");
        newPrivateChatButton.setOnMouseClicked((mouseEvent) -> {
            String username = textField.getText();
            if (username == null) {
                showErrorAndWait("Please enter a username.");
                return;
            }

            String result = ChatMenuController.makePrivateChat(username);
            showInformationAlertAndWait(result);

            try {
                new AllChatsMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        vBox.getChildren().add(textField);
        vBox.getChildren().add(newPrivateChatButton);

        return vBox;
    }

    private VBox getRooms() {
        VBox vBox = SceneBuilder.getLabeledVBox("Rooms");
        for (String name :
                ChatMenuController.getRooms()) {
            vBox.getChildren().add(getSingleChatMenuButton(Chat.Type.ROOM.toString(), name));
        }


        TextField textField = new TextField("Enter the new room's name.");
        Button newPrivateChatButton = new Button("New room");
        newPrivateChatButton.setOnMouseClicked((mouseEvent) -> {
            String roomName = textField.getText();
            if (roomName == null) {
                showErrorAndWait("Please enter a room name.");
                return;
            }

            String result = ChatMenuController.makeRoom(roomName);
            showInformationAlertAndWait(result);

            try {
                new AllChatsMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        vBox.getChildren().add(textField);
        vBox.getChildren().add(newPrivateChatButton);


        return vBox;
    }

    private VBox getPublicChats() {
        VBox vBox = SceneBuilder.getLabeledVBox("Public Chat");
        for (String name :
                ChatMenuController.getPublicChats()) {
            vBox.getChildren().add(getSingleChatMenuButton(Chat.Type.PUBLIC_CHAT.toString(), name));
        }

        return vBox;
    }

    private static Button getMainMenuButton() {
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

    private static Button getSingleChatMenuButton(String type, String name) {
        Button button = new Button(name);
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new SingleChatMenu().manualStart(stage, type, name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }
}
