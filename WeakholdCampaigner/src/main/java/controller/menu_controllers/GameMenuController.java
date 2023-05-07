package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.Database;
import model.enums.Food;
import model.game.Game;
import model.game.Government;
import model.User;
import view.menus.AppMenu;

import java.util.ArrayList;
import java.util.Map;

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
        for (Map.Entry<Food, Double> entry :
                currentGame.getCurrentGovernment().getFoods().entrySet())
            AppMenu.show(entry.getKey() + " : " + entry.getValue());
    }

    public static MenuMessages foodRate(int rate) {
        if (rate > 2 || rate < -2)
            return MenuMessages.OUT_OF_BOUNDS;
        currentGame.getCurrentGovernment().setFoodRate(rate);
        return MenuMessages.OK;
    }

    public static void showFoodRate() {
        Government government = currentGame.getCurrentGovernment();
        if (government.consumableFood() > government.getFoodUnit())
            AppMenu.show("food rate : -2");
        else AppMenu.show("food rate : " + government.getFoodRate());
    }

    public static MenuMessages taxRate(int rate) {
        if (rate < -3 || rate > 8)
            return MenuMessages.OUT_OF_BOUNDS;
        currentGame.getCurrentGovernment().setTaxRate(rate);
        return MenuMessages.OK;
    }

    public static void showTaxRate() {
        Government government = currentGame.getCurrentGovernment();
        if ((government.getTax() * (-1)) > government.getGold())
            AppMenu.show("tax rate : 0");
        else AppMenu.show("tax rate : " + government.getTaxRate());
    }

    public static MenuMessages setFearRate(int rate) {
        if (rate > 5 || rate < -5)
            return MenuMessages.OUT_OF_BOUNDS;
        currentGame.getCurrentGovernment().setFearRate(rate);
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
