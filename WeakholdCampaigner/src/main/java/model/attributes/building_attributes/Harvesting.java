package model.attributes.building_attributes;

import controller.menu_controllers.GameMenuController;
import controller.messages.MenuMessages;
import model.enums.Resource;
import model.game.Game;
import model.game.Government;

public class Harvesting implements BuildingAttribute {
    private final Resource harvestedResource;
    private double rate;

    public Harvesting(Resource harvestedResource, double rate) {
        this.harvestedResource = harvestedResource;
        this.rate = rate;
    }

    public Resource getHarvestedResource() {
        return harvestedResource;
    }
    public void harvestForNextTurn() {
        //TODO : check capacity

        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        government.addResources(harvestedResource, rate * 20);

    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
