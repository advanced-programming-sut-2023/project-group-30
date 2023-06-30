package view;

import controller.MainController;
import controller.menu_controllers.GameEntityController;
import controller.menu_controllers.GameMenuController;
import controller.messages.MenuMessages;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.attributes.Attribute;
import model.attributes.building_attributes.*;
import model.attributes.building_attributes.Process;
import model.game.Government;
import model.game.Game;
import model.game.game_entities.Building;
import model.game.game_entities.BuildingName;
import model.game.game_entities.Unit;
import model.game.game_entities.UnitName;
import model.game.map.Map;
import model.game.map.MapCell;
import view.menus.AbstractMenu;
import view.menus.AppMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import static model.enums.FileName.*;

import static model.game.game_entities.BuildingName.*;
import static model.game.map.MapCell.Texture.*;


public class GameMenu extends Application {
    private GridPane gridPane;
    private ScrollPane scrollPane;
    private static StackPane gamePane;
    private static HashMap<Enum, ImagePattern> imagePatternHashMap;
    private HashMap<Enum, ImageView> imageOfBuilding;


    private static Button textForPopularity = new Button();


    private ArrayList<Building> buildings;
    private int unitNumbers;
    private boolean drag;
    private Rectangle rectangle;
    private int chosenX;
    private int chosenY;

