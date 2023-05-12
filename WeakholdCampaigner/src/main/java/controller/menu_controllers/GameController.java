package controller.menu_controllers;

import model.game.Game;

public abstract class GameController {
    protected static Game currentGame;


    protected static boolean checkLocation(int x, int y) {
        //x and y must be indexed from 0.
        return currentGame.getMapX() > x && 0 <= x && currentGame.getMapY() > y && 0 <= y;
    }
}
