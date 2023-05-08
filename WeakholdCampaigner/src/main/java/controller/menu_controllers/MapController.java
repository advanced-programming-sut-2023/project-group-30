package controller.menu_controllers;

import controller.messages.MenuMessages;

public class MapController {
    public static MenuMessages showMap(int x, int y) {
        if(InvalidCoordinate(x, y)){
            return MenuMessages.INVALID_LOCATION;
        }
        return MenuMessages.OK;
    }
    public static MenuMessages moveMap(int right, int left, int up, int down) {
        return MenuMessages.OK;
    }

    public static MenuMessages showDetails(int x, int y) {
        return MenuMessages.OK;
    }
    public static boolean InvalidCoordinate(int x, int y){
        if(GameMenuController.getCurrentGame().getMapID() == 5){
            if(y < 0 || y > 400)
                return false;
            if(x < 0 || x > 400)
                return false;
        }else {
            if(y < 0 || y > 200)
                return false;
            if(x < 0 || x > 200)
                return false;
        }
        return true;
    }
}
