package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.attributes.Attribute;
import model.attributes.building_attributes.Capacity;
import model.attributes.building_attributes.Harvesting;
import model.attributes.building_attributes.Process;
import model.enums.Resource;
import model.game.Game;
import model.game.Government;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import view.menus.AppMenu;

import java.util.HashMap;
import java.util.Map;

public class GameEntityController extends GameController {
    private static Unit currentUnit; //is set whenever user selects a Unit
    private static Building currentBuilding;

    public static void setCurrentUnit(Unit unit) {
        currentUnit = unit;
    }

    public static void setCurrentBuilding(Building building) {
        currentBuilding = building;
    }

    public static MenuMessages createUnit(String type, int count) {
        Unit unit = Unit.getInstance(type, 0, 0);
        Government government = currentGame.getCurrentGovernment();
        if (unit == null) return MenuMessages.INVALID_TYPE;
        for (Map.Entry<Resource, Integer> entry : unit.getProductionCost().entrySet()) {
            if (government.getResources(entry.getKey()) < entry.getValue())
                return MenuMessages.INVALID_AMOUNT;
        }
        for (Map.Entry<Resource, Integer> entry : unit.getProductionCost().entrySet()) {
            government.addResources(entry.getKey(), -entry.getValue());
        }
        //TODO : create unit in map should write
        return MenuMessages.OK;

    }

    public static void repairBuilding() {

    }

    public static MenuMessages moveUnitTo(int destinationX, int destinationY) {
        if (!checkLocation(destinationX, destinationY))
            return MenuMessages.INVALID_LOCATION;

        if (!currentUnit.canGoTo(currentGame.getTexture(destinationX, destinationY)))
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;

        if (currentUnit.isPatrolling())
            return MenuMessages.IS_PATROLLING;

        int[] tempDestination = currentGame.move(
                currentUnit.getCurrentX(), currentUnit.getCurrentY(), destinationX, destinationY,
                currentUnit.getRemainingMovement());
        currentUnit.setCurrentLocation(tempDestination[0], tempDestination[1]);
        currentUnit.setRemainingMovement(tempDestination[2]);

        currentUnit.addDestination(destinationX, destinationY); //TODO: remove reached destinations in nextTurn()
        return MenuMessages.SUCCESS;
    }

    public static MenuMessages patrolUnit(int fromX, int fromY, int toX, int toY) {
        if (!checkLocation(fromX, fromY) || !checkLocation(toX, toY))
            return MenuMessages.INVALID_LOCATION;

        if (!Unit.canGoTo(currentGame.getTexture(fromX, fromY)) || !Unit.canGoTo(currentGame.getTexture(toX, toY)))
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;

        currentUnit.setPatrolling(true);
        currentUnit.addDestination(fromX, fromY);
        currentUnit.addDestination(toX, toY);
        return MenuMessages.SUCCESS;
    }

    public static void halt() {
        currentUnit.setPatrolling(false);
        currentUnit.clearDestinations();
    }

    public static MenuMessages setStance(String stance) {
        switch (stance) {
            case "standing":
                currentUnit.setStance(Unit.UnitStance.STAND_GROUND);
                break;
            case "defensive":
                currentUnit.setStance(Unit.UnitStance.DEFENSIVE_STANCE);
                break;
            case "offensive":
                currentUnit.setStance(Unit.UnitStance.AGGRESSIVE_STANCE);
                break;
            default:
                return MenuMessages.INVALID_TYPE;
            //break
        }

        return MenuMessages.SUCCESS;
    }

    public static MenuMessages meleeAttack(int enemyX, int enemyY) {
        if (currentUnit.hasAttacked()) return MenuMessages.ALREADY_ATTACKED;

        if (Math.abs(currentUnit.getCurrentX() - enemyX) > 1 || Math.abs(currentUnit.getCurrentY() - enemyY) > 1)
            return MenuMessages.TOO_FAR;

        Unit enemyUnit = null;
        for (Unit unit :
                currentGame.getUnits(enemyX, enemyY))
            if (!unit.getGovernmentColor().equals(currentUnit.getGovernmentColor())) {
                enemyUnit = unit;
                break;
            }
        if (enemyUnit == null) return MenuMessages.NO_MATCHING_UNIT;

        //TODO
        return MenuMessages.SUCCESS;
    }

    public static void rangedAttack(int enemyX, int enemyY) {

    }

    public static void pourOil(String direction) {

    }

    public static void digTunnel(int x, int y) {

    }

    public static void buildEquipment(String equipmentName) {

    }

    public static void disbandUnit() {

    }

    public static MenuMessages serveDrink(int amount) {
        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        Double wine = government.getResources(Resource.WINE);
        if (wine < amount) return MenuMessages.NOT_ENOUGH_RESOURCES;
        government.addResources(Resource.WINE, -amount);
        government.addPopularity(amount);
        return MenuMessages.OK;
    }

    public static MenuMessages process(int amount) {
        Process process = getProcess();
        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        if (government.getResources(process.getPrimarySubstance()) < amount)
            return MenuMessages.NOT_ENOUGH_RESOURCES;
        government.addResources(process.getPrimarySubstance(), -amount);
        government.addResources(process.getFinallySubstance(), amount);
        return MenuMessages.OK;


    }

    public static Process getProcess() {
        for (Attribute i : currentBuilding.getAttributes()) {
            if (i instanceof Process) {
                return (Process) i;
            }
        }
        return null;
    }

    public static void showCondition() {
        Capacity capacity = getCapacity();
        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        AppMenu.show("filled " + government.getStoredUnit(capacity.getStoredItem()) + " out of "
                + capacity.getMaxCapacity());

    }

    public static Capacity getCapacity() {
        for (Attribute i : currentBuilding.getAttributes()) {
            if (i instanceof Capacity) {
                return (Capacity) i;
            }
        }
        return null;
    }

}
