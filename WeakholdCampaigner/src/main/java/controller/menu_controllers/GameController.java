package controller.menu_controllers;

import model.game.Game;
import model.game.game_entities.Unit;

public abstract class GameController {
    protected static Game currentGame;


    protected static boolean checkLocation(int x, int y) {
        //x and y must be indexed from 0.
        return currentGame.getMapX() > x && 0 <= x && currentGame.getMapY() > y && 0 <= y;
    }

    protected static void autoMoveUnit(Unit unit, int destinationX, int destinationY) {
        int[] tempDestination = currentGame.move(
                unit.getCurrentX(), unit.getCurrentY(), destinationX, destinationY,
                unit.getRemainingMovement());
        unit.setCurrentLocation(tempDestination[0], tempDestination[1]);
        unit.setRemainingMovement(tempDestination[2]);
    }

    //TODO: consider the neighbouring unit's stance
}
