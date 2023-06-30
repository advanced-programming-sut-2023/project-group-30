package view;

import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.ShopMenuController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.enums.Resource;
import model.game.Government;
import model.game.Shop;

import java.util.ArrayList;

public class ShopMenu {
    public void run() {
        Stage shop = new Stage();
        shop.initModality(Modality.APPLICATION_MODAL);
        shop.setTitle("Shop");
        shopMenu(shop);
    }

    private void shopMenu(Stage stage) {
        Button buttonForTradeMenu = new Button("Enter Trade Menu");
        buttonForTradeMenu.setStyle("-fx-background-color: transparent;");
        buttonForTradeMenu.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        buttonForTradeMenu.getStyleClass().add("old-text2");
        buttonForTradeMenu.setOnAction(event -> {
            TradeMenu tradeMenu = new TradeMenu();
            tradeMenu.run();
        });
        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane(hBox);
        Button food = new Button();
        food.setOnAction(event -> foodGood(borderPane));
        Button industry = new Button();
        industry.setOnAction(event -> industryGood(borderPane));
        Button weapon = new Button();
        weapon.setOnAction(event -> weaponGood(borderPane));
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
        hBox.getChildren().addAll(food, industry, weapon, buttonForTradeMenu);
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

    private void weaponGood(BorderPane borderPane) {
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
            button.setOnAction(event -> goToTheMenuOfSellingOrBuying(i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    private void industryGood(BorderPane borderPane) {
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
            button.setOnAction(event -> goToTheMenuOfSellingOrBuying(i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    private void foodGood(BorderPane borderPane) {
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
            button.setOnAction(event -> goToTheMenuOfSellingOrBuying(i, borderPane));
            button.setStyle("-fx-background-color: transparent;");
        }
        hBox.setSpacing(10);
        borderPane.getChildren().clear();
        borderPane.setCenter(hBox);
        hBox.setAlignment(Pos.CENTER);
    }

    int number = 1;
    Label numberText;
    Label buyPrice;
    Label sellPrice;

    private void goToTheMenuOfSellingOrBuying(Resource i, BorderPane borderPane) {
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
        HBox hBox = new HBox();
        HBox hBox1 = new HBox();


        VBox vBox = new VBox();

        ImageView resourceItem = new ImageView(new Image(TradeMenu.class
                .getResource("/ShopMenu/foodShop/" + i.getNameString() + ".png").toExternalForm()));

        HBox amountBox = new HBox();
        amountBox.setAlignment(Pos.CENTER);
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
        plusButton.setOnAction(event -> incrementNumber(i));
        Button minusButton = new Button("-");
        minusButton.setStyle("-fx-background-color: transparent;");
        minusButton.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        minusButton.getStyleClass().add("old-text1");
        minusButton.setOnAction(event -> decrementNumber(i));
        amountBox.getChildren().addAll(label1, numberText, minusButton, plusButton);
        amountBox.setSpacing(5);

        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: transparent;");
        buyButton.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        buyButton.getStyleClass().add("old-text1");
        buyButton.setOnAction(event -> buy(i, number));

        buyPrice = new Label(String.valueOf(number * Shop.getShopItemByName(i).getPurchasePrice()));
        buyPrice.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        buyPrice.getStyleClass().add("old-text2");

        Button sellButton = new Button("Sell");
        sellButton.setStyle("-fx-background-color: transparent;");
        sellButton.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        sellButton.getStyleClass().add("old-text1");
        sellButton.setOnAction(event -> sell(i, number));

        Label labelForOwn = new Label("You have " + government.getResources(i) + " number");
        labelForOwn.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        labelForOwn.getStyleClass().add("old-text2");

        sellPrice = new Label(String.valueOf(number * Shop.getShopItemByName(i).getSalesPrice()));
        sellPrice.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        sellPrice.getStyleClass().add("old-text2");

        hBox.getChildren().addAll(resourceItem, buyButton, buyPrice);
        hBox.setSpacing(6);
        hBox.setAlignment(Pos.CENTER);

        hBox1.getChildren().addAll(labelForOwn, sellButton, sellPrice);
        hBox1.setSpacing(6);
        hBox1.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(hBox, hBox1, amountBox);
        vBox.setSpacing(10);
        borderPane.setBackground((new Background(new BackgroundImage(new Image(GameMenu.class
                .getResource("/TradeMenu/Background.jpg").toExternalForm())
                , BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false)))));
        borderPane.getChildren().clear();
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
    }

    private void buy(Resource i, int number) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (ShopMenuController.buyItem(number, i.getNameString())) {
            case INVALID_RESOURCE:
                alert.setContentText("shop doesn't have this resource");
                break;
            case INVALID_AMOUNT:
                alert.setContentText("shop doesn't have enough amount of this resource");
                break;
            case INVALID_MONEY:
                alert.setContentText("you doesn't have enough money for buy this item");
                break;
            case INVALID_COMMAND:
                alert.setContentText("error: you entered your confirmation incorrect");
                break;
            case NOT_ENOUGH_SPACE:
                alert.setContentText("not enough space");
                break;
            case CANCEL:
                alert.setContentText("your purchase canceled successfully");
                break;
            case OK:
                alert.setContentText("you purchased successfully");
                break;
        }
        alert.showAndWait();
        GameMenu.setGold();
    }

    private void sell(Resource i, int number) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (ShopMenuController.sellItem(number, i.getNameString())) {
            case INVALID_RESOURCE:
                alert.setContentText("you doesn't have this resource");
                break;
            case INVALID_AMOUNT:
                alert.setContentText("you doesn't have enough amount");
                break;
            case CANCEL:
                alert.setContentText("you successfully canceled this item");
                break;
            case INVALID_COMMAND:
                alert.setContentText("error: you entered your confirmation incorrect");
                break;
            case OK:
                alert.setContentText("you sold successfully");
                break;
        }
        alert.showAndWait();
        GameMenu.setGold();
    }

    private void incrementNumber(Resource i) {
        number++;
        numberText.setText(String.valueOf(number));
        sellPrice.setText(String.valueOf(number * Shop.getShopItemByName(i).getSalesPrice()));
        buyPrice.setText(String.valueOf(number * Shop.getShopItemByName(i).getPurchasePrice()));
    }

    private void decrementNumber(Resource i) {
        if (number >= 1) {
            number--;
            numberText.setText(String.valueOf(number));
            sellPrice.setText(String.valueOf(number * Shop.getShopItemByName(i).getSalesPrice()));
            buyPrice.setText(String.valueOf(number * Shop.getShopItemByName(i).getPurchasePrice()));
        }
    }
}
