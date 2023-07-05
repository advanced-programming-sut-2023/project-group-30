package network.client.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.controller.menu_controllers.ChatMenuController;
import network.common.chat.Chat;
import network.common.chat.ChatMessage;

public class SingleChatMenu extends AbstractMenu{
    private Chat chat;
    private String name;

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new RuntimeException();
    }

    @Override
    protected void initialize() {
        VBox vBox = SceneBuilder.getLabeledVBox(name, getChatMessages(), getSendMessageField(),
                getRefreshButton(), getBackButton());
        if (chat.type.equals(Chat.Type.ROOM))
            vBox.getChildren().add(getAddUserHBox());

        borderPane.setCenter(vBox);
    }

    public void manualStart(Stage primaryStage, String chatType, String chatName) throws Exception {
        this.chat = ChatMenuController.getChat(chatType, chatName);
        this.name = chatName;

        super.start(primaryStage);
    }

    private static Button getBackButton() {
        Button button = new Button("Back");
        button.setOnMouseClicked((mouseEvent) -> {
            try {
                new AllChatsMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }

    private VBox getChatMessages() {
        VBox vBox = SceneBuilder.getVBox();

        for (ChatMessage message :
                chat.getChatMessages()) {
            Text preText = new Text("---by:" + message.sender + "---at:" + message.sentTime + "---");
            preText.setFill(new Color(1, 0, 0, 1));

            Text text = new Text(message.getMessage());
            text.setFill(new Color(1, 1, 0, 1));

            Text postText = new Text("-----------------");

            postText.setFill(new Color(1, 0, 0, 1));

            HBox editMessageField = getMessageDetailsAndOptionsField(message);

            vBox.getChildren().add(SceneBuilder.getHBox(getAvatarRectangle(1.0, message), preText));
            vBox.getChildren().add(text);
            if (editMessageField != null)
                vBox.getChildren().add(editMessageField);
            vBox.getChildren().add(postText);
        }

        return vBox;
    }

    private HBox getSendMessageField() {
        TextField messageTextField = new TextField("Write your message");
        Button sendMessageButton = new Button("Send");
        sendMessageButton.setOnMouseClicked((mouseEvent) -> {
            ChatMenuController.sendChatMessage(messageTextField.getText());
        });

        return SceneBuilder.getHBox(messageTextField, sendMessageButton);
    }

    private Button getRefreshButton() {
        Button button = new Button("Refresh");
        button.setOnMouseClicked((mouseEvent) -> {
            refreshPage();
        });

        return button;
    }

    private HBox getAddUserHBox() {
        TextField textField = new TextField("Please enter the username of that whom you want to add");
        Button button = new Button("Add member");
        button.setOnMouseClicked((mouseEvent) -> {
            String username = textField.getText();
            if (username == null) {
                showErrorAndWait("Please enter a username.");
                return;
            }

            String result = ChatMenuController.addMember(username);
            showInformationAlertAndWait(result);
        });


        return SceneBuilder.getHBox(textField, button);
    }

    private HBox getMessageDetailsAndOptionsField(ChatMessage chatMessage) {
        if (!ChatMenuController.canEditMessage(chatMessage.IDLocalToChat))
            return null;

        Button editMessageButton = new Button("Edit message");
        editMessageButton.setOnMouseClicked((mouseEvent) -> {
            String newMessage = showTextInputDialogAndWait("Enter new message");
            String result = ChatMenuController.editMessage(chatMessage.IDLocalToChat, newMessage);
            showInformationAlertAndWait(result);

            refreshPage();
        });

        Button deleteMessageButton = new Button("Delete message");
        deleteMessageButton.setOnMouseClicked((mouseEvent) -> {
            String result = ChatMenuController.deleteMessage(chatMessage.IDLocalToChat);
            showInformationAlertAndWait(result);

            refreshPage();
        });

        Text sent = new Text("--Sent--");
        sent.setFill(new Color(1, 0, 0, 1));

        Text seen = new Text();
        seen.setText(chatMessage.isSeen() ? "--Seen--" : "--Not Seen--");
        seen.setFill(new Color(1, 0, 0, 1));

        return SceneBuilder.getHBox(editMessageButton, deleteMessageButton, sent, seen);
    }

    private void refreshPage() {
        try {
            manualStart(stage, chat.type.toString(), name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Rectangle getAvatarRectangle(Double size, ChatMessage message) {
        Rectangle avatar = new Rectangle(size * PIXEL_UNIT, size * PIXEL_UNIT);
        try {
            Image image = new Image(message.senderAvatarURL);
            ImagePattern imagePattern = new ImagePattern(image);
            avatar.setFill(imagePattern);
        } catch (Exception exception) {
            avatar.setFill(new Color(0.69, 0.69, 0.69, 1));
        }

        return avatar;
    }
}
