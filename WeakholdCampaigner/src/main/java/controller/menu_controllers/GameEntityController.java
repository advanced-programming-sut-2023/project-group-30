package controller.menu_controllers;

import controller.MainController;
import controller.messages.MenuMessages;
import model.attributes.Attribute;
import model.attributes.building_attributes.Capacity;
import model.attributes.building_attributes.CreateUnit;
import model.attributes.building_attributes.Process;
import model.attributes.unit_attributes.RangedAttack;
import model.enums.Resource;
import model.game.Game;
import model.game.Government;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import view.menus.AbstractMenu;
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

    public static MenuMessages createUnit(String type) {
        int spawnX = currentBuilding.getCurrentX(), spawnY = currentBuilding.getCurrentY();
        Unit unit = Unit.getInstance(type, spawnX, spawnY);
        if (unit == null) return MenuMessages.INVALID_TYPE;

        for (Attribute attribute :
                currentBuilding.getAttributes()) if (attribute instanceof CreateUnit) {
            if (((CreateUnit) attribute).createArab != unit.isArab)
                return MenuMessages.INVALID_RACE;

            break;
        }

        Government government = currentGame.getCurrentGovernment();
        for (Map.Entry<Resource, Integer> entry : unit.getProductionCost().entrySet()) {
            if (government.getResources(entry.getKey()) < entry.getValue())
                return MenuMessages.INVALID_AMOUNT;
        }
        for (Map.Entry<Resource, Integer> entry : unit.getProductionCost().entrySet()) {
            government.addResources(entry.getKey(), -entry.getValue());
        }

        currentGame.dropUnit(unit, spawnX, spawnY);
        return MenuMessages.SUCCESS;
    }

    public static boolean repairBuilding() {
        HashMap<Resource, Integer> cost = new HashMap<>();
        cost.put(Resource.GOLD_COIN, currentBuilding.howMuchToRepair());

        if (currentGame.getCurrentGovernment().purchase(cost)) {
            currentBuilding.repair();
            return true;
        }
        return false;
    }

    public static MenuMessages moveUnitTo(int destinationX, int destinationY) {
        if (!checkLocation(destinationX, destinationY))
            return MenuMessages.INVALID_LOCATION;

        if (!currentUnit.canGoTo(currentGame.getTexture(destinationX, destinationY)))
            return MenuMessages.CELL_HAS_INCOMPATIBLE_TEXTURE;

        if (currentUnit.isPatrolling())
            return MenuMessages.IS_PATROLLING;

        if (currentUnit.getRemainingMovement() <= 0) return MenuMessages.NO_REMAINING_MOVEMENT;

        autoMoveUnit(currentUnit, destinationX, destinationY);

        currentUnit.addDestination(destinationX, destinationY);
        //we remove reached destinations in nextTurn() so that the unit can be momentarily seen as moving.
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

    public static MenuMessages attack(int enemyX, int enemyY, boolean isRanged) {
        if (currentUnit.hasAttacked()) return MenuMessages.ALREADY_ATTACKED;

        int myRange = 1, myDamage = currentUnit.getMeleeDamage();
        if (isRanged)
            for (Attribute attribute :
                    currentUnit.getAttributes())
                if (attribute instanceof RangedAttack) {
                    myRange = ((RangedAttack) attribute).getRange();
                    myDamage = ((RangedAttack) attribute).getRangedDamage();
                    break;
                }

        int myX = currentUnit.getCurrentX(), myY = currentUnit.getCurrentY();
        int distanceX = Math.abs(myX - enemyX), distanceY = Math.abs(myY - enemyY);
        if (distanceX > myRange || distanceY > myRange)
            return MenuMessages.TOO_FAR;

        Unit enemyUnit = null;
        for (Unit unit :
                currentGame.getUnits(enemyX, enemyY))
            if (!unit.getGovernmentColor().equals(currentUnit.getGovernmentColor())) {
                enemyUnit = unit;
                break;
            }
        if (enemyUnit == null) return MenuMessages.NO_MATCHING_UNIT;

        //TODO: attack buildings
        //damaging the enemy unit:
        if (enemyUnit.reduceHP(myDamage)) {
            AbstractMenu.show("Enemy unit wasted.");
            currentGame.removeUnit(enemyUnit, enemyX, enemyY);
        }

        //calculating the damage the enemy unit can deal to us:
        int enemyDamage = enemyUnit.getMeleeDamage();
        if (isRanged) {
            enemyDamage = 0; //the enemy unit cannot defend against a ranged attack unless it has RangedAttack itself.
            for (Attribute attribute :
                    enemyUnit.getAttributes())
                if (attribute instanceof RangedAttack) {
                    int enemyRange = ((RangedAttack) attribute).getRange();
                    if (distanceX <= enemyRange && distanceY <= enemyRange)
                        enemyDamage = ((RangedAttack) attribute).getRangedDamage();
                    break;
                }
        }

        //damaging our unit:
        if (!currentUnit.reduceHP(enemyDamage)) {
            AbstractMenu.show("Your unit died in battle!");
            currentGame.removeUnit(currentUnit, myX, myY);

            MainController.setCurrentMenu(AbstractMenu.MenuName.GAME_MENU);
            currentUnit = null;
            AbstractMenu.show("Unit unselected.");
        }

        return MenuMessages.SUCCESS;
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
