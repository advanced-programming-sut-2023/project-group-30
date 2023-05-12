package controller.menu_controllers;

import model.attributes.Attribute;
import model.attributes.unit_attributes.RangedAttack;
import model.game.Game;
import model.game.game_entities.Unit;
import view.menus.AbstractMenu;

public abstract class GameController {
    protected static Game currentGame;


    protected static boolean checkLocation(int x, int y) {
        //x and y must be indexed from 0.
        return currentGame.getMapWidth() > x && 0 <= x && currentGame.getMapHeight() > y && 0 <= y;
    }

    protected static void autoMoveUnit(Unit unitToMove, int destinationX, int destinationY) {
        //moving the unit:
        int[] currentLocation = simpleUnitMovement(unitToMove, destinationX, destinationY);

        //taking the neighbouring units' stances into effect:
        int currentX = currentLocation[0], currentY = currentLocation[1];
        //loop through nearby MapCells:
        for (int i = Math.max(0, currentX - Unit.fieldOfView);
             i < Math.min(currentGame.getMapWidth(), currentX + Unit.fieldOfView + 1);
             i++)
            for (int j = Math.max(0, currentY - Unit.fieldOfView);
                 j < Math.min(currentGame.getMapHeight(), currentY + Unit.fieldOfView + 1);
                 j++)
                loopThroughEnemyUnits(i, j, unitToMove, currentX, currentY);
    }

    private static int[] simpleUnitMovement(Unit unitToMove, int destinationX, int destinationY) {
        int[] tempDestination = currentGame.move(
                unitToMove.getCurrentX(), unitToMove.getCurrentY(), destinationX, destinationY,
                unitToMove.getRemainingMovement());
        unitToMove.setCurrentLocation(tempDestination[0], tempDestination[1]);
        unitToMove.setRemainingMovement(tempDestination[2]);

        return tempDestination;
    }

    private static void loopThroughEnemyUnits(int i, int j, Unit movingUnit, int currentX, int currentY) {
        boolean movingUnitDied = false;

        for (Unit enemyUnit :
                currentGame.getUnits(i, j))
            if (!enemyUnit.getGovernmentColor().equals(movingUnit.getGovernmentColor())) {
                boolean shouldMove = false;
                switch (enemyUnit.getStance()) {
                    case STAND_GROUND:
                        break;
                    case DEFENSIVE_STANCE:
                        if (Math.abs(currentX - i) <= Unit.fieldOfView/3 && Math.abs(currentY - j) <= Unit.fieldOfView/3) {
                            shouldMove = true;
                        }
                        break;
                    case AGGRESSIVE_STANCE:
                        shouldMove = true;
                        break;
                }

                //move the enemyUnit:
                if (shouldMove) simpleUnitMovement(enemyUnit, currentX, currentY);
                //attack the movingUnit:
                movingUnitDied = !autoAttack(enemyUnit, movingUnit);

                if (movingUnitDied) break;
            }
    }

    //this is only here to reduce code's indentation depth:

    private static boolean autoAttack(Unit attacker, Unit defender) { //returns false if the defender dies.
        int attackRange = 1, attackDamage = attacker.getMeleeDamage();

        //Pick RangedAttack over MeleeAttack if possible:
        for (Attribute attribute :
                attacker.getAttributes())
            if (attribute instanceof RangedAttack) {
                attackRange = ((RangedAttack) attribute).getRange();
                attackDamage = ((RangedAttack) attribute).getRangedDamage();
            }

        int defenderX = defender.getCurrentX(), defenderY = defender.getCurrentY();
        if (Math.abs(attacker.getCurrentX() - defenderX) <= attackRange &&
                Math.abs(attacker.getCurrentY() - defenderY) <= attackRange) { //attack if in range:
            AbstractMenu.show("While moving, one " + defender.getGovernmentColor() + " " +
                    defender.getUnitName() + " got attacked by one " + attacker.getGovernmentColor() + " " +
                    attacker.getUnitName() + "!");
            if (!defender.reduceHP(attackDamage)) {
                AbstractMenu.show("And it died.");
                currentGame.removeUnit(defender, defenderX, defenderY);
                return false;
            }
        }

        return true;
    }
}
