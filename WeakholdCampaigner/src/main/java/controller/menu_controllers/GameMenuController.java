package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Government;
import model.User;

import java.util.ArrayList;

public class GameMenuController {
    private static ArrayList<Government> governments = new ArrayList<>();


    public static ArrayList<Government> getGovernments() {
        return governments;
    }
    public void addGovernment (Government government) {
        governments.add(government);
    }
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
}