    @Override
    public void start(Stage stage) throws Exception {
        gridPane = new GridPane();
        gridPane.setVgap(-0.5);
        gridPane.setHgap(-0.5);
        //Database.loadData();
        //forTest();//Todo for test
        scrollPane = new ScrollPane(gridPane);
        scrollPane.setPrefViewportWidth(stage.getMaxWidth()); // 40 cells wide (20 pixels each)
        scrollPane.setPrefViewportHeight(stage.getMaxHeight()); // 40 cells high (20 pixels each)
        gamePane = new StackPane(scrollPane);
        Scene scene = new Scene(gamePane, stage.getMaxWidth(), stage.getMaxHeight());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                scrollUp();
            } else if (event.getCode() == KeyCode.DOWN) {
                scrollDown();
            } else if (event.getCode() == KeyCode.RIGHT) {
                scrollRight();
            } else if (event.getCode() == KeyCode.LEFT) {
                scrollLeft();
            } else if (event.getCode() == KeyCode.S) {
                setRates();
            } else if (event.getCode() == KeyCode.T) {
                TradeMenu tradeMenu = new TradeMenu();
                tradeMenu.run();
            } else if (event.getCode() == KeyCode.N) {
                nextTurn();
            }

        });
        gamePane.getStylesheets().add(GameMenu.class.getResource("/CSS/Style.css").toExternalForm());
        scrollPane.setPrefSize(stage.getMaxWidth(), stage.getMaxHeight());
        createMap(GameMenuController.getCurrentGame().getMap());//TODO: after finishing game menu change 1
        createMiniMap(2);

        scrollPane.setScaleX(3 * scrollPane.getScaleX() / 2);
        scrollPane.setScaleY(3 * scrollPane.getScaleY() / 2);
        setZoom(scrollPane);
        dropBuilding();

        stage.setScene(scene);
        showDetailWithDragClick(gridPane);
        pressedNode(gridPane, stage);
        stage.setFullScreen(true);
        stage.show();
    }

    private void nextTurn() {
        GameMenuController.endOnePlayersTurn();
        setGold();
        setPopularity();

    }

    private void pressedNode(GridPane gridPane, Stage stage) {
        for (Node node : gridPane.getChildren()) {
            node.setOnMousePressed(event -> {
                if (rectangle != null) {
                    removeInMap(rectangle, chosenX, chosenY);
                }
                int i = gridPane.getChildren().indexOf(node) % 200;
                int j = gridPane.getChildren().indexOf(node) / 200;
                rectangle = new Rectangle(60, 60, Color.TRANSPARENT);
                rectangle.setFill(Color.rgb(96, 96, 217));
                rectangle.setOpacity(0.5);
                chosenX = i;
                chosenY = j;
                addInMap(rectangle, i, j);

                scrollPane.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.M) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.initOwner(stage);
                        dialog.setTitle("Move Units");
                        dialog.setHeaderText("Unit type and destination components");
                        dialog.setContentText("Enter your x and y components");
//                        Optional<String> result = dialog.showAndWait();
//                        if(result.isPresent()) {
//                            dialog.setContentText("Enter your units type");
//                            Optional<String> result2 = dialog.showAndWait();
//
//                            if (result2.isPresent()) {
//                                String component = result.get();
//                                String types = result2.get();
//                                System.out.println(component+ types);
//                            }
//                        }
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
                        if (result.isPresent()) {
                            String x = xTextField.getText();
                            String y = yTextField.getText();
                            String type = unitType.getText();
                            if (!checkStringsAreNumbers(x, y)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Inputs Error");
                                alert.setContentText("Your x and y components should be numbers");
                                alert.showAndWait();
                            } else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if (messages == MenuMessages.SUCCESS) {

                                }
                            }
                        }
                    } else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V) {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        String buildingName = clipboard.getString();
                        createBuilding(getBuildingName(buildingName), i, j);
                        gamePane.getChildren().remove(clipboardPane);
                    }
                });
            });
        }
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
            case SUCCESS:
                Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION);
                alert4.setTitle("Move");
                alert4.setHeaderText("Unit moved");
                alert4.setContentText("Done!");
                alert4.showAndWait();
                break;
        }
        return message;
    }

    public static void moveUnit(int x, int y) {
        switch (GameEntityController.moveUnitTo(x, y)) {
            case INVALID_LOCATION:
                AbstractMenu.show("Error: Location out of bounds.");
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                AbstractMenu.show("Error: The destination is unreachable due to its texture.");
                break;
            case NO_REMAINING_MOVEMENT:
                AbstractMenu.show("Error: This unit does not have any remaining movement.");
                break;
            case IS_PATROLLING:
                AbstractMenu.show("Error: The Unit is currently patrolling.");
                AbstractMenu.show("You can use 'unit halt' to end its patrol.");
                break;
            case SUCCESS:
                AbstractMenu.show("Unit moved successfully.");
                break;
        }
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
            for (Node node : gridPane.getChildren()) {
                if (node.getBoundsInParent().intersects(draggedSection.getBoundsInParent())) {
                    int i = gridPane.getChildren().indexOf(node) % 200;
                    int j = gridPane.getChildren().indexOf(node) / 200;
                    unitNumbers += GameMenuController.getCurrentGame().getMap().getCell(i, j).getUnits().size();//TODO: after finishing game menu change 1
                    if (GameMenuController.getCurrentGame().getMap().getCell(i, j).getBuilding() != null) {//TODO: after finishing game menu change 1
                        buildings.add(GameMenuController.getCurrentGame().getMap().getCell(i, j).getBuilding());
                    }
                }
            }
            String popupText = "";
            for (Building building : buildings) {
                popupText = popupText + building.getBuildingName();
            }
            if (drag) {
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

//    //public static void main(String[] args) {
//        launch();
//    }


    public void createMap(Map map) {
        imagePatternHashMap = new HashMap<>();
        setImagePattern(imagePatternHashMap);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                Rectangle cell = new Rectangle(30, 30);
                cell.setFill(imagePatternHashMap.get(map.getCell(i, j).getTexture()));
                Pane pane = new Pane();
                pane.setPrefSize(30, 30);
                pane.getChildren().add(cell);
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Texture:" + map.getCell(i, j).getTexture().name());
                if (map.getCell(i, j).getBuilding() != null) {
                    tooltip.setText(tooltip.getText() + "\nBuilding:" + map.getCell(i, j).getBuilding());
                }
                if (map.getCell(i, j).getUnits() != null) {
                    tooltip.setText(tooltip.getText() + "\nNumber of Units: " + map.getCell(i, j).getUnits().size());
                    for (Unit unit : map.getCell(i, j).getUnits()) {
                        tooltip.setText("\n" + tooltip.getText() + unit.unitName + "health : " + unit.getHP() +
                                "Melee damage : " + unit.getMeleeDamage());
                    }
                }
                if (movingUnits(map.getCell(i, j).getUnits()).size() != 0) {
                    tooltip.setText(tooltip.getText() + "\nMoving Unit:");
                    for (Unit unit : movingUnits(map.getCell(i, j).getUnits())) {
                        tooltip.setText(tooltip.getText() + unit.unitName + " ");
                    }
                }
                Tooltip.install(pane, tooltip);
                gridPane.add(pane, i, j);
            }
        }
        ImageView imageView = new ImageView(GameMenu.class.getResource("/Menu/BoarderMenuOfGame.png")
                .toExternalForm());
        StackPane.setMargin(imageView, new Insets(700, 0, 0, 0));
        gamePane.getChildren().add(imageView);
        setGold();
        setPopularity();


    }

    private void createMiniMap(int mapId) {
        ImageView imageView = new ImageView(GameMenu.class.getResource("/miniMap/" + mapId + ".jpg")
                .toExternalForm());
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        StackPane.setMargin(imageView, new Insets(700, 0, -80, 100));
        gamePane.getChildren().add(imageView);
    }

    private void setImagePattern(HashMap<Enum, ImagePattern> imagePatternHashMap) {
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
        imagePatternHashMap.put(angryMask, new ImagePattern(new Image(GameMenu.class
                .getResource("/Icon/angryMask.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(ignoreMask, new ImagePattern(new Image(GameMenu.class
                .getResource("/Icon/ignoreMask.jpg")
                .toExternalForm())));
        imagePatternHashMap.put(happyMask, new ImagePattern(new Image(GameMenu.class
                .getResource("/Icon/happyMask.jpg")
                .toExternalForm())));

    }


    public void addInMap(Node node, int i, int j) {
        Pane pane = (Pane) gridPane.getChildren().get(gridPane.getRowCount() * j + i);
        pane.getChildren().add(node);

    }

    public void removeInMap(Node node, int i, int j) {
        Pane pane = (Pane) gridPane.getChildren().get(gridPane.getRowCount() * j + i);
        pane.getChildren().remove(node);
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
            if (newScale <= 3) {
                scrollPane.setScaleX(currentScale + 0.1);
                scrollPane.setScaleY(currentScale + 0.1);
            }


        });

        zoomOutButton.setOnAction(event -> {
            double currentScale = scrollPane.getScaleX();
            double newScale = currentScale - 0.1;
            if (newScale >= 0.9) {
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
        ArrayList<Unit> movingUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.isMoving()) {
                movingUnits.add(unit);
            }
        }
        return movingUnits;
    }

    private static Label labelForGold = new Label();

    public static void setGold() {//todo for next turn
        labelForGold.setText(GameMenuController.getCurrentGame().getCurrentGovernment().getGold() + "");
        labelForGold.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        labelForGold.getStyleClass().add("old-text");
        StackPane.setMargin(labelForGold, new Insets(780, 0, 0, 260));
        if (!gamePane.getChildren().contains(labelForGold))
            gamePane.getChildren().add(labelForGold);
    }
    private void setPopularity(){
        Label label = new Label(GameMenuController.getCurrentGame().getCurrentGovernment().getStaticPopularity() + "");
        label.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());
        label.getStyleClass().add("old-text");
        textForPopularity.setStyle("-fx-background-color: transparent;");
        textForPopularity.setGraphic(label);
        StackPane.setMargin(textForPopularity, new Insets(740, 0, 0, 270));
        if (!gamePane.getChildren().contains(textForPopularity))
            gamePane.getChildren().add(textForPopularity);
        textForPopularity.setOnAction(e -> showPopularity());

    }

    public static void showPopularity() {
        Stage showThePopularityFactor = new Stage();
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundImage(
                new Image(GameMenu.class.getResource("/Menu/popularityFactorsBackground.png").toExternalForm())
                , BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT
                , BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false))));
        showThePopularityFactor.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(pane);
        pane.setPrefSize(800, 300);
        setPopularityFactorsInPopup(pane);
        showThePopularityFactor.setScene(scene);
        showThePopularityFactor.showAndWait();
    }

    private static void setPopularityFactorsInPopup(Pane pane) {
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
        VBox vBox1 = new VBox(getMaskCondition(government.getFoodPopularity(), "Food")
                , getMaskCondition(government.getTaxPopularity(), "Tax"),
                getMaskCondition(government.getReligionPopularity(), "Religion"));
        VBox vBox2 = new VBox(getMaskCondition(government.getFearPopularity(), "Fear")
                , getMaskCondition(government.getOtherPopularity(), "Other"));
        HBox hBox = new HBox(vBox1, vBox2);
        vBox2.setSpacing(30);
        vBox1.setSpacing(30);
        hBox.setSpacing(60);
        hBox.setLayoutX(350);
        hBox.setLayoutY(80);
        pane.getChildren().add(hBox);
        HBox hBoxForAll = getMaskCondition(government.getFoodPopularity() + government.getTaxPopularity()
                + government.getReligionPopularity() + government.getFearPopularity()
                + government.getOtherPopularity(), null);
        hBoxForAll.setLayoutX(560);
        hBoxForAll.setLayoutY(255);
        pane.getChildren().add(hBoxForAll);
    }

    private static HBox getMaskCondition(double number, String name) {
        HBox hBox = new HBox();

        Label numberText = new Label(number + "");
        numberText.setStyle("-fx-font-family: Cardamon");
        numberText.setStyle("-fx-font-size: 20px");
        numberText.getStylesheets().add(GameMenu.class.getResource("/CSS/style.css").toExternalForm());

        hBox.getChildren().add(numberText);
        Rectangle rectangle = new Rectangle(25, 25);
        if (number == 0) {
            rectangle.setFill((imagePatternHashMap.get(ignoreMask)));
            numberText.setTextFill(Color.WHITE);
        } else if (number > 0) {
            rectangle.setFill(imagePatternHashMap.get(happyMask));
            numberText.setTextFill(Color.GREEN);
        } else {
            rectangle.setFill(imagePatternHashMap.get(angryMask));
            numberText.setTextFill(Color.RED);
        }
        hBox.getChildren().add(rectangle);
        Text textForName = new Text(name);
        textForName.setStyle("-fx-font-family: Cardamon");
        textForName.setStyle("-fx-font-size: 20px");
        hBox.getChildren().add(textForName);
        hBox.setSpacing(10);
        return hBox;
    }

    private void setRates() {
        Stage setRates = new Stage();
        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(new Background(new BackgroundImage(
                new Image(GameMenu.class.getResource("/Menu/setRates.jpg").toExternalForm())
                , BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT
                , BackgroundPosition.CENTER
                , new BackgroundSize(1.0, 1.0, true, true
                , false, false))));
        setRates.initModality(Modality.APPLICATION_MODAL);
        setSliderForRates(borderPane);
        Scene scene = new Scene(borderPane);
        borderPane.setPrefSize(800, 800);
        setRates.setScene(scene);
        setRates.showAndWait();

    }

    private void setSliderForRates(BorderPane borderPane) {
        VBox vBox = new VBox();
        Label fearRate = new Label("Fear Rate");
        Label foodRate = new Label("Food Rate");
        Label taxRate = new Label("Tax Rate");
        vBox.getChildren().addAll(fearRate, getFearSlider(), foodRate, setFoodAndTaxRate(-2, 2
                        , GameMenuController.getCurrentGame().getCurrentGovernment().getFoodRate())
                , taxRate, setFoodAndTaxRate(-3, 8, GameMenuController.getCurrentGame()
                        .getCurrentGovernment().getTaxRate()));
        vBox.setAlignment(Pos.CENTER);
        borderPane.getStylesheets().add(GameMenu.class.getResource("/CSS/Slider.css").toExternalForm());
        borderPane.setCenter(vBox);


    }

    private VBox getFearSlider() {
        double min = -5;
        double max = 5;
        Slider slider = new Slider(min, max, GameMenuController.getCurrentGame().getCurrentGovernment().getFearRate());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(10);
        slider.setMinWidth(200);

        Label valueLabel = new Label(0 + "");
        valueLabel.setAlignment(Pos.CENTER);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double roundedValue = Math.round(newValue.doubleValue() * 10.0) / 10.0;
            valueLabel.setText(roundedValue + "");
            GameMenuController.setFearRate(roundedValue);
        });
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(slider, valueLabel);
        return root;
    }

    private VBox setFoodAndTaxRate(int min, int max, int value) {
        Label label = new Label(value + "");
        Slider slider = new Slider(min, max, value);
        slider.setBlockIncrement(1);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMinWidth(200);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            label.setText(Integer.toString(newVal.intValue()));
            if (min == -3)
                GameMenuController.taxRate(newVal.intValue());//min -3 for taxRate
            else GameMenuController.foodRate(newVal.intValue());
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(slider, label);
        return root;

    }

    private void dropBuilding() {
        ScrollPane scrollOfBuilding = new ScrollPane();
        scrollOfBuilding.setStyle("-fx-background: #F5DEB3; -fx-border-color: #F5DEB3;");

        scrollOfBuilding.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollOfBuilding.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Button buttonForCastle = new Button();
        Button buttonForFarm = new Button();
        Button buttonForTree = new Button();
        Button buttonForWeapon = new Button();
        buttonForCastle.setBackground(null);
        buttonForTree.setBackground(null);
        buttonForWeapon.setBackground(null);
        buttonForFarm.setBackground(null);
        ImageView imageViewForCastle = new ImageView(new Image(GameMenu.class.getResource("/Icon/Castle.jpg")
                .toExternalForm()));
        ImageView imageViewForFarm = new ImageView(new Image(GameMenu.class.getResource("/Icon/Farm.jpg")
                .toExternalForm()));
        ImageView imageViewForTree = new ImageView(new Image(GameMenu.class.getResource("/Icon/Tree.jpg")
                .toExternalForm()));
        ImageView imageViewForWeapon = new ImageView(new Image(GameMenu.class.getResource("/Icon/Weapon.jpg")
                .toExternalForm()));
        imageViewForCastle.setFitWidth(30);
        imageViewForCastle.setFitHeight(30);
        imageViewForFarm.setFitWidth(30);
        imageViewForFarm.setFitHeight(30);
        imageViewForTree.setFitWidth(30);
        imageViewForTree.setFitHeight(30);
        imageViewForWeapon.setFitWidth(30);
        imageViewForWeapon.setFitHeight(30);
        buttonForCastle.setGraphic(imageViewForCastle);
        buttonForFarm.setGraphic(imageViewForFarm);
        buttonForTree.setGraphic(imageViewForTree);
        buttonForWeapon.setGraphic(imageViewForWeapon);
        StackPane.setMargin(buttonForCastle, new Insets(660, 680, 0, 0));
        gamePane.getChildren().add(buttonForCastle);
        StackPane.setMargin(buttonForFarm, new Insets(660, 600, 0, 0));
        gamePane.getChildren().add(buttonForFarm);
        StackPane.setMargin(buttonForTree, new Insets(660, 520, 0, 0));
        gamePane.getChildren().add(buttonForTree);
        StackPane.setMargin(buttonForWeapon, new Insets(660, 440, 0, 0));
        gamePane.getChildren().add(buttonForWeapon);
        buttonForFarm.setOnAction(e -> showFarmScroll(scrollOfBuilding));
        buttonForCastle.setOnAction(e -> showCastleScroll(scrollOfBuilding));
        buttonForTree.setOnAction(e -> showTreeScroll(scrollOfBuilding));
        buttonForWeapon.setOnAction(e -> showWeaponScroll(scrollOfBuilding));
        //StackPane.setMargin(scrollOfBuilding, new Insets(780, 680, 5, 410));
        StackPane.setMargin(scrollOfBuilding, new Insets(780, 780, 5, 410));
        gamePane.getChildren().add(scrollOfBuilding);
    }

    private void setDragOnBuilding(Button button, BuildingName buildingName) {
        button.setOnDragDetected(event -> {
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(button.getText());
            db.setContent(content);
            event.consume();
        });
        gridPane.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        gridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {


                Node node = (Node) event.getSource();
                if (node instanceof GridPane) {
                    double dropX = event.getX();
                    double dropY = event.getY();
                    Node droppedOnNode = null;
                    int sum = 0;
                    for (Node node1 : gridPane.getChildren()) {
                        Bounds bounds = node1.getBoundsInParent();
                        if (dropX >= bounds.getMinX() && dropX <= bounds.getMaxX() &&
                                dropY >= bounds.getMinY() && dropY <= bounds.getMaxY()) {
                            droppedOnNode = node1;
                            break;
                        }
                        sum++;
                    }
                    createBuilding(buildingName, sum % 200, sum / 200);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

    }

    private void showWeaponScroll(ScrollPane scrollPane1) {
        Button buttonForSHOP = new Button();
        buttonForSHOP.setGraphic(getImageOFBuildingWithName(BuildingName.SHOP));
        buttonForSHOP.setStyle("-fx-background-color: transparent;");
        buttonForSHOP.setOnAction(e -> setDragOnBuilding(buttonForSHOP, SHOP));

        Button buttonForIRON_MINE = new Button();
        buttonForIRON_MINE.setGraphic(getImageOFBuildingWithName(BuildingName.IRON_MINE));
        buttonForIRON_MINE.setStyle("-fx-background-color: transparent;");
        buttonForIRON_MINE.setOnAction(e -> setDragOnBuilding(buttonForIRON_MINE, IRON_MINE));

        Button buttonForSTONE_MINE = new Button();
        buttonForSTONE_MINE.setGraphic(getImageOFBuildingWithName(BuildingName.STONE_MINE));
        buttonForSTONE_MINE.setStyle("-fx-background-color: transparent;");
        buttonForSTONE_MINE.setOnAction(e -> setDragOnBuilding(buttonForSTONE_MINE, STONE_MINE));

        Button buttonForSTORE = new Button();
        buttonForSTORE.setGraphic(getImageOFBuildingWithName(BuildingName.STORE));
        buttonForSTORE.setStyle("-fx-background-color: transparent;");
        buttonForSTORE.setOnAction(e -> setDragOnBuilding(buttonForSTORE, STORE));

        Button buttonForWOOD_CUTTER = new Button();
        buttonForWOOD_CUTTER.setGraphic(getImageOFBuildingWithName(BuildingName.WOOD_CUTTER));
        buttonForWOOD_CUTTER.setStyle("-fx-background-color: transparent;");
        buttonForWOOD_CUTTER.setOnAction(e -> setDragOnBuilding(buttonForWOOD_CUTTER, WOOD_CUTTER));

        Button buttonForARMOR = new Button();
        buttonForARMOR.setGraphic(getImageOFBuildingWithName(BuildingName.ARMOR));
        buttonForARMOR.setStyle("-fx-background-color: transparent;");
        buttonForARMOR.setOnAction(e -> setDragOnBuilding(buttonForARMOR, ARMOR));

        Button buttonForBLACKSMITH = new Button();
        buttonForBLACKSMITH.setGraphic(getImageOFBuildingWithName(BuildingName.BLACKSMITH));
        buttonForBLACKSMITH.setStyle("-fx-background-color: transparent;");
        buttonForBLACKSMITH.setOnAction(e -> setDragOnBuilding(buttonForBLACKSMITH, BLACKSMITH));

        Button buttonForPOLETURNER = new Button();
        buttonForPOLETURNER.setGraphic(getImageOFBuildingWithName(BuildingName.POLETURNER));
        buttonForPOLETURNER.setStyle("-fx-background-color: transparent;");
        buttonForPOLETURNER.setOnAction(e -> setDragOnBuilding(buttonForPOLETURNER, POLETURNER));

        Button buttonForFLETCHER = new Button();
        buttonForFLETCHER.setGraphic(getImageOFBuildingWithName(BuildingName.FLETCHER));
        buttonForFLETCHER.setStyle("-fx-background-color: transparent;");
        buttonForFLETCHER.setOnAction(e -> setDragOnBuilding(buttonForFLETCHER, FLETCHER));

        HBox content = new HBox();
        content.setPrefSize(200, 40);
        content.getChildren().addAll(buttonForSHOP, buttonForSTONE_MINE, buttonForSTORE, buttonForIRON_MINE
                , buttonForWOOD_CUTTER, buttonForBLACKSMITH, buttonForARMOR, buttonForFLETCHER, buttonForPOLETURNER);
        scrollPane1.setContent(content);
        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);

    }

    private void showTreeScroll(ScrollPane scrollPane1) {
        Button buttonForCHERRY_TREE = new Button();
        buttonForCHERRY_TREE.setGraphic(getImageOFBuildingWithName(BuildingName.CHERRY_TREE));
        buttonForCHERRY_TREE.setStyle("-fx-background-color: transparent;");
        buttonForCHERRY_TREE.setOnAction(e -> setDragOnBuilding(buttonForCHERRY_TREE, CHERRY_TREE));

        Button buttonForOLIVE_TREE = new Button();
        buttonForOLIVE_TREE.setGraphic(getImageOFBuildingWithName(BuildingName.OLIVE_TREE));
        buttonForOLIVE_TREE.setStyle("-fx-background-color: transparent;");
        buttonForOLIVE_TREE.setOnAction(e -> setDragOnBuilding(buttonForOLIVE_TREE, OLIVE_TREE));

        Button buttonForCOCONUT_TREE = new Button();
        buttonForCOCONUT_TREE.setGraphic(getImageOFBuildingWithName(BuildingName.COCONUT_TREE));
        buttonForCOCONUT_TREE.setStyle("-fx-background-color: transparent;");
        buttonForCOCONUT_TREE.setOnAction(e -> setDragOnBuilding(buttonForCOCONUT_TREE, COCONUT_TREE));

        Button buttonForDATE_TREE = new Button();
        buttonForDATE_TREE.setGraphic(getImageOFBuildingWithName(BuildingName.DATE_TREE));
        buttonForDATE_TREE.setStyle("-fx-background-color: transparent;");
        buttonForDATE_TREE.setOnAction(e -> setDragOnBuilding(buttonForDATE_TREE, DATE_TREE));

        Button buttonForDESERT_SHRUB = new Button();
        buttonForDESERT_SHRUB.setGraphic(getImageOFBuildingWithName(BuildingName.DESERT_SHRUB));
        buttonForDESERT_SHRUB.setStyle("-fx-background-color: transparent;");
        buttonForDESERT_SHRUB.setOnAction(e -> setDragOnBuilding(buttonForDESERT_SHRUB, DESERT_SHRUB));

        HBox content = new HBox();
        content.setPrefSize(200, 40);
        content.getChildren().addAll(buttonForDATE_TREE, buttonForCHERRY_TREE, buttonForCOCONUT_TREE, buttonForDESERT_SHRUB
                , buttonForOLIVE_TREE);
        scrollPane1.setContent(content);
        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);

    }

    private void showCastleScroll(ScrollPane scrollPane1) {
        Button buttonForKeep = new Button();
        buttonForKeep.setGraphic(getImageOFBuildingWithName(BuildingName.KEEP));
        buttonForKeep.setStyle("-fx-background-color: transparent;");
        buttonForKeep.setOnAction(e -> setDragOnBuilding(buttonForKeep, KEEP));

        Button buttonForSMALL_GATEHOUSE = new Button();
        buttonForSMALL_GATEHOUSE.setGraphic(getImageOFBuildingWithName(BuildingName.SMALL_GATEHOUSE));
        buttonForSMALL_GATEHOUSE.setStyle("-fx-background-color: transparent;");
        buttonForSMALL_GATEHOUSE.setOnAction(e -> setDragOnBuilding(buttonForSMALL_GATEHOUSE, SMALL_GATEHOUSE));

        Button buttonForBIG_GATEHOUSE = new Button();
        buttonForBIG_GATEHOUSE.setGraphic(getImageOFBuildingWithName(BuildingName.BIG_GATEHOUSE));
        buttonForBIG_GATEHOUSE.setStyle("-fx-background-color: transparent;");
        buttonForBIG_GATEHOUSE.setOnAction(e -> setDragOnBuilding(buttonForBIG_GATEHOUSE, BIG_GATEHOUSE));

        Button buttonForLOOKOUT_TOWER = new Button();
        buttonForLOOKOUT_TOWER.setGraphic(getImageOFBuildingWithName(BuildingName.LOOKOUT_TOWER));
        buttonForLOOKOUT_TOWER.setStyle("-fx-background-color: transparent;");
        buttonForLOOKOUT_TOWER.setOnAction(e -> setDragOnBuilding(buttonForLOOKOUT_TOWER, LOOKOUT_TOWER));

        Button buttonForPERIMETER_TOWER = new Button();
        buttonForPERIMETER_TOWER.setGraphic(getImageOFBuildingWithName(BuildingName.PERIMETER_TOWER));
        buttonForPERIMETER_TOWER.setStyle("-fx-background-color: transparent;");
        buttonForPERIMETER_TOWER.setOnAction(e -> setDragOnBuilding(buttonForPERIMETER_TOWER, PERIMETER_TOWER));

        Button buttonForDEFENCE_TURRET = new Button();
        buttonForDEFENCE_TURRET.setGraphic(getImageOFBuildingWithName(BuildingName.DEFENCE_TURRET));
        buttonForDEFENCE_TURRET.setStyle("-fx-background-color: transparent;");
        buttonForDEFENCE_TURRET.setOnAction(e -> setDragOnBuilding(buttonForDEFENCE_TURRET, DEFENCE_TURRET));

        Button buttonForSQUARE_TOWER = new Button();
        buttonForSQUARE_TOWER.setGraphic(getImageOFBuildingWithName(BuildingName.SQUARE_TOWER));
        buttonForSQUARE_TOWER.setStyle("-fx-background-color: transparent;");
        buttonForSQUARE_TOWER.setOnAction(e -> setDragOnBuilding(buttonForSQUARE_TOWER, SQUARE_TOWER));

        Button buttonForROUND_TOWER = new Button();
        buttonForROUND_TOWER.setGraphic(getImageOFBuildingWithName(BuildingName.ROUND_TOWER));
        buttonForROUND_TOWER.setStyle("-fx-background-color: transparent;");
        buttonForROUND_TOWER.setOnAction(e -> setDragOnBuilding(buttonForROUND_TOWER, ROUND_TOWER));

        Button buttonForBARRACKS = new Button();
        buttonForBARRACKS.setGraphic(getImageOFBuildingWithName(BuildingName.BARRACKS));
        buttonForBARRACKS.setStyle("-fx-background-color: transparent;");
        buttonForBARRACKS.setOnAction(e -> setDragOnBuilding(buttonForBARRACKS, BARRACKS));

        Button buttonForARMORY = new Button();
        buttonForARMORY.setGraphic(getImageOFBuildingWithName(BuildingName.ARMORY));
        buttonForARMORY.setStyle("-fx-background-color: transparent;");
        buttonForARMORY.setOnAction(e -> setDragOnBuilding(buttonForARMORY, ARMORY));

        Button buttonForMERCENARY_POST = new Button();
        buttonForMERCENARY_POST.setGraphic(getImageOFBuildingWithName(BuildingName.MERCENARY_POST));
        buttonForMERCENARY_POST.setStyle("-fx-background-color: transparent;");
        buttonForMERCENARY_POST.setOnAction(e -> setDragOnBuilding(buttonForMERCENARY_POST, MERCENARY_POST));

        HBox content = new HBox();
        content.setPrefSize(200, 40);
        content.getChildren().addAll(buttonForKeep, buttonForSMALL_GATEHOUSE, buttonForBIG_GATEHOUSE
                , buttonForLOOKOUT_TOWER, buttonForPERIMETER_TOWER, buttonForDEFENCE_TURRET, buttonForSQUARE_TOWER
                , buttonForROUND_TOWER, buttonForBARRACKS, buttonForARMORY, buttonForMERCENARY_POST);
        scrollPane1.setContent(content);
        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);
    }

    private void showFarmScroll(ScrollPane scrollPane1) {
        Button buttonForMOTEL = new Button();
        buttonForMOTEL.setGraphic(getImageOFBuildingWithName(BuildingName.MOTEL));
        buttonForMOTEL.setStyle("-fx-background-color: transparent;");
        buttonForMOTEL.setOnAction(e -> setDragOnBuilding(buttonForMOTEL, MOTEL));

        Button buttonForMILL = new Button();
        buttonForMILL.setGraphic(getImageOFBuildingWithName(BuildingName.MILL));
        buttonForMILL.setStyle("-fx-background-color: transparent;");
        buttonForMILL.setOnAction(e -> setDragOnBuilding(buttonForMILL, MILL));

        Button buttonForAPPLE_GARDEN = new Button();
        buttonForAPPLE_GARDEN.setGraphic(getImageOFBuildingWithName(BuildingName.APPLE_GARDEN));
        buttonForAPPLE_GARDEN.setStyle("-fx-background-color: transparent;");
        buttonForAPPLE_GARDEN.setOnAction(e -> setDragOnBuilding(buttonForAPPLE_GARDEN, APPLE_GARDEN));

        Button buttonForDIARY_FARMER = new Button();
        buttonForDIARY_FARMER.setGraphic(getImageOFBuildingWithName(BuildingName.DIARY_FARMER));
        buttonForDIARY_FARMER.setStyle("-fx-background-color: transparent;");
        buttonForDIARY_FARMER.setOnAction(e -> setDragOnBuilding(buttonForDIARY_FARMER, DIARY_FARMER));


        Button buttonForWHEAT_FIELD = new Button();
        buttonForWHEAT_FIELD.setGraphic(getImageOFBuildingWithName(BuildingName.WHEAT_FIELD));
        buttonForWHEAT_FIELD.setStyle("-fx-background-color: transparent;");
        buttonForWHEAT_FIELD.setOnAction(e -> setDragOnBuilding(buttonForWHEAT_FIELD, WHEAT_FIELD));

        Button buttonForBAKERY = new Button();
        buttonForBAKERY.setGraphic(getImageOFBuildingWithName(BuildingName.BAKERY));
        buttonForBAKERY.setStyle("-fx-background-color: transparent;");
        buttonForBAKERY.setOnAction(e -> setDragOnBuilding(buttonForBAKERY, BAKERY));

        Button buttonForBREWING = new Button();
        buttonForBREWING.setGraphic(getImageOFBuildingWithName(BuildingName.BREWING));
        buttonForBREWING.setStyle("-fx-background-color: transparent;");
        buttonForBREWING.setOnAction(e -> setDragOnBuilding(buttonForBREWING, BREWING));

        Button buttonForFOOD_STORE = new Button();
        buttonForFOOD_STORE.setGraphic(getImageOFBuildingWithName(BuildingName.FOOD_STORE));
        buttonForFOOD_STORE.setStyle("-fx-background-color: transparent;");
        buttonForFOOD_STORE.setOnAction(e -> setDragOnBuilding(buttonForFOOD_STORE, FOOD_STORE));

        Button buttonForHOUSE = new Button();
        buttonForHOUSE.setGraphic(getImageOFBuildingWithName(BuildingName.HOUSE));
        buttonForHOUSE.setStyle("-fx-background-color: transparent;");
        buttonForHOUSE.setOnAction(e -> setDragOnBuilding(buttonForHOUSE, HOUSE));

        Button buttonForCHURCH = new Button();
        buttonForCHURCH.setGraphic(getImageOFBuildingWithName(BuildingName.CHURCH));
        buttonForCHURCH.setStyle("-fx-background-color: transparent;");
        buttonForCHURCH.setOnAction(e -> setDragOnBuilding(buttonForCHURCH, CHURCH));

        HBox content = new HBox();
        content.setPrefSize(2000, 40);
        content.getChildren().addAll(buttonForAPPLE_GARDEN, buttonForMOTEL, buttonForMILL, buttonForDIARY_FARMER
                , buttonForWHEAT_FIELD, buttonForBAKERY, buttonForFOOD_STORE, buttonForCHURCH, buttonForHOUSE);
        scrollPane1.setContent(content);
        scrollPane1.setFitToWidth(true);
        scrollPane1.setFitToHeight(true);
    }

    private ImageView getImageOFBuildingWithName(Enum name) {
        ImageView imageView = new ImageView(GameMenu.class.getResource("/Building/" + name + ".png")
                .toExternalForm());
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        return imageView;
    }


    private void createBuilding(BuildingName buildingName, int i, int j) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (GameMenuController.dropBuilding(i, j, buildingName.name, true)) {
            case ALREADY_HAS_KEEP:
                alert.setTitle("ERROR");
                alert.setContentText("Error: You can have only one Keep.");
                alert.showAndWait();
                break;
            case HAS_NOT_PLACED_KEEP:
                alert.setTitle("ERROR");
                alert.setContentText("Error: You must Place your Keep before any other building.");
                alert.showAndWait();
                break;
            case CELL_IS_FULL:
                alert.setTitle("ERROR");
                alert.setContentText("Error: There is already a building in that location.");
                alert.showAndWait();
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                alert.setTitle("ERROR");
                alert.setContentText("Error: The cell has an incompatible texture.");
                alert.showAndWait();
                break;
            case SUCCESS:
                Building building = Building.getInstance(buildingName.name, i, j);
                Button button = new Button();
                ImageView imageView = new ImageView(new Image(building.getImageView().toExternalForm()));
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                button.setGraphic(imageView);
                button.setMaxWidth(29);
                button.setMaxHeight(29);
                button.setMinHeight(29);
                button.setMinWidth(29);
                button.setStyle("-fx-background-color: transparent;");
                button.setOnAction(event -> createBuildingMenu(buildingName, i, j));
                addInMap(button, i, j);
                break;
        }


    }

    private void createBuildingMenu(BuildingName buildingName, int i, int j) {
        if (GameMenuController.selectBuilding(i, j).equals(MenuMessages.SUCCESS)) {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Popup");
            VBox vbox = new VBox();
            vbox.setBackground(new Background(new BackgroundImage(new Image(GameMenu.class
                    .getResource("/Backgrounds/default.jpg").toExternalForm())
                    , BackgroundRepeat.NO_REPEAT
                    , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER
                    , new BackgroundSize(1.0, 1.0, true, true
                    , false, false))));
            vbox.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
            Building building = Building.getInstance(buildingName.name, 0, 0);

            if (!building.getCategory().equals(Building.Category.TREE)) {
                Button buttonForRepair = new Button("Repair");
                buttonForRepair.setOnAction(event -> repairBuildingView());
                vbox.getChildren().add(buttonForRepair);

                Button buttonForShowHealth = new Button("Show Health");
                buttonForShowHealth.setOnAction(event -> showHealthBuildingView());
                vbox.getChildren().add(buttonForShowHealth);
            }

            Button buttonForCopy = new Button("Copy");
            buttonForCopy.setOnAction(event -> copyBuilding(buildingName, buttonForCopy));
            vbox.getChildren().add(buttonForCopy);


            for (Attribute attribute :
                    building.getAttributes()) {
                if (attribute instanceof CreateUnit) {
                    //todo
                } else if (attribute instanceof ChangeTaxRate) {
                    Button buttonForChangeTaxRate = new Button("Change Tax Rate");
                    buttonForChangeTaxRate.setOnAction(event -> popupForTaxRate());
                    vbox.getChildren().add(buttonForChangeTaxRate);
                } else if (attribute instanceof Shop) {
                    Button buttonForShop = new Button("Shop");
                    buttonForShop.setOnAction(event -> new ShopMenu().run());
                    vbox.getChildren().add(buttonForShop);
                } else if (attribute instanceof DrinkServing) {
                    Button buttonForServeDrink = new Button("Serve Drink");
                    buttonForServeDrink.setOnAction(event -> serveDrinkPopup());
                    vbox.getChildren().add(buttonForServeDrink);
                } else if (attribute instanceof Process) {
                    Button buttonForProcess = new Button("Process");
                    buttonForProcess.setOnAction(event -> ProcessPopup());
                    vbox.getChildren().add(buttonForProcess);
                } else if (attribute instanceof Capacity) {
                    Button buttonForCapacity = new Button("Show Capacity");
                    buttonForCapacity.setOnAction(event -> showCapacity1());
                    vbox.getChildren().add(buttonForCapacity);
                } else if (attribute instanceof ChangeFoodRate) {
                    Button buttonForChangeFoodRate = new Button("Change Food Rate");
                    buttonForChangeFoodRate.setOnAction(event -> changeFoodRatePopup());
                    vbox.getChildren().add(buttonForChangeFoodRate);
                }
            }
            vbox.setSpacing(30);
            vbox.setAlignment(Pos.CENTER);
            Scene popupScene = new Scene(vbox, 400, 600);
            popup.setScene(popupScene);
            popup.showAndWait();
        }
    }

    private Label clipBoard = new Label();
    private BorderPane clipboardPane;

    private void copyBuilding(BuildingName buildingName, Button buttonForCopy) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(buildingName.name);
        clipboard.setContent(content);
        // Update the label to show the copied value
        clipBoard.setText("ClipBoard\n" + buildingName + " Copied");
        clipboardPane = new BorderPane(clipBoard);
        clipboardPane.setPrefSize(100, 100);
        clipboardPane.setStyle("-fx-background-color: #e57550;");
        StackPane.setMargin(clipboardPane, new Insets(30, 0, 800, 1400));
        gamePane.getChildren().add(clipboardPane);
        buttonForCopy.setText("Copied");

    }

    private void changeFoodRatePopup() {
        Stage popup1 = new Stage();
        popup1.initModality(Modality.APPLICATION_MODAL);
        popup1.setTitle("Food Rate");
        Label label = new Label("Enter New Food Rate:");
        TextField textField = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(event1 -> {
            String text = textField.getText();
            try {
                int number = Integer.parseInt(text);
                changeFoodRate(number);
                popup1.close();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number entered!");
            }
        });

        VBox vbox1 = new VBox(label, textField, okButton);
        vbox1.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        vbox1.setAlignment(Pos.CENTER);
        Scene popupScene1 = new Scene(vbox1, 200, 150);
        popup1.setScene(popupScene1);
        popup1.showAndWait();
    }

    private void changeFoodRate(int number) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (GameMenuController.foodRate(number)) {
            case OUT_OF_BOUNDS:
                alert.setContentText("food rate must be between 2 & -2");
                break;
        }
        alert.showAndWait();
    }

    private void showCapacity1() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(GameEntityController.showCondition());
        alert.showAndWait();
    }

    private void ProcessPopup() {
        Stage popup1 = new Stage();
        popup1.initModality(Modality.APPLICATION_MODAL);
        popup1.setTitle("Process");
        Label label = new Label("Number Of Process Resource:");
        TextField textField = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(event1 -> {
            String text = textField.getText();
            try {
                int number = Integer.parseInt(text);
                ProcessResource(number);
                popup1.close();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number entered!");
            }
        });
        VBox vbox1 = new VBox(label, textField, okButton);
        vbox1.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        vbox1.setAlignment(Pos.CENTER);
        Scene popupScene1 = new Scene(vbox1, 200, 150);
        popup1.setScene(popupScene1);
        popup1.showAndWait();
    }

    private void ProcessResource(int number) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (GameEntityController.process(number)) {
            case NOT_ENOUGH_RESOURCES:
                alert.setContentText("you don't have this amount");
                alert.showAndWait();
                break;
        }
    }

    private void serveDrinkPopup() {
        Stage popup1 = new Stage();
        popup1.initModality(Modality.APPLICATION_MODAL);
        popup1.setTitle("Serve Drink");
        Label label = new Label("Number Of Drink Serve:");
        TextField textField = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(event1 -> {
            String text = textField.getText();
            try {
                int number = Integer.parseInt(text);
                serveDrink(number);
                popup1.close();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number entered!");
            }
        });
        VBox vbox1 = new VBox(label, textField, okButton);
        vbox1.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        vbox1.setAlignment(Pos.CENTER);
        Scene popupScene1 = new Scene(vbox1, 200, 150);
        popup1.setScene(popupScene1);
        popup1.showAndWait();
    }


    private void serveDrink(int number) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (GameEntityController.serveDrink(number)) {
            case NOT_ENOUGH_RESOURCES:
                alert.setContentText("you don't have enough wine");
                alert.showAndWait();
                break;
        }
    }

    private void changeTaxRate(int rate) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (GameMenuController.taxRate(rate)) {
            case OUT_OF_BOUNDS:
                alert.setContentText("tax rate must be between 8 & -3");
                alert.showAndWait();
                break;
        }
    }

    private void showHealthBuildingView() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("HP = " + GameEntityController.getBuildingHealth());
        alert.showAndWait();
    }

    private void repairBuildingView() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (GameEntityController.repairBuilding(false)) alert.setContentText("Repaired successfully.");
        else alert.setContentText("Error: You do not have enough GoldCoin.");
        alert.showAndWait();
    }

    private void popupForTaxRate() {
        Stage popup1 = new Stage();
        popup1.initModality(Modality.APPLICATION_MODAL);
        popup1.setTitle("Tax Rate");
        Label label = new Label("Enter New Tax Rate:");
        TextField textField = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(event1 -> {
            String text = textField.getText();
            try {
                int number = Integer.parseInt(text);
                changeTaxRate(number);
                popup1.close();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number entered!");
            }
        });
        VBox vbox1 = new VBox(label, textField, okButton);
        vbox1.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        vbox1.setAlignment(Pos.CENTER);
        Scene popupScene1 = new Scene(vbox1, 200, 150);
        popup1.setScene(popupScene1);
        popup1.showAndWait();
    }

    private void forTest() {
        ArrayList<String> userName = new ArrayList<>();
        userName.add("player1");
        MainController.setCurrentUser(Database.getAllUsers().get(1));
        GameMenuController.createGame(2, userName);
        GameMenuController.loadGame(1);

    }

}

