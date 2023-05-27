package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Database;
import model.game.map.Map;
import model.game.map.MapCell;

import java.util.HashMap;

import static model.game.map.MapCell.Texture.*;


public class GameMenu extends Application {
    private GridPane gridPane;
    private ScrollPane scrollPane;
    private StackPane gamePane;

    @Override
    public void start(Stage stage) throws Exception {
        gridPane = new GridPane();
        gridPane.setVgap(-0.5);
        gridPane.setHgap(-0.5);

        Database.loadMap();
        createMap(Database.getMapById(1));

        scrollPane = new ScrollPane(gridPane);
        scrollPane.setPrefViewportWidth(40 * 20); // 40 cells wide (20 pixels each)
        scrollPane.setPrefViewportHeight(40 * 20); // 40 cells high (20 pixels each)
        setZoom(scrollPane);

        gamePane = new StackPane(scrollPane);
        Scene scene = new Scene(gamePane,stage.getMaxWidth(),stage.getMaxHeight());
        scrollPane.setPrefSize(stage.getMaxWidth(),stage.getMaxHeight());

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
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
                gridPane.add(group, i, j);
            }
        }
        Rectangle cell = new Rectangle(60, 60);
        cell.setFill(new ImagePattern(new Image(GameMenu.class
                .getResource("/Tiles/1.png")
                .toExternalForm())));
        addInMap(cell, 150, 3);
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
        double minScale = 0.8;
        double maxScale = 1.5;
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if (deltaY < 0) {
                    zoomFactor = 1 / 1.05;
                }

                // Check if zoom out will cause the scale to become negative
                double newScaleX = scrollPane.getScaleX() * zoomFactor;
                double newScaleY = scrollPane.getScaleY() * zoomFactor;
                if (newScaleX < minScale || newScaleY > maxScale) {
                    return;
                }


                scrollPane.setScaleX(newScaleX);
                scrollPane.setScaleY(newScaleY);

                event.consume();
            }
        });

    }

}

