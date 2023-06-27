package view;

import controller.menu_controllers.GameEntityController;
import controller.menu_controllers.GameMenuController;
import controller.messages.MenuMessages;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database;
import model.game.Game;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.game_entities.UnitName;
import model.game.map.Map;
import model.game.map.MapCell;
import view.menus.AbstractMenu;
import view.menus.AppMenu;

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
        dropUnit(stage);
        scene.getStylesheets().add(GameMenu.class.getResource("/CSS/defaultCSS.css").toExternalForm());
        stage.setScene(scene);
        showDetailWithDragClick(gridPane);
        pressedNode(gridPane, stage);
        stage.setFullScreen(true);
        stage.show();
    }

    private void dropUnit(Stage stage) {
        Image buttonImage = new Image(GameMenu.class.getResource("/Icon/dropUnit.png").toExternalForm());
        ImageView imageView = new ImageView(buttonImage);
        Button button = new Button();
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
                stage.setScene(scene2);
                stage.setFullScreen(true);
                stage.show();
            }
        });
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
                                alert.setContentText("Your x and y components should be numbers");
                                alert.showAndWait();
                            }
                            else {
                                MenuMessages messages = selectUnit(chosenX, chosenY, type);
                                if(messages == MenuMessages.SUCCESS){
                                    moveUnit(Integer.parseInt(x), Integer.parseInt(y));
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
                                    MenuMessages message = moveUnit(Integer.parseInt(x), Integer.parseInt(y));
                                    if(message == MenuMessages.SUCCESS){

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

                    }
                });
            });
        }
    }
    public static void patrolUnit(int fromX, int fromY, int toX ,int toY) {

        switch (GameEntityController.patrolUnit(fromX, fromY, toX, toY)) {
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
