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

    public static void showFoodRate() {
    }

    public static MenuMessages taxRate(int rate) {
        return MenuMessages.OK;
    }

    public static void showTaxRate() {
    }

    public static MenuMessages setFearRate(int rate) {
        return MenuMessages.OK;
    }

    public static MenuMessages dropBuilding(int x, int y, String type) {
        return MenuMessages.OK;
    }

    public static MenuMessages selectBuilding(int x, int y) {
        return MenuMessages.OK;
    }

    public static MenuMessages creatUnit(int count, String type) {
        return MenuMessages.OK;
    }

    public static MenuMessages repair() {
        return MenuMessages.OK;
    }
}
