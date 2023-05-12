package controller.menu_controllers;

import controller.MainController; //TODO: check every import on every file, and attempt to reduce dependencies.
import controller.messages.MenuMessages;
import model.Database;
import model.attributes.Attribute;
import model.attributes.building_attributes.NeedsSpecialPlacement;
import model.enums.Resource;
import model.game.Game;
import model.game.Government;
import model.User;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.game_entities.UnitName;
import model.game.map.MapCell;
import view.menus.AbstractMenu;
import view.menus.AppMenu;

import java.util.ArrayList;
import java.util.Map;

public class GameMenuController extends GameController {
    public static MenuMessages createGame(int mapId, ArrayList<String> usernames) {
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
                        new Game(Database.getMapById(mapId), governments, mapId)
                ).toString());
        return MenuMessages.SUCCESS;
    }

    public static boolean loadGame(int gameId) {
        currentGame = Database.getGameById(gameId);

        return currentGame != null;
    }

    public static void showPopularityFactor() {
        Government government = currentGame.getCurrentGovernment();
        AppMenu.show("food :  " + government.getFoodPopularity());
        AppMenu.show("tax :  " + government.getTaxPopularity());
        AppMenu.show("religion :  " + government.getReligionPopularity());
        AppMenu.show("fear :  " + government.getFearPopularity());
        AppMenu.show("other : " + government.getOtherPopularity());
    }

    public static int showPopularity() {
        return currentGame.getCurrentGovernment().getPopularity();
    }

    public static void showFoodList() {
        for (Map.Entry<Resource, Double> entry :
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
        GameEntityController.setCurrentBuilding(building);
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

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game currentGame) {
        GameMenuController.currentGame = currentGame;
    }

    public static void endOnePlayersTurn() {
        if (currentGame.nextGovernment()) nextTurn();
    }

    public static void nextTurn() { //gets called when one full turn has passed
        currentGame.incrementTurn();

        ArrayList<Government> governments = currentGame.getGovernments();
        for (Government i : governments)
            i.updateAllForNextTurn();

        //loop through every unit and meet their needs:
        for (Unit unit :
                MapCell.allMapUnits) {
            unit.resetRemainingMovement();
            unit.setHasAttacked(false);

            //move the unit if needed:
            //Beware that having the units move and rest(reset their movement limit) in the same loop can be bug-prone.
            int[] unitDestination;
            while ((unitDestination = unit.getFirstDestination()) != null) {
                if (unit.getRemainingMovement() <= 0) break;

                if (unitDestination[0] == unit.getCurrentX() && unitDestination[1] == unit.getCurrentY()) {
                    unit.removeFirstDestination();
                    continue;
                }

                autoMoveUnit(unit, unitDestination[0], unitDestination[1]);
            }
        }

        //TODO: might be too time-consuming to search for entities this way.
        for (int x = 0; x < currentGame.getMapWidth(); x++)
            for (int y = 0; y < currentGame.getMapHeight(); y++) {

                Building building = currentGame.getBuilding(x, y);
                if (building != null) {

                }
            }
    }

    public static String whoseTurn() {
        return currentGame.getCurrentGovernment().getColor().toString() +
                " (" + currentGame.getCurrentGovernment().getOwner().getUsername() + ")";
    }

    public static int getTurn() {
        return currentGame.getCurrentTurn();
    }
}
