package model.game.game_entities;


import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
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
    protected int[] currentLocation;


    public GameEntity(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes, int x, int y) {
        this.productionCost = productionCost;
        this.attributes = attributes;
        this.governmentColor = null; //you should always call setGovernmentColor ?
        this.currentLocation = new int[]{x, y};

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

    public int getCurrentX() {
        return currentLocation[0];
    }

    public int getCurrentY() {
        return currentLocation[1];
    }
}
