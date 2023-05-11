package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.game.game_entities.Building;
import model.game.game_entities.BuildingName;
import model.game.map.MapCell;

import java.util.ArrayList;
import java.util.Random;

public class MapController {
    public static MenuMessages showMap(int x, int y) {
        if (InvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        return MenuMessages.OK;
    }

    public static MenuMessages moveMap(int right, int left, int up, int down, int x, int y) {
        int mapXMovement = down - up;
        int mapYMovement = right - left;
        if (InvalidCoordinate(x + mapXMovement, y + mapYMovement)) {
            return MenuMessages.INVALID_LOCATION;
        }
        return MenuMessages.OK;
    }

    public static MenuMessages showDetails(int x, int y) {
        if (secondInvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        return MenuMessages.OK;
    }

    public static MenuMessages setCellTexture(int x, int y, String texure) {
        if (secondInvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        if(GameMenuController.getCurrentGame().getBuilding(x, y) != null){
            return MenuMessages.BUILDING_EXISTENCE;
        }
        if(findingTextureType(texure) == null){
            return MenuMessages.INVALID_TYPE;
        }
        GameMenuController.getCurrentGame().setTexture(x, y, findingTextureType(texure));
        return MenuMessages.OK;
    }

    public static boolean InvalidCoordinate(int x, int y) {
        if (GameMenuController.getCurrentGame().getMapID() == 5) {
            if (y < 2 || y > 398)
                return true;
            if (x < 2 || x > 398)
                return true;
        } else {
            if (y < 2 || y > 198)
                return true;
            if (x < 2 || x > 198)
                return true;
        }
        return false;
    }

    public static boolean secondInvalidCoordinate(int x, int y) {
        if (GameMenuController.getCurrentGame().getMapID() == 5) {
            if (y < 0 || y > 400)
                return true;
            return x < 0 || x > 400;
        } else {
            if (y < 0 || y > 200)
                return true;
            return x < 0 || x > 200;
        }
    }
    public static MapCell.Texture findingTextureType(String texture) {
        switch (texture) {
            case "land":
                return MapCell.Texture.LAND;
            case "gravel":
                return MapCell.Texture.GRAVEL;
            case "slate":
                return MapCell.Texture.SLATE;
            case "stone":
                return MapCell.Texture.STONE;
            case "iron":
                return MapCell.Texture.IRON;
            case "grass":
                return MapCell.Texture.GRASS;
            case "grassland":
                return MapCell.Texture.GRASSLAND;
            case "meadow":
                return MapCell.Texture.MEADOW;
            case "shallow_water":
                return MapCell.Texture.SHALLOW_WATER;
            case "deep_water":
                return MapCell.Texture.DEEP_WATER;
            case "oil":
                return MapCell.Texture.OIL;
            case "beach":
                return MapCell.Texture.BEACH;
            case "river":
                return MapCell.Texture.RIVER;
            case "plain":
                return MapCell.Texture.PLAIN;
            default:
                return null;
        }
    }
    public static MenuMessages setBlockTexture(int x,int y, int x2, int y2, String type){
        if(secondInvalidCoordinate(x, y) || secondInvalidCoordinate(x2, y2)
                || (x2 < x) || (y2 < y))
            return MenuMessages.INVALID_LOCATION;
        if(findingTextureType(type) == null){
            return MenuMessages.INVALID_TYPE;
        }
        for(int i = x; i <= x2; i++){
            for (int j = y; j <= y2;j++){
                if(GameMenuController.getCurrentGame().getBuilding(i , j) != null){
                    return MenuMessages.BUILDING_EXISTENCE;
                }
            }
        }
        for(int i = x; i <= x2; i++){
            for (int j = y; j <= y2;j++){
                GameMenuController.getCurrentGame().setTexture(i , j, findingTextureType(type));
            }
        }
        return MenuMessages.OK;
    }
    public static MenuMessages clear(int x, int y){
        if (secondInvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        int mapId = GameMenuController.getCurrentGame().getMapID();
        GameMenuController.getCurrentGame().getMap().getCell(x, y).setTexture
                (Database.getMapById(mapId).getCell(x, y).getTexture());
        GameMenuController.getCurrentGame().getMap().getCell(x, y).setBuilding(null);
        GameMenuController.getCurrentGame().getMap().getCell(x, y).setUnits(null);
        return MenuMessages.OK;
    }
    public static MenuMessages dropRock(int x, int y, String direction){
        if (secondInvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        if(!isDirectionTrue(direction)){
            return MenuMessages.INVALID_DIRECTION;
        }
        if(GameMenuController.getCurrentGame().getBuilding(x , y) != null){
            return MenuMessages.BUILDING_EXISTENCE;
        }
        Building building = Building.getInstance("rock");
        GameMenuController.getCurrentGame().dropBuilding(building, x, y);
        if(direction.equals("r")){
            ArrayList<String> directionslist = new ArrayList<>();
            directionslist.add("n");
            directionslist.add("e");
            directionslist.add("w");
            directionslist.add("s");
            Random random = new Random();
            String d = directionslist.get(random.nextInt(4));
            GameMenuController.getCurrentGame().getBuilding(x, y).setDirection(d);
        }else {
            GameMenuController.getCurrentGame().getBuilding(x, y).setDirection(direction);
        }
        return MenuMessages.OK;
    }
    public static boolean isDirectionTrue(String direction){
        String []directions = new String[]{"n", "e", "w", "s", "r"};
        for (String d : directions){
            if(direction.equals(d))
                return true;
        }
        return false;
    }
    public static MenuMessages dropTree(int x, int y, String type){
        if (secondInvalidCoordinate(x, y)) {
            return MenuMessages.INVALID_LOCATION;
        }
        if(GameMenuController.getCurrentGame().getBuilding(x , y) != null){
            return MenuMessages.BUILDING_EXISTENCE;
        }
        if(findingTreeType(type) == null){
            return MenuMessages.INVALID_TYPE;
        }
        if(!isTextureAppropriateForTree(x, y)){
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;
        }
        Building building =Building.getInstance(type.replace("_", " "));
        GameMenuController.getCurrentGame().dropBuilding(building, x, y);
        return MenuMessages.OK;
    }
    public static BuildingName findingTreeType(String type){
        switch (type){
            case "desert_shrub":
                return BuildingName.DESERT_SHRUB;
            case "olive_tree":
                return BuildingName.OLIVE_TREE;
            case "cherry_tree":
                return BuildingName.CHERRY_TREE;
            case "coconut_tree":
                return BuildingName.COCONUT_TREE;
            case "date_tree":
                return BuildingName.DATE_TREE;
            default:
                return null;
        }
    }
    public static boolean isTextureAppropriateForTree(int x, int y){
        ArrayList<MapCell.Texture> textures = new ArrayList<>();
        textures.add(MapCell.Texture.DEEP_WATER);
        textures.add(MapCell.Texture.SHALLOW_WATER);
        textures.add(MapCell.Texture.RIVER);
        textures.add(MapCell.Texture.BEACH);
        textures.add(MapCell.Texture.OIL);
        textures.add(MapCell.Texture.GRAVEL);
        textures.add(MapCell.Texture.IRON);
        textures.add(MapCell.Texture.STONE);
        for(MapCell.Texture texture : textures){
            if(GameMenuController.getCurrentGame().getTexture(x, y).equals(texture))
                return false;
        }
        return true;
    }
}
