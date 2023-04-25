package model.game_entities;

import model.enums.Resource;
import model.attributes.Attribute;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameEntity {
    protected HashMap<Resource, Integer> productionCost;
    protected EntityName name;
    protected ArrayList<Attribute> attributes;

    public GameEntity(HashMap<Resource, Integer> productionCost, EntityName name, ArrayList<Attribute> attributes) {
        this.productionCost = productionCost;
        this.name = name;
        this.attributes = attributes;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
