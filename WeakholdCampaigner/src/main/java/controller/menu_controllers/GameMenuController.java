package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.game.Game;
import model.game.Government;
import model.User;
import view.menus.AppMenu;

import java.util.ArrayList;

public class GameMenuController {
    private static Game currentGame;

    public static boolean loadGame(int gameId) {
        currentGame = Database.getGameById(gameId);

        return currentGame != null;
    }

    public static MenuMessages showMap(int x, int y) {
        return MenuMessages.OK;
    }

    public static void showPopularityFactor() {
        Government government = currentGame.getCurrentGovernment();
        AppMenu.show("food :  " + government.getPopularityOfFood());
        AppMenu.show("tax :  " + government.getPopularityOfTax());
        AppMenu.show("religion :  " + government.getReligionRate());
        AppMenu.show("fear :  " + government.getPopularityOfFear());
    }

    public static int showPopularity() {
        return currentGame.getCurrentGovernment().getPopularity();
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

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game currentGame) {
        GameMenuController.currentGame = currentGame;
    }

    public static void nextTurn() {
        ArrayList<Government> governments = currentGame.getGovernments();
        for (Government i : governments)
            i.updateAllForNextTurn();

    }
}
