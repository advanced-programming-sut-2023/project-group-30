package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.game.game_entities.Unit;

public class GameEntityController extends GameController {
    private static Unit currentUnit; //is set whenever user selects a Unit

    public static void setCurrentUnit(Unit unit) {
        currentUnit = unit;
    }

    public static void createUnit(String type, int count) {

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
}
