package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.User;

public class GameMenuController {
    public static MenuMessages showMap(int x, int y) {
        return MenuMessages.OK;
    }

    public static void showPopularityFactor() {
    }

    public static int showPopularity(User currentUser) {
        return 1;
    }

    public static void showFoodList() {

    }
    public static MenuMessages foodRate(int rate) {
        return MenuMessages.OK;
    }
}
