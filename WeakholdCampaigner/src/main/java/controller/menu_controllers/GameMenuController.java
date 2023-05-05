package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.game.Game;
import model.game.Government;
import model.User;

import java.util.ArrayList;

public class GameMenuController {
    private static Game currentGame;

    public static boolean loadGame(int gameId) {
        currentGame = Database.getGameById(gameId);

        return currentGame != null;
    }

    public static ArrayList<Government> getGovernments() {
        //return governments;
        //TODO
        return null;
    }

    public static void addGovernment(Government government) {
        //governments.add(government);
        //TODO
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
