package model.game.game_entities;

import model.enums.Resource;
import model.attributes.Attribute;
import model.game.Government.GovernmentColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameEntity {
    protected HashMap<Resource, Integer> productionCost;
    protected ArrayList<Attribute> attributes;
    protected GovernmentColor governmentColor;

    public GameEntity(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes) {
        this.productionCost = productionCost;
        this.attributes = attributes;
        this.governmentColor = null; //you should always call setGovernmentColor ?
    }

    public void setGovernmentColor(GovernmentColor governmentColor) { //This is expected to be called (?)
        this.governmentColor = governmentColor;
    }

    @NotNull
    public GovernmentColor getGovernmentColor() {
        return governmentColor;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public HashMap<Resource, Integer> getProductionCost() {
        return productionCost;
    }
}
