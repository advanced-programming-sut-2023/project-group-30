package model.attributes.building_attributes;

import controller.menu_controllers.GameMenuController;
import model.game.Game;
import model.game.Government;

public class IncreasePopularity implements BuildingAttribute {
    private final int amount;

    public IncreasePopularity(int amount) {
        this.amount = amount;
    }

    public void nextTurn() {
        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        government.addPopularityOfReligion(amount);
    }
}
