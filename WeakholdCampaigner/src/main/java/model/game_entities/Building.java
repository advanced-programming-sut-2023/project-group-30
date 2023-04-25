package model.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends GameEntity {
    private Category category;

    protected Building(HashMap<Resource, Integer> productionCost, EntityName name, ArrayList<Attribute> attributes) {
        super(productionCost, name, attributes);
    }

    public enum Category {
        CASTLE,
        INDUSTRY,
        FARM,
        TOWN,
        WEAPONS,
        FOOD_PROCESSING
    }
}
