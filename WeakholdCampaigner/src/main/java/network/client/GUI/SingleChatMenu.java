package network.client.GUI;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
            preText.setFill(new Color(1, 1, 0, 1));
            vBox.getChildren().add(preText);
            Text text = new Text(message.getMessage());
            text.setFill(new Color(1, 1, 0, 1));
            vBox.getChildren().add(text);
            Text postText = new Text("---------");
            postText.setFill(new Color(1, 1, 0, 1));
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
            try {
                manualStart(stage, chat.type.toString(), name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return button;
    }
}
