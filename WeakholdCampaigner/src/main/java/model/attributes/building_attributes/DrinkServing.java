package model.attributes.building_attributes;

import controller.menu_controllers.GameMenuController;
import model.game.Game;

public class DrinkServing implements BuildingAttribute{
    public void callForNextTurn() {
        Game game = GameMenuController.getCurrentGame();
    }

}
