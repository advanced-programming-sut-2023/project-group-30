package view;

import controller.menu_controllers.GameEntityController;
import controller.menu_controllers.GameMenuController;
import controller.messages.MenuMessages;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Database;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.map.Map;
import model.game.map.MapCell;
import view.menus.AbstractMenu;

import java.util.*;
import java.util.regex.Pattern;

import static model.game.map.MapCell.Texture.*;


public class GameMenu extends Application {
    private GridPane gridPane;
    private ScrollPane scrollPane;
    private StackPane gamePane;

    private ArrayList<Building> buildings;
    private int unitNumbers ;
    private boolean drag;
    private Rectangle rectangle;
    private int chosenX;
    private int chosenY;
    private HashMap<Unit, ImageView> unitsInMap;
    private ArrayList<TranslateTransition> transition = new ArrayList<>();

    @Override
    public void start(Stage stage){
        gridPane = new GridPane();
        gridPane.setVgap(-0.5);
        gridPane.setHgap(-0.5);
        Database.loadMap();
        scrollPane = new ScrollPane(gridPane);
        scrollPane.setPrefViewportWidth(stage.getMaxWidth()); // 40 cells wide (20 pixels each)
        scrollPane.setPrefViewportHeight(stage.getMaxHeight()); // 40 cells high (20 pixels each)
        gamePane = new StackPane(scrollPane);
        Scene scene = new Scene(gamePane, stage.getMaxWidth(), stage.getMaxHeight());
        scene.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.UP){
                scrollUp();
            }else if(event.getCode() == KeyCode.DOWN){
                scrollDown();
            } else if (event.getCode() == KeyCode.RIGHT) {
                scrollRight();
            } else if (event.getCode() == KeyCode.LEFT) {
                scrollLeft();
            }
        });
        scrollPane.setPrefSize(stage.getMaxWidth(), stage.getMaxHeight());
        createMap(Database.getMapById(1));//TODO: after finishing game menu change 1
        setZoom(scrollPane);
        dropUnit(stage ,scene);
        scene.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        stage.setScene(scene);
        showDetailWithDragClick(gridPane);
        pressedNode(gridPane, stage);
        stage.setFullScreen(true);
        stage.show();
    }

    private void dropUnit(Stage stage, Scene scene) {
        unitsInMap  = new HashMap<>();
        Image buttonImage = new Image(GameMenu.class.getResource("/Icon/dropUnit.png").toExternalForm());
        ImageView imageView = new ImageView(buttonImage);
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent");
        button.setGraphic(imageView);
        gamePane.getChildren().add(button);
        gamePane.setAlignment(button, Pos.CENTER_LEFT);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Image image = new Image(GameMenu.class.getResource
                        ("/Backgrounds/selectUnitBack.png").toExternalForm());
                BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
                GridPane unitPane = new GridPane();
                Scene scene2 = new Scene(unitPane, stage.getMaxWidth(), stage.getMaxHeight());
                Background background = new Background(backgroundImage);
                unitPane.setBackground(background);
                unitPane.setPrefSize(stage.getMaxWidth(), stage.getMaxHeight());
                Image buttonImage2 = new Image(GameMenu.class.getResource("/Units/Fire Throwers2.png").toExternalForm());
                ImageView imageView2 = new ImageView(buttonImage2);
                Button fireThrower = new Button();
                fireThrower.setGraphic(imageView2);
                fireThrower.setStyle("-fx-background-color: transparent");
                unitPane.add(fireThrower, 1, 1);
                Image buttonImage3 = new Image(GameMenu.class.getResource("/Units/Knight2.png").toExternalForm());
                ImageView imageView3 = new ImageView(buttonImage3);
                Button knight = new Button();
                knight.setGraphic(imageView3);
                knight.setStyle("-fx-background-color: transparent");
                unitPane.add(knight, 2, 1);
                Image buttonImage4 = new Image(GameMenu.class.getResource("/Units/Swordsmen2.png").toExternalForm());
                ImageView imageView4 = new ImageView(buttonImage4);
                Button swordMan = new Button();
                swordMan.setGraphic(imageView4);
                swordMan.setStyle("-fx-background-color: transparent");
                unitPane.add(swordMan, 3, 1);
                Image buttonImage5 = new Image(GameMenu.class.getResource("/Units/Crossbowmen2.png").toExternalForm());
                ImageView imageView5 = new ImageView(buttonImage5);
                Button arabianBow = new Button();
                arabianBow.setGraphic(imageView5);
                arabianBow.setStyle("-fx-background-color: transparent");
                unitPane.add(arabianBow, 1, 2);
                Image buttonImage6 = new Image(GameMenu.class.getResource("/Units/Assassins2.png").toExternalForm());
                ImageView imageView6 = new ImageView(buttonImage6);
                Button assassin = new Button();
                assassin.setGraphic(imageView6);
                assassin.setStyle("-fx-background-color: transparent");
                unitPane.add(assassin, 2, 2);
                Image buttonImage7 = new Image(GameMenu.class.getResource("/Units/Slingers2.png").toExternalForm());
                ImageView imageView7 = new ImageView(buttonImage7);
                Button slinger = new Button();
                slinger.setGraphic(imageView7);
                slinger.setStyle("-fx-background-color: transparent");
                unitPane.add(slinger, 3, 2);
                Label label = new Label("Enter your mapCell's component");
                label.setStyle("-fx-font-size: 20;-fx-text-fill: white");
                label.setAlignment(Pos.BOTTOM_RIGHT);
                VBox vBox = new VBox(label);
                unitPane.add(vBox, 2, 3);
                TextField xTextField = new TextField();
                xTextField.setPromptText("x component");
                xTextField.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #cccccc;-fx-border-radius: 5;" +
                        "-fx-padding: 5;-fx-font-size: 14");
                unitPane.add(xTextField, 1, 4);
                TextField yTextField = new TextField();
                yTextField.setPromptText("y component");
                yTextField.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #cccccc;-fx-border-radius: 5;" +
                        "-fx-padding: 5;-fx-font-size: 14");
                unitPane.add(yTextField,3,4);
                unitPane.setHgap(5);
                unitPane.setVgap(40);
                unitPane.setAlignment(Pos.TOP_RIGHT);
                fireThrower.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Fire Throwers", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Fire Throwers", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(30);
                                imageView.setFitHeight(30);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });
                knight.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Knight", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Knight", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(55);
                                imageView.setFitHeight(55);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });
                swordMan.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Swordsmen", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Swordsmen", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(45);
                                imageView.setFitHeight(45);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });
                arabianBow.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Crossbowmen", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Crossbowmen", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(60);
                                imageView.setFitHeight(60);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });
                assassin.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Assassins", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Assassins", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });
                slinger.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(xTextField.getText().equals("") || yTextField.getText().equals("")){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong input");
                            alert.setContentText("You should enter your components");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            int xLocation = Integer.parseInt(xTextField.getText());
                            int yLocation = Integer.parseInt(yTextField.getText());
                            MenuMessages messages = dropUnitToController(xLocation,
                                    yLocation, "Slingers", stage);
                            if(messages.equals(MenuMessages.SUCCESS)){
                                stage.setScene(scene);
                                stage.setFullScreen(true);
                                Unit unit = Unit.getInstance("Slingers", xLocation, yLocation);
                                ImageView imageView = new ImageView(new Image(unit.getImageView().toExternalForm()));
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                addInMap(imageView, xLocation, yLocation);
                                unitsInMap.put(unit, imageView);
                            }
                        }
                    }
                });

                scene2.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
                stage.setScene(scene2);
                stage.setFullScreen(true);
                stage.show();
            }
        });
    }
    public static MenuMessages dropUnitToController(int x, int y, String type, Stage stage) {
        boolean godMode = true;//TODO:make god mode false for game
        MenuMessages message = GameMenuController.dropUnit(x,y, type, godMode);
        switch (message) {
            case INVALID_LOCATION:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Location");
                alert.setContentText("Location is out of bound");
                alert.initOwner(stage);
                alert.showAndWait();
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error");
                alert2.setHeaderText("Texture");
                alert2.setContentText("The cell's texture is incompatible");
                alert2.initOwner(stage);
                alert2.showAndWait();
                break;
            case NOT_ENOUGH_RESOURCES:
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Error");
                alert3.setHeaderText("Resource");
                alert3.setContentText("Your government does not have enough resources");
                alert3.initOwner(stage);
                alert3.showAndWait();
                break;
            case SUCCESS:
                Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION);
                alert4.setTitle("Unit");
                alert4.setHeaderText("Unit dropped");
                alert4.setContentText("Done!");
                alert4.initOwner(stage);
                alert4.showAndWait();
                break;
        }
        return message;
    }
    public static void showAlert(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void pressedNode(GridPane gridPane, Stage stage){
        for (Node node: gridPane.getChildren()){
            node.setOnMousePressed(event -> {
                if(rectangle != null){
                    removeInMap(rectangle, chosenX, chosenY);
                }
                int i = gridPane.getChildren().indexOf(node) % 200;
                int j = gridPane.getChildren().indexOf(node) / 200;
                rectangle = new Rectangle(60, 60, Color.TRANSPARENT);
                rectangle.setFill(Color.rgb( 96, 96,217));
                rectangle.setOpacity(0.5);
                chosenX = i;
                chosenY = j;
                addInMap(rectangle, i, j);
                scrollPane.setOnKeyPressed(keyEvent ->{
                    if(keyEvent.getCode() == KeyCode.M){
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.initOwner(stage);
                        dialog.setTitle("Move Units");
                        dialog.setHeaderText("Unit type and destination components");
                        dialog.setContentText("Enter your x and y components");
                        TextField xTextField = new TextField();
                        xTextField.setPromptText("x component");
                        TextField yTextField = new TextField();
                        yTextField.setPromptText("y component");
                        TextField unitType = new TextField();
                        unitType.setPromptText("unit type");
                        TextField numberOfUnit = new TextField();
                        numberOfUnit.setPromptText("unit's number");
                        GridPane dialogGridPane = new GridPane();
                        dialogGridPane.add(new Label("x:"), 0, 0);
                        dialogGridPane.add(xTextField, 1, 0);
                        dialogGridPane.add(new Label("y:"), 0, 1);
                        dialogGridPane.add(yTextField, 1, 1);
                        dialogGridPane.add(new Label("type:"), 0, 2);
                        dialogGridPane.add(unitType, 1, 2);
                        dialogGridPane.add(new Label("number:"), 0 ,3);
                        dialogGridPane.add(numberOfUnit, 1, 3);
                        dialogGridPane.setHgap(40);
                        dialog.getDialogPane().setContent(dialogGridPane);
                        dialog.getDialogPane().getStylesheets().add
                                (GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
                        Optional<String> result = dialog.showAndWait();
                        if(result.isPresent()){
                            String x = xTextField.getText();
                            String y = yTextField.getText();
                            String type = unitType.getText();
                            String number = numberOfUnit.getText();
                            if(!checkStringsAreNumbers(x, y, number)){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Inputs Error");
                                alert.setContentText("Your x and y components should be numbers");
                                alert.initOwner(stage);
                                alert.showAndWait();
                            }
                            else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if(messages == MenuMessages.SUCCESS){
                                    MenuMessages message = moveUnit(Integer.parseInt(x), Integer.parseInt(y));
                                    if(message.equals(MenuMessages.SUCCESS)){
                                        ArrayList <Unit> units = Database.getMapById(1).getCell(i, j).getUnits();//TODO:change with map
                                        int unitsNumber = Integer.parseInt(number);
                                        int counter = 0;
                                        ArrayList<Unit> movingUnits = new ArrayList<>();
                                        for(Unit unit: units){
                                            if(unit.unitName.name.equals(type)){
                                                counter++;
                                                movingUnits.add(unit);
                                            }
                                            if(counter == unitsNumber){
                                                break;
                                            }
                                        }
                                        if(unitsNumber != counter){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Inputs Error");
                                            alert.setContentText("Over limited number of unit");
                                            alert.initOwner(stage);
                                            alert.showAndWait();
                                        }else {
                                            for(Unit unit : movingUnits){
                                                ImageView imageView = unitsInMap.get(unit);
                                                TranslateTransition translateTransition =
                                                        new TranslateTransition(Duration.seconds(2), imageView);
                                                translateTransition.setFromX(chosenX);
                                                translateTransition.setFromY(chosenY);
                                                translateTransition.setToX(Integer.parseInt(x));
                                                translateTransition.setToY(Integer.parseInt(y));
                                                translateTransition.play();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (keyEvent.getCode() == KeyCode.A) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.initOwner(stage);
                        dialog.setTitle("Melee Attack");
                        dialog.setHeaderText("Unit type for melee attack and destination components");
                        dialog.setContentText("Enter your x and y components and unit type");
                        TextField xTextField = new TextField();
                        xTextField.setPromptText("x component");
                        TextField yTextField = new TextField();
                        yTextField.setPromptText("y component");
                        TextField unitType = new TextField();
                        unitType.setPromptText("unit type");
                        TextField numberOfUnit = new TextField();
                        numberOfUnit.setPromptText("unit's number");
                        GridPane dialogGridPane = new GridPane();
                        dialogGridPane.add(new Label("x:"), 0, 0);
                        dialogGridPane.add(xTextField, 1, 0);
                        dialogGridPane.add(new Label("y:"), 0, 1);
                        dialogGridPane.add(yTextField, 1, 1);
                        dialogGridPane.add(new Label("type:"), 0, 2);
                        dialogGridPane.add(unitType, 1, 2);
                        dialogGridPane.add(new Label("number:"), 0 ,3);
                        dialogGridPane.add(numberOfUnit, 1, 3);
                        dialogGridPane.setHgap(40);
                        dialog.getDialogPane().setContent(dialogGridPane);
                        dialog.getDialogPane().getStylesheets().add
                                (GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
                        Optional<String> result = dialog.showAndWait();
                        if(result.isPresent()){
                            String x = xTextField.getText();
                            String y = yTextField.getText();
                            String type = unitType.getText();
                            String number = numberOfUnit.getText();
                            if(!checkStringsAreNumbers(x, y, number)){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Inputs Error");
                                alert.setContentText("Your x and y components should be number");
                                alert.showAndWait();
                            }
                            else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if(messages == MenuMessages.SUCCESS){
                                    MenuMessages message = moveUnit(Integer.parseInt(x), Integer.parseInt(y));
                                    //todo:change logic of attack
                                    if(message == MenuMessages.SUCCESS){
                                        ArrayList <Unit> units = Database.getMapById(1).getCell(i, j).getUnits();//TODO:change with map
                                        int unitsNumber = Integer.parseInt(number);
                                        int counter = 0;
                                        ArrayList<Unit> movingUnits = new ArrayList<>();
                                        for(Unit unit: units){
                                            if(unit.unitName.name.equals(type)){
                                                counter++;
                                                movingUnits.add(unit);
                                            }
                                            if(counter == unitsNumber){
                                                break;
                                            }
                                        }
                                        if(unitsNumber != counter){
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Inputs Error");
                                            alert.setContentText("Over limited number of unit");
                                            alert.initOwner(stage);
                                            alert.showAndWait();
                                        }else {
                                            for(Unit unit : movingUnits){
                                                ImageView imageView = unitsInMap.get(unit);
                                                TranslateTransition translateTransition =
                                                        new TranslateTransition(Duration.seconds(2), imageView);
                                                translateTransition.setFromX(chosenX);
                                                translateTransition.setFromY(chosenY);
                                                translateTransition.setToX(Integer.parseInt(x));
                                                translateTransition.setToY(Integer.parseInt(y));
                                                translateTransition.play();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (keyEvent.getCode() == KeyCode.R) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.initOwner(stage);
                        dialog.setTitle("Ranged Attack");
                        dialog.setHeaderText("Unit type for ranged attack and destination components");
                        dialog.setContentText("Enter your x and y components and unit type");
                        TextField xTextField = new TextField();
                        TextField yTextField = new TextField();
                        TextField unitType = new TextField();
                        GridPane dialogGridPane = new GridPane();
                        dialogGridPane.add(new Label("x:"), 0, 0);
                        dialogGridPane.add(xTextField, 1, 0);
                        dialogGridPane.add(new Label("y:"), 0, 1);
                        dialogGridPane.add(yTextField, 1, 1);
                        dialogGridPane.add(new Label("type:"), 0, 2);
                        dialogGridPane.add(unitType, 1, 2);
                        dialogGridPane.setHgap(40);
                        dialog.getDialogPane().setContent(dialogGridPane);
                        dialog.getDialogPane().getStylesheets().add
                                (GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
                        Optional<String> result = dialog.showAndWait();
                        if(result.isPresent()){
                            String x = xTextField.getText();
                            String y = yTextField.getText();
                            String type = unitType.getText();
                            if(!checkStringsAreNumbers(x, y)){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Inputs Error");
                                alert.setContentText("Your x and y components should be number");
                                alert.showAndWait();
                            }
                            else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if(messages == MenuMessages.SUCCESS){
                                    moveUnit(Integer.parseInt(x), Integer.parseInt(y));
                                }
                            }
                        }
                    } else if (keyEvent.getCode() == KeyCode.P) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.initOwner(stage);
                        dialog.setTitle("Patrol units");
                        dialog.setHeaderText("Unit type and destination components");
                        dialog.setContentText("Enter your start and final component");
                        TextField firstX = new TextField();
                        firstX.setPromptText("first x");
                        TextField lastX = new TextField();
                        lastX.setPromptText("final x");
                        TextField firstY = new TextField();
                        firstY.setPromptText("first y");
                        TextField lastY = new TextField();
                        lastY.setPromptText("final y");
                        TextField unitType = new TextField();
                        unitType.setPromptText("unit type");
                        GridPane dialogGridPane = new GridPane();
                        dialogGridPane.add(new Label("x:"), 0, 0);
                        dialogGridPane.add(firstX, 1, 0);
                        dialogGridPane.add(new Label("x2:"), 0, 1);
                        dialogGridPane.add(lastX, 1, 1);
                        dialogGridPane.add(new Label("y:"), 0, 2);
                        dialogGridPane.add(firstY, 1, 2);
                        dialogGridPane.add(new Label("y2:"), 0, 3);
                        dialogGridPane.add(lastY, 1, 3);
                        dialogGridPane.add(new Label("type:"), 0, 4);
                        dialogGridPane.add(unitType, 1, 4);
                        dialogGridPane.setHgap(40);
                        dialog.getDialogPane().setContent(dialogGridPane);
                        dialog.getDialogPane().getStylesheets().add
                                (GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
                        Optional<String> result = dialog.showAndWait();
                        if(result.isPresent()){
                            String x = firstX.getText();
                            String y = firstY.getText();
                            String x2 = lastX.getText();
                            String y2 = lastY.getText();
                            String type = unitType.getText();
                            if(!checkStringsAreNumbers(x, y, x2, y2)){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Inputs Error");
                                alert.setContentText("Your entered component should be numbers");
                                alert.initOwner(stage);
                                alert.showAndWait();
                            }
                            else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if(messages == MenuMessages.SUCCESS){
                                    MenuMessages message = patrolUnit(Integer.parseInt(x), Integer.parseInt(y),
                                            Integer.parseInt(x2), Integer.parseInt(y2));
                                    if(message.equals(MenuMessages.SUCCESS)){
                                        ArrayList <Unit> units = Database.getMapById(1).getCell(i, j).getUnits();//TODO:change with map
                                        for(Unit unit : units){
                                            if(unit.unitName.name.equals(type)){
                                                ImageView imageView = unitsInMap.get(unit);
                                                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), imageView);
                                                translateTransition.setFromX(Integer.parseInt(x));
                                                translateTransition.setFromY(Integer.parseInt(y));
                                                translateTransition.setToX(Integer.parseInt(x2));
                                                translateTransition.setToY(Integer.parseInt(y2));
                                                translateTransition.setCycleCount(-1);
                                                translateTransition.setAutoReverse(true);
                                                translateTransition.play();
                                                transition.add(translateTransition);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else if (keyEvent.getCode() == KeyCode.H) {
                        if(transition.size() == 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Input Error");
                            alert.setContentText("There is no patrolling unit");
                            alert.initOwner(stage);
                            alert.showAndWait();
                        }else {
                            GameEntityController.halt();
                            TranslateTransition transition1 = transition.get(0);
                            transition1.stop();
                            transition.remove(transition1);
                        }
                    }
                });
            });
        }
    }
    public static MenuMessages patrolUnit(int fromX, int fromY, int toX ,int toY) {
        MenuMessages messages = GameEntityController.patrolUnit(fromX, fromY, toX, toY);
        switch (messages) {
            case INVALID_LOCATION:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error");
                alert2.setHeaderText("Invalid Location");
                alert2.setContentText("Error: At least one location is out of bounds.");
                alert2.showAndWait();
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Location");
                alert.setContentText("Error: At least one destination is unreachable due to its texture.");
                break;
            case SUCCESS:
                AbstractMenu.show("Success. Unit will start patrolling at the end of the turn.");
                AbstractMenu.show("You can use 'unit halt' to stop the unit's movement.");
                break;
        }
        return messages;
    }

    public static boolean checkStringsAreNumbers(String... entrances) {
        Pattern patternForCheckStrIsNumber = Pattern.compile("-?\\d+$");
        for (String entrance : entrances) {
            if (entrance == null)
                return false;
            if (!patternForCheckStrIsNumber.matcher(entrance).matches())
                return false;
        }
        return true;
    }
    public static MenuMessages selectUnit(int x, int y, String type) {
        MenuMessages message = GameMenuController.selectUnit(x, y, type);
        switch (message) {
            case INVALID_TYPE:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error");
                alert2.setHeaderText("Unit type error");
                alert2.setContentText("Invalid unit type");
                alert2.showAndWait();
                break;
            case NO_MATCHING_UNIT:
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Error");
                alert3.setHeaderText("There ");
                alert3.setContentText("There is no user with this username!");
                alert3.showAndWait();
                break;
        }
        return message;
    }

    public static MenuMessages moveUnit(int x,int y) {
        MenuMessages messages = GameEntityController.moveUnitTo(x, y);
        switch (messages) {
            case INVALID_LOCATION:
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error");
                alert2.setHeaderText("Invalid location");
                alert2.setContentText("Entered location is out of bounds!");
                alert2.showAndWait();
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Error");
                alert3.setHeaderText("Cell texture error");
                alert3.setContentText("Cell texture is incompatible");
                alert3.showAndWait();
                break;
            case NO_REMAINING_MOVEMENT:
                Alert alert4 = new Alert(Alert.AlertType.ERROR);
                alert4.setTitle("Error");
                alert4.setHeaderText("Cell texture error");
                alert4.setContentText("This unit does not have any remaining movement.");
                alert4.showAndWait();
                break;
            case IS_PATROLLING:
                Alert alert5 = new Alert(Alert.AlertType.ERROR);
                alert5.setTitle("Error");
                alert5.setHeaderText("Patrolling unit");
                alert5.setContentText("Unit is patrolling.");
                alert5.showAndWait();
                break;
            case SUCCESS:
                Alert alert6 = new Alert(Alert.AlertType.CONFIRMATION);
                alert6.setTitle("Move");
                alert6.setHeaderText("Move unit");
                alert6.setContentText("Done!");
                alert6.showAndWait();
                break;
        }
        return messages;
    }

    private void showDetailWithDragClick(GridPane gridPane) {
        drag = false;
        unitNumbers = 0;
        buildings = new ArrayList<>();
        Rectangle draggedSection = new Rectangle();
        gridPane.setOnMousePressed(event -> {
            draggedSection.setX(event.getX());
            draggedSection.setY(event.getY());
        });
        gridPane.setOnMouseDragged(event -> {
            double x = Math.min(event.getX(), draggedSection.getX());
            double y = Math.min(event.getY(), draggedSection.getY());
            double width = Math.abs(event.getX() - draggedSection.getX());
            double height = Math.abs(event.getY() - draggedSection.getY());
            draggedSection.setX(x);
            draggedSection.setY(y);
            draggedSection.setWidth(width);
            draggedSection.setHeight(height);
            drag = true;
        });
        gridPane.setOnMouseReleased(event -> {
            for(Node node : gridPane.getChildren()){
                if(node.getBoundsInParent().intersects(draggedSection.getBoundsInParent())){
                    int i = gridPane.getChildren().indexOf(node) % 200;
                    int j = gridPane.getChildren().indexOf(node) / 200;
                    unitNumbers += Database.getMapById(1).getCell(i, j).getUnits().size();//TODO: after finishing game menu change 1
                    if(Database.getMapById(1).getCell(i,  j).getBuilding() != null){//TODO: after finishing game menu change 1
                        buildings.add(Database.getMapById(1).getCell(i, j).getBuilding());
                    }
                }
            }
            String popupText = "";
            for(Building building : buildings){
                popupText = popupText + building.getBuildingName();
            }
            if(drag) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Details");
                alert.setHeaderText("Dragged cells information");
                alert.setContentText("Units number:" + unitNumbers + "\nBuildings:" + popupText);
                alert.showAndWait();
            }
            drag = false;
        });
    }

    private void scrollRight() {
        double deltaX = 10.0;
        double width = scrollPane.getContent().getBoundsInLocal().getWidth();
        double hValue = scrollPane.getHvalue();
        scrollPane.setVvalue(hValue + deltaX / width);
    }

    private void scrollLeft() {
        double deltaX = -10.0;
        double width = scrollPane.getContent().getBoundsInLocal().getWidth();
        double hValue = scrollPane.getHvalue();
        scrollPane.setVvalue(hValue + deltaX / width);
    }

    private void scrollDown() {
        double deltaY = 10.0;
        double height = scrollPane.getContent().getBoundsInLocal().getHeight();
        double vValue = scrollPane.getVvalue();
        scrollPane.setVvalue(vValue + deltaY / height);
    }

    private void scrollUp() {
        double deltaY = -10.0;
        double height = scrollPane.getContent().getBoundsInLocal().getHeight();
        double vValue = scrollPane.getVvalue();
        scrollPane.setVvalue(vValue + deltaY / height);
    }

    public static void main(String[] args) {
        launch();
    }

    public void createMap(Map map) {
        HashMap<MapCell.Texture, ImagePattern> imagePatternHashMap = new HashMap<>();
        setImagePattern(imagePatternHashMap);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                Rectangle cell = new Rectangle(60, 60);
                cell.setFill(imagePatternHashMap.get(map.getCell(i, j).getTexture()));
                Group group = new Group(cell);
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Texture:" + map.getCell(i, j).getTexture().name());
                if(map.getCell(i, j).getBuilding() != null){
                    tooltip.setText(tooltip.getText() + "\nBuilding:"+ map.getCell(i, j).getBuilding());
                }
                if(map.getCell(i, j).getUnits() != null){
                    tooltip.setText(tooltip.getText() + "\nNumber of Units: " + map.getCell(i, j).getUnits().size());
                    for (Unit unit : map.getCell(i, j).getUnits()) {
                            tooltip.setText("\n" + tooltip.getText() + unit.unitName + "health : "+ unit.getHP()+
                                    "Melee damage : "+ unit.getMeleeDamage());
                    }
                }
                if(movingUnits(map.getCell(i, j).getUnits()).size() != 0){
                    tooltip.setText(tooltip.getText() + "\nMoving Unit:");
                    for(Unit unit : movingUnits(map.getCell(i, j).getUnits())){
                        tooltip.setText(tooltip.getText() + unit.unitName + " ");
                    }
                }
                Tooltip.install(group, tooltip);

                gridPane.add(group, i, j);
            }
        }
        ImageView imageView = new ImageView(GameMenu.class.getResource("/Menu/BoarderMenuOfGame.png")
                .toExternalForm());
        StackPane.setMargin(imageView, new Insets(700, 0, 0, 0));
        gamePane.getChildren().add(imageView);


    }

    private void setImagePattern(HashMap<MapCell.Texture, ImagePattern> imagePatternHashMap) {
        imagePatternHashMap.put(BEACH, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/BEACH.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(DEEP_WATER, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/DEEP_WATER.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(GRASS, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/GRASS.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(GRASSLAND, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/GRASSLAND.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(GRAVEL, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/GRAVEL.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(IRON, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/IRON.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(LAND, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/LAND.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(MEADOW, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/MEADOW.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(OIL, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/OIL.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(PLAIN, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/PLAIN.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(RIVER, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/RIVER.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(SHALLOW_WATER, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/SHALLOW_WATER.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(SLATE, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/SLATE.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(STONE, new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/STONE.jpg")
                .toExternalForm())));
    }


    public void addInMap(Node node, int i, int j) {
        Group group = (Group) gridPane.getChildren().get(gridPane.getRowCount() * j + i);
        group.getChildren().add(node);
    }

    public void removeInMap(Node node, int i, int j) {
        Group group = (Group) gridPane.getChildren().get(gridPane.getRowCount() * j + i);
        group.getChildren().remove(node);
    }

    public void setZoom(ScrollPane scrollPane) {
        Button zoomInButton = new Button();
        Button zoomOutButton = new Button();
        ImageView zoomInImage = new ImageView(GameMenu.class.getResource("/Icon/zoomIn.png").toExternalForm());
        ImageView zoomOutImage = new ImageView(GameMenu.class.getResource("/Icon/zoomOut.png").toExternalForm());
        zoomOutImage.setFitHeight(20);
        zoomOutImage.setFitWidth(20);
        zoomInImage.setFitHeight(20);
        zoomInImage.setFitWidth(20);
        zoomInButton.setGraphic(zoomInImage);
        zoomOutButton.setGraphic(zoomOutImage);
        zoomInButton.setOnAction(event -> {
            double currentScale = scrollPane.getScaleX();
            double newScale = currentScale + 0.1;
            if (newScale <= 1.5) {
                scrollPane.setScaleX(currentScale + 0.1);
                scrollPane.setScaleY(currentScale + 0.1);
            }


        });

        zoomOutButton.setOnAction(event -> {
            double currentScale = scrollPane.getScaleX();
            double newScale = currentScale - 0.1;
            if (newScale >= 0.8) {
                scrollPane.setScaleX(currentScale - 0.1);
                scrollPane.setScaleY(currentScale - 0.1);
            }

        });

        HBox zoomBox = new HBox(zoomInButton, zoomOutButton);
        zoomBox.setSpacing(6);
        StackPane.setMargin(zoomBox, new Insets(0, 0, 0, 1450));
        gamePane.getChildren().add(zoomBox);
    }
    public static ArrayList<Unit> movingUnits(ArrayList<Unit> units) {
        ArrayList <Unit> movingUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.isMoving()) {
                movingUnits.add(unit);
            }
        }
        return movingUnits;
    }

}
