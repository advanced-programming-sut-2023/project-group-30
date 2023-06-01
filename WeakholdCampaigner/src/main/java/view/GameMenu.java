package view;

import controller.menu_controllers.GameMenuController;
import javafx.application.Application;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.Database;
import model.User;
import model.game.Game;
import model.game.Government;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.map.Map;
import model.game.map.MapCell;

import java.util.ArrayList;
import java.util.HashMap;

import static model.enums.FileName.*;
import static model.game.map.MapCell.Texture.*;


public class GameMenu extends Application {
    private GridPane gridPane;
    private ScrollPane scrollPane;
    private StackPane gamePane;
    private HashMap<Enum, ImagePattern> imagePatternHashMap;
    private Button textForPopularity = new Button();


    @Override
    public void start(Stage stage) throws Exception {
        gridPane = new GridPane();
        gridPane.setVgap(-0.5);
        gridPane.setHgap(-0.5);
        Database.loadMap();
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
            }
        });
        scrollPane.setPrefSize(stage.getMaxWidth(), stage.getMaxHeight());
        createMap(Database.getMapById(1));
        scrollPane.setScaleX(3 * scrollPane.getScaleX() / 2);
        scrollPane.setScaleY(3 * scrollPane.getScaleY() / 2);
        setZoom(scrollPane);
        stage.setScene(scene);
        stage.setFullScreen(true);
        System.out.println(gridPane.getChildren().size());
        stage.show();
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
        imagePatternHashMap = new HashMap<>();
        setImagePattern(imagePatternHashMap);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                Rectangle cell = new Rectangle(30, 30);
                cell.setFill(imagePatternHashMap.get(map.getCell(i, j).getTexture()));
                Group group = new Group(cell);
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Texture:" + map.getCell(i, j).getTexture().name());
                if (map.getCell(i, j).getBuilding() != null) {
                    tooltip.setText(tooltip.getText() + "\nBuilding:" + map.getCell(i, j).getBuilding());
                }
                if (movingUnits(map.getCell(i, j).getUnits()).size() != 0) {
                    tooltip.setText(tooltip.getText() + "\nUnit:");
                    for (Unit unit : movingUnits(map.getCell(i, j).getUnits())) {
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
        setPopularity();


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

    public void setPopularity() {
        textForPopularity.setStyle("-fx-background-color: transparent;");
        textForPopularity.setText(/*GameMenuController.getCurrentGame().getCurrentGovernment().getPopularity() + ""*/"1000");
        StackPane.setMargin(textForPopularity, new Insets(750, 0, 0, 270));
        gamePane.getChildren().add(textForPopularity);
        textForPopularity.setOnAction(e -> showPopularity());
    }

    public void showPopularity() {
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

    private void setPopularityFactorsInPopup(Pane pane) {
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

    private HBox getMaskCondition(double number, String name) {
        HBox hBox = new HBox();

        Text numberText = new Text(number + "");
        numberText.setStyle("-fx-font-family: Cardamon");
        numberText.setStyle("-fx-font-size: 20px");
        hBox.getChildren().add(numberText);
        Rectangle rectangle = new Rectangle(25, 25);
        if (number == 0) {
            rectangle.setFill((imagePatternHashMap.get(ignoreMask)));
            numberText.setFill(Color.WHITE);
        } else if (number > 0) {
            rectangle.setFill(imagePatternHashMap.get(happyMask));
            numberText.setFill(Color.GREEN);
        } else {
            rectangle.setFill(imagePatternHashMap.get(angryMask));
            numberText.setFill(Color.RED);
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
        vBox.getChildren().addAll(fearRate, getFearSlider(), foodRate, setFoodAndTaxRate(-2, 2, 0)
                , taxRate, setFoodAndTaxRate(-3, 8, 0));
        vBox.setAlignment(Pos.CENTER);
        borderPane.getStylesheets().add(GameMenu.class.getResource("/Css/Slider.css").toExternalForm());
        borderPane.setCenter(vBox);


    }

    private VBox getFearSlider() {
        double min = -5;
        double max = 5;
        Slider slider = new Slider(min, max, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(10);
        slider.setMinWidth(200);

        Label valueLabel = new Label(0+ "");
        valueLabel.setAlignment(Pos.CENTER);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double roundedValue = Math.round(newValue.doubleValue() * 10.0) / 10.0;
            valueLabel.setText(roundedValue+"");
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


}

