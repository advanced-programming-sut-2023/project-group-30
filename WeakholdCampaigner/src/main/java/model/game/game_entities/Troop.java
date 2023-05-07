package model.game.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Troop extends Unit {
    private Troop(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes) {
        super(productionCost, attributes);
    }

    public static Unit getInstance(EntityName entityName) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        HashMap<Resource, Integer> productionCost = new HashMap<>();
        switch (entityName) {
            case ARCHER:
                productionCost = null;
                break;
        }

        return new Troop(productionCost, attributes);
    }
}
