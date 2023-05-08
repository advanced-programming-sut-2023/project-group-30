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
                currentUnit.getSpeed());
        currentUnit.setCurrentLocation(tempDestination[0], tempDestination[1]);

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

    public static void setStance(String stance) {

    }

    public static void meleeAttack(int enemyX, int enemyY) {

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
