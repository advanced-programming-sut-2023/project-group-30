package view;

import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.TradeMenuController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.enums.Resource;
import model.game.Government;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class TradeMenu {
    private Stage tradeMenu = new Stage();
    private Boolean[] request = {true};

    public void run() {
        mainMenu();

    }

    private void mainMenu() {
        tradeMenu.initModality(Modality.APPLICATION_MODAL);
        tradeMenu.setTitle("Trade Menu");
        BorderPane tradePane = new BorderPane();
        Button createNewTrade = new Button();
        Button showPreviousTrade = new Button();
        Button tradeList = new Button();
        ImageView createNewTradeNode = new ImageView(new Image(TradeMenu.class
                .getResource("/TradeMenu/NewTrade.png").toExternalForm()));
        ImageView tradeHistoryNode = new ImageView(new Image(TradeMenu.class
                .getResource("/TradeMenu/TradeHistory.png").toExternalForm()));
        ImageView tradeListNode = new ImageView(new Image(TradeMenu.class
                .getResource("/TradeMenu/TradeList.png").toExternalForm()));
        createNewTradeNode.setFitWidth(100);
        createNewTradeNode.setFitHeight(60);
        tradeHistoryNode.setFitWidth(100);
        tradeHistoryNode.setFitHeight(60);
        tradeListNode.setFitWidth(100);
        tradeListNode.setFitHeight(60);
        createNewTrade.setStyle("-fx-background-color: transparent;");
        showPreviousTrade.setStyle("-fx-background-color: transparent;");
        tradeList.setStyle("-fx-background-color: transparent;");
        createNewTrade.setGraphic(createNewTradeNode);
        showPreviousTrade.setGraphic(tradeHistoryNode);
        tradeList.setGraphic(tradeListNode);
        createNewTrade.setOnAction(event -> newTrade(tradeMenu));
        showPreviousTrade.setOnAction(event -> previousTrade());
        VBox vBox = new VBox(createNewTrade, showPreviousTrade, tradeList);
        vBox.setSpacing(10);
        tradePane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        tradePane.setBackground((new Background(new BackgroundImage(new Image(GameMenu.class
                .getResource("/TradeMenu/Background.jpg").toExternalForm())
                , BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false)))));
        Scene popupScene1 = new Scene(tradePane, 800, 300);
        tradeMenu.setScene(popupScene1);
        tradeMenu.show();

    }

    private void previousTrade() {
    }

    private void newTrade(Stage stage) {
        VBox vBox = new VBox();

        Button donateBtn = new Button();
        donateBtn.setStyle("-fx-background-color: transparent;");
        Button requestBtn = new Button();
        requestBtn.setStyle("-fx-background-color: transparent;");
        Label donateLabel = new Label("Donate");
        Label requestLabel = new Label("Request");
        donateLabel.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        donateLabel.getStyleClass().add("old-text1");
        donateBtn.setGraphic(donateLabel);
        requestLabel.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        requestLabel.getStyleClass().add("old-text1");
        requestBtn.setGraphic(requestLabel);
        donateBtn.setOnAction(e -> {
            request[0] = false;
            setGovernment(vBox, stage);
        });
        requestBtn.setOnAction(e -> {
            request[0] = true;
            setGovernment(vBox, stage);
        });
        HBox hBox = new HBox(donateBtn, requestBtn);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);

        BorderPane borderPane = new BorderPane(vBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        borderPane.setBackground((new Background(new BackgroundImage(new Image(GameMenu.class
                .getResource("/TradeMenu/Background.jpg").toExternalForm())
                , BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false)))));
        Scene scene = new Scene(borderPane, 800, 300);
        stage.setScene(scene);
        stage.show();

    }

    private void setGovernment(VBox vBox, Stage stage) {
        vBox.getChildren().remove(0);
        Label select = new Label("Select User");
        select.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        select.getStyleClass().add("old-text1");
        vBox.getChildren().add(select);
        ArrayList<Government> governments = GameMenuController.getCurrentGame().getGovernments();
        for (Government i : governments) {
            if (!i.equals(GameMenuController.getCurrentGame().getCurrentGovernment())) {
                Label label = new Label(i.getOwner().getUsername());
                label.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
                label.getStyleClass().add("old-text");
                Button button = new Button();
                button.setGraphic(label);
                button.setStyle("-fx-background-color: transparent;");
                vBox.getChildren().add(button);
                button.setOnAction(event -> individualTrade(GameMenuController.getCurrentGame().getCurrentGovernment()
                        , i, stage));
            }
        }
    }

    private void individualTrade(Government applicant, Government receiver, Stage stage) {
        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane(hBox);
        Button food = new Button();
        food.setOnAction(event -> foodGood(borderPane, applicant, receiver));
        Button industry = new Button();
        industry.setOnAction(event -> industryGood(borderPane, applicant, receiver));
        Button weapon = new Button();
        weapon.setOnAction(event -> weaponGood(borderPane, applicant, receiver));
        ImageView foodPic = new ImageView(new Image(TradeMenu.class
                .getResource("/ShopMenu/food.png").toExternalForm()));
        ImageView industryPic = new ImageView(new Image(TradeMenu.class
                .getResource("/ShopMenu/industry.png").toExternalForm()));
        ImageView weaponPic = new ImageView(new Image(TradeMenu.class
                .getResource("/ShopMenu/weapon.png").toExternalForm()));
        food.setStyle("-fx-background-color: transparent;");
        industry.setStyle("-fx-background-color: transparent;");
        weapon.setStyle("-fx-background-color: transparent;");
        foodPic.setFitWidth(60);
        foodPic.setFitHeight(60);
        industryPic.setFitWidth(60);
        industryPic.setFitHeight(60);
        weaponPic.setFitWidth(60);
        weaponPic.setFitHeight(60);
        food.setGraphic(foodPic);
        industry.setGraphic(industryPic);
        weapon.setGraphic(weaponPic);
        hBox.getChildren().addAll(food, industry, weapon);
        hBox.setSpacing(30);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setBackground((new Background(new BackgroundImage(new Image(GameMenu.class
                .getResource("/TradeMenu/shopMenu.png").toExternalForm())
                , BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false)))));
        Scene scene = new Scene(borderPane, 600, 210);
        stage.setScene(scene);
        stage.show();
    }

    private void weaponGood(BorderPane borderPane, Government applicant, Government receiver) {
        ArrayList<Resource> foods = new ArrayList<>();
        foods.add(Resource.ARMOR);
        foods.add(Resource.BOW);
        foods.add(Resource.SPEAR);
        foods.add(Resource.SWORD);
        HBox hBox = new HBox();
        for (Resource i : foods) {
            ImageView imageView = new ImageView(new Image(TradeMenu.class
                    .getResource("/ShopMenu/weapon/" + i.getNameString() + ".png").toExternalForm()));
            Button button = new Button();
            button.setGraphic(imageView);
            hBox.getChildren().add(button);
            button.setOnAction(event -> sendTrade(applicant, receiver, i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    private void industryGood(BorderPane borderPane, Government applicant, Government receiver) {
        ArrayList<Resource> foods = new ArrayList<>();
        foods.add(Resource.IRON);
        foods.add(Resource.STONE);
        foods.add(Resource.WOOD);
        HBox hBox = new HBox();
        for (Resource i : foods) {
            ImageView imageView = new ImageView(new Image(TradeMenu.class
                    .getResource("/ShopMenu/industryShop/" + i.getNameString() + ".png").toExternalForm()));
            Button button = new Button();
            button.setGraphic(imageView);
            hBox.getChildren().add(button);
            button.setOnAction(event -> sendTrade(applicant, receiver, i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    private void foodGood(BorderPane borderPane, Government applicant, Government receiver) {
        ArrayList<Resource> foods = new ArrayList<>();
        foods.add(Resource.APPLE);
        foods.add(Resource.WINE);
        foods.add(Resource.CHEESE);
        foods.add(Resource.FLOUR);
        foods.add(Resource.MEAT);
        foods.add(Resource.GRAIN);
        foods.add(Resource.WHEAT);
        foods.add(Resource.BREAD);
        HBox hBox = new HBox();
        for (Resource i : foods) {
            ImageView imageView = new ImageView(new Image(TradeMenu.class
                    .getResource("/ShopMenu/foodShop/" + i.getNameString() + ".png").toExternalForm()));
            Button button = new Button();
            button.setGraphic(imageView);
            hBox.getChildren().add(button);
            button.setOnAction(event -> sendTrade(applicant, receiver, i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    int number = 0;
    Label numberText;

    private void sendTrade(Government applicant, Government receiver, Resource resource, BorderPane borderPane) {
        VBox vBox = new VBox();
        HBox hbox = new HBox();
        Label label = new Label("Price:");
        label.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        label.getStyleClass().add("old-text2");
        TextField textField = new TextField();
        textField.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        hbox.getChildren().addAll(label, textField);
        hbox.setSpacing(5);

        HBox hBox1 = new HBox();
        Label label1 = new Label("Amount:");
        label1.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        label1.getStyleClass().add("old-text2");
        numberText = new Label(String.valueOf(number));
        numberText.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        numberText.getStyleClass().add("old-text2");
        Button plusButton = new Button("+");
        plusButton.setStyle("-fx-background-color: transparent;");
        plusButton.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        plusButton.getStyleClass().add("old-text1");
        plusButton.setOnAction(event -> incrementNumber());
        Button minusButton = new Button("-");
        minusButton.setStyle("-fx-background-color: transparent;");
        minusButton.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        minusButton.getStyleClass().add("old-text1");
        minusButton.setOnAction(event -> decrementNumber());
        hBox1.getChildren().addAll(label1, numberText, minusButton, plusButton);
        hBox1.setSpacing(5);

        HBox hBox2 = new HBox();
        Label messageLabel = new Label("Message:");
        messageLabel.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        messageLabel.getStyleClass().add("old-text2");
        TextField message = new TextField();
        message.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        hBox2.setSpacing(5);
        hBox2.getChildren().addAll(messageLabel, message);

        hbox.setAlignment(Pos.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);

        Button send = new Button("Send");
        send.setStyle("-fx-background-color: transparent;");
        send.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        send.getStyleClass().add("old-text1");
        send.setOnAction(event -> {
            if (getConformation())
                trade(applicant, receiver, resource, message.getText(), number, Integer.parseInt(textField.getText()));
            tradeMenu.close();
        });

        vBox.getChildren().addAll(hbox, hBox1, hBox2, send);
        vBox.setSpacing(15);
        borderPane.getChildren().clear();
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setBackground((new Background(new BackgroundImage(new Image(GameMenu.class
                .getResource("/TradeMenu/Background.jpg").toExternalForm())
                , BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false)))));


    }

    private void trade(Government applicant, Government receiver, Resource resource, String message, int number
            , int price) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        switch (TradeMenuController.trade(receiver.getOwner(), resource.getNameString(), number, price, message
                , request[0])) {
            case INVALID_RESOURCE:
                alert.setContentText("error: resource is not correct");
                break;
            case INVALID_MONEY:
                alert.setContentText("you does not haven enough gold coin");
                break;
            case NOT_ENOUGH_SPACE:
                alert.setContentText("not enough space");
                break;
            case INVALID_AMOUNT:
                alert.setContentText("you don't have this amount to donate");
                break;
            case OK:
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("your request/donation added");
                break;
        }
        alert.showAndWait();
    }

    private void incrementNumber() {
        number++;
        numberText.setText(String.valueOf(number));
    }

    private void decrementNumber() {
        if (number >= 1) {
            number--;
            numberText.setText(String.valueOf(number));
        }
    }

    private boolean getConformation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you do this trade?");
        alert.setContentText("You cannot undo this action.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

}
