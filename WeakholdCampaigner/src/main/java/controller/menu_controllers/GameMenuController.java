package controller.menu_controllers;

import controller.MainController; //TODO: check every import on every file, and attempt to reduce dependencies.
import controller.messages.MenuMessages;
import model.Database;
import model.game.Game;
import model.game.Government;
import model.User;
import view.menus.AbstractMenu;

import java.util.ArrayList;

public class GameMenuController {
    private static Game currentGame;

    public static MenuMessages createGame(int mapId, ArrayList<String> usernames){
        if (Database.getMapById(mapId) == null) return MenuMessages.MAP_DOES_NOT_EXIST;

        //there should be 2 to 8 players including the host
        if (usernames.size() < 1 || usernames.size() > 7) return MenuMessages.INVALID_NUMBER_OF_PLAYERS;

        //currentUser should be player1
        ArrayList<User> users = new ArrayList<>();
        users.add(MainController.getCurrentUser());

        //check that users exist
        for (String username :
                usernames) {
            User user = Database.getUserByName(username);
            if (user == null) return MenuMessages.USERNAME_DOES_NOT_EXIST;
            users.add(user);
        }

        //create a government for each user
        ArrayList<Government> governments = new ArrayList<>();
        for (User user :
                users) {
            governments.add(new Government(user));
        }

        AbstractMenu.show("Game id: " +
                Database.addGame(
                        new Game(Database.getMapById(mapId), governments)
                ).toString());
        return MenuMessages.GAME_CREATED_SUCCESSFULLY;
    }

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
