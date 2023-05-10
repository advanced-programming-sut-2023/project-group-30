package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.game.map.MapCell;

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
            if (y < 0 || y > 398)
                return true;
            if (x < 0 || x > 398)
                return true;
        } else {
            if (y < 0 || y > 198)
                return true;
            if (x < 0 || x > 198)
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
        for(int i = x; i < x2; i++){
            for (int j = y; j < y2;j++){
                if(GameMenuController.getCurrentGame().getBuilding(i , j) != null){
                    return MenuMessages.BUILDING_EXISTENCE;
                }
            }
        }
        for(int i = x; i < x2; i++){
            for (int j = y; j < y2;j++){
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
}
