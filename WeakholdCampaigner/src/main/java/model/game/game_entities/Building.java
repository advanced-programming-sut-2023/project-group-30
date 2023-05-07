package model.game.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends GameEntity {
    private Category category;

    protected Building(HashMap<Resource, Integer> productionCost, EntityName name, ArrayList<Attribute> attributes) {
        super(productionCost, name, attributes);
    }

    @Nullable
    public static Building getInstance(String name) {
        BuildingName buildingName = BuildingName.getBuildingName(name);
        if (buildingName == null) return null;

        switch (buildingName) {
            //TODO
            case A:
                break;
        }

        return null;
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
