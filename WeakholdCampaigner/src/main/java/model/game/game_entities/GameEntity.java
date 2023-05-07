package model.game.game_entities;

import model.enums.Resource;
import model.attributes.Attribute;
import model.game.Government.GovernmentColor;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameEntity {
    protected HashMap<Resource, Integer> productionCost;
    protected EntityName name;
    protected ArrayList<Attribute> attributes;
    protected GovernmentColor governmentColor;

    public GameEntity(HashMap<Resource, Integer> productionCost, EntityName name, ArrayList<Attribute> attributes) {
        this.productionCost = productionCost;
        this.name = name;
        this.attributes = attributes;
        this.governmentColor = null;
    }

    public void setGovernmentColor(GovernmentColor governmentColor) { //This is expected to be called (?)
        this.governmentColor = governmentColor;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public HashMap<Resource, Integer> getProductionCost() {
        return productionCost;
    }
}
