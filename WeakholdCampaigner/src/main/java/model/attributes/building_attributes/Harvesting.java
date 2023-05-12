package model.attributes.building_attributes;

import controller.menu_controllers.GameMenuController;
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

    public void nextTurn() {
        Game game = GameMenuController.getCurrentGame();
        Government government = game.getCurrentGovernment();
        if (government.getMaximumResource(government.getResourcesCategory(harvestedResource))
                >= (government.getStoredUnit(government.getResourcesCategory(harvestedResource)) + 20))
            government.addResources(harvestedResource, rate * 20);
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
