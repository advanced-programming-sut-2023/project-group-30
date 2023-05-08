package controller.menu_controllers;

import controller.MainController; //TODO: check every import on every file, and attempt to reduce dependencies.
import controller.messages.MenuMessages;
import model.Database;
import model.attributes.Attribute;
import model.attributes.building_attributes.NeedsSpecialPlacement;
import model.game.Game;
import model.game.Government;
import model.User;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.game_entities.UnitName;
import model.game.map.MapCell;
import view.menus.AbstractMenu;

import java.util.ArrayList;

public class GameMenuController extends GameController {
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
        int colorIndex = 0;
        for (User user :
                users) {
            governments.add(new Government(user, colorIndex++));
        }

        AbstractMenu.show("Game id: " +
                Database.addGame(
                        new Game(Database.getMapById(mapId), governments)
                ).toString());
        return MenuMessages.SUCCESS;
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

    public static MenuMessages dropBuilding(int x, int y, String type, boolean godMode) {
        if (!checkLocation(x, y))
            return MenuMessages.INVALID_LOCATION;

        Building building = Building.getInstance(type);
        if (building == null) return MenuMessages.INVALID_TYPE;

        if (type.equals("keep")) if (currentGame.getCurrentGovernment().hasPlacedKeep())
            return MenuMessages.ALREADY_HAS_KEEP;
        else if (!currentGame.getCurrentGovernment().hasPlacedKeep()) return MenuMessages.HAS_NOT_PLACED_KEEP;

        if (currentGame.getBuilding(x, y) != null) return MenuMessages.CELL_IS_FULL;

        MapCell.Texture texture = currentGame.getTexture(x, y);
        if (texture.equals(MapCell.Texture.STONE))
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;
        for (Attribute attribute :
                building.getAttributes())
            if (attribute instanceof NeedsSpecialPlacement)
                if (
                        !((NeedsSpecialPlacement) attribute).canBePlacedIn(texture)
                ) {
                    AbstractMenu.show(type + " needs to be placed in a cell with " +
                            ((NeedsSpecialPlacement) attribute).getNeededTexture() + "texture");
                    return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;
                }

        if (!godMode && !currentGame.getCurrentGovernment().purchase(building.getProductionCost()))
            return MenuMessages.NOT_ENOUGH_RESOURCES;

        currentGame.dropBuilding(building, x, y);
        return MenuMessages.SUCCESS;
    }

    public static MenuMessages selectBuilding(int x, int y) {
        if (!checkLocation(x, y))
            return MenuMessages.INVALID_LOCATION;

        Building building = currentGame.getBuilding(x, y);
        if (building == null) return MenuMessages.CELL_IS_EMPTY;

        if (!building.getGovernmentColor().equals(currentGame.getCurrentGovernment().getColor()))
            return MenuMessages.NOT_THE_OWNER;

        MainController.setCurrentMenu(building);
        return MenuMessages.SUCCESS;
    }

    public static MenuMessages selectUnit(int x, int y, String type) {
        if (!checkLocation(x, y))
            return MenuMessages.INVALID_LOCATION;

        if (UnitName.getUnitName(type) == null) return MenuMessages.INVALID_TYPE;

        for (Unit unit :
                currentGame.getUnits(x, y))
            if (unit.unitName.name.equals(type))
                if (unit.getGovernmentColor().equals(currentGame.getCurrentGovernment().getColor())) {
                    MainController.setCurrentMenu(unit);
                    GameEntityController.setCurrentUnit(unit);
                    return MenuMessages.SUCCESS;
                }

        return MenuMessages.NO_MATCHING_UNIT;
    }

    public static MenuMessages dropUnit(int x, int y, String type, boolean godMode) {
        if (!checkLocation(x, y))
            return MenuMessages.INVALID_LOCATION;

        Unit unit = Unit.getInstance(type, x, y);
        if (unit == null) return MenuMessages.INVALID_TYPE;

        MapCell.Texture texture = currentGame.getTexture(x, y);
        if (texture.equals(MapCell.Texture.DEEP_WATER) || texture.equals(MapCell.Texture.STONE))
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;

        if (!godMode && !currentGame.getCurrentGovernment().purchase(unit.getProductionCost()))
            return MenuMessages.NOT_ENOUGH_RESOURCES;

        currentGame.dropUnit(unit, x, y);
        return MenuMessages.SUCCESS;
    }
}
